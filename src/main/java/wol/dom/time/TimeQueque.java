package wol.dom.time;

import wol.dom.Entity;
import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.LatentEffect;
import wol.dom.space.planets.Planetoid;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 1-feb-2010
 * Time: 23.45.29
 * To change this template use File | Settings | File Templates.
 */
public class TimeQueque<E extends Entity> implements iTime<E>{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5727897362144888852L;
    private List<iEventObserver<E>> observers=new ArrayList<iEventObserver<E>>();

    private class QuequeElement{
        private List<LatentEffect<E>> elements;
        private Long count;

        public QuequeElement(){
        	elements=new LinkedList<LatentEffect<E>>();
        }
    }
    
    public void run() {
       List<LatentEffect<E>> latentEffects=getPresent();
        if (latentEffects!=null&&!latentEffects.isEmpty()){
            for(LatentEffect<E> latentEffect:latentEffects){
            	latentEffect.setDelay(0);
                 for(iEventObserver<E> curObserver:observers){
                    curObserver.processEvent(latentEffect);
                    }
            }

        }
    }

    public void processEvent(iEvent<E> event) {
    	if (event instanceof LatentEffect&&((LatentEffect<E>)event).getDelay()>0){
    		LatentEffect<E> tEvent=(LatentEffect<E>)event;
     	   setFuture(tEvent);
        }
    }

    
    
    private List<QuequeElement> timeList;
    private Map<iEvent<E>,QuequeElement> index;
    public TimeQueque(){
         timeList=new  LinkedList<QuequeElement>();
        index=new HashMap<iEvent<E>,QuequeElement>();
    }

    public List<LatentEffect<E>> getPresent(){
    	QuequeElement headTime=null;
    	if (!timeList.isEmpty()){
    		headTime=timeList.get(0);
    		headTime.count-=1;
    	}
    	if (headTime!=null&&headTime.count==0){
    		timeList.remove(headTime);
    		return headTime.elements;
    	}else{
    		 return null;
    	}
    }


    public void addObserver(iEventObserver<E> observer) {
        observers.add(observer);
    }

    private void setFuture(LatentEffect<E> element){
        QuequeElement curElement=null;

        long curDelay=element.getDelay();
        int curIndex=0;

        do{
           if (timeList.size()>curIndex){
            curElement=timeList.get(curIndex);
            curDelay-=curElement.count;
            }
        }while(curDelay>0&&curElement!=null);

        if (curElement==null&&curDelay>0){//New last element
            if (curDelay<Long.MAX_VALUE){
                QuequeElement lastElement=new QuequeElement();
                lastElement.count=new Long(curDelay);
                lastElement.elements.add(element);
                timeList.add(lastElement);
            }else{//curDelay>Integer.MAX_VALUE
                long splitDelay=curDelay;
                while(splitDelay>Long.MAX_VALUE){
                   QuequeElement emptytElement=new QuequeElement();
                   emptytElement.count=new Long(Long.MAX_VALUE);
                   timeList.add(emptytElement);
                   splitDelay-=Integer.MAX_VALUE;
                }
                
                QuequeElement lastElement=new QuequeElement();
                lastElement.count=new Long(splitDelay);
                lastElement.elements.add(element);
                timeList.add(lastElement);
            }
        }else if (curDelay==0){//Current position
             curElement.elements.add(element);
        }else{//insert element between
            int insertDelay=(int)curDelay*-1;
            QuequeElement insertElement=new QuequeElement();
            insertElement.count=new Long(insertDelay);
            insertElement.elements.add(element);
            timeList.add(curIndex,insertElement);
            curElement.count-=insertDelay;//Subtract delay
        }
    }

    public boolean removeFuture(LatentEffect<E> element){
    	boolean removed=false;
    	if (index.containsKey(element)){
    		QuequeElement queue=index.get(element);
    		removed=queue.elements.remove(element);
    	}
        return removed;
    }
}

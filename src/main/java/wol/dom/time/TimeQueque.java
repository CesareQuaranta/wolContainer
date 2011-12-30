package wol.dom.time;

import wol.dom.Entity;
import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.iLatentEffect;
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

    public void run() {
       List<iEvent<E>> latentEffects=getPresent();
        if (latentEffects!=null&&!latentEffects.isEmpty()){
            for(iEvent<E> latentEffect:latentEffects){
                 for(iEventObserver<E> curObserver:observers){
                    curObserver.processEvent(latentEffect);
                    }
            }

        }
    }

    public void processEvent(iEvent<E> event) {
    	if (event instanceof TimeEvent){
     	   TimeEvent<E> tEvent=(TimeEvent<E>)event;
     	   setFuture(tEvent.getSeed().getLatentEffect(),tEvent.getFuture());
        }
    }

    private class QuequeElement{
        private List<iEvent<E>> elements;
        private Integer count;

        public QuequeElement(){
        	elements=new LinkedList<iEvent<E>>();
        }
        public List<iEvent<E>> getElements() {
            return elements;
        }

        public void setElements(List<iEvent<E>> elements) {
            this.elements = elements;
        }
    }
    private List<QuequeElement> timeList;
    private Map<iEvent<E>,QuequeElement> index;
    public TimeQueque(){
         timeList=new  LinkedList<QuequeElement>();
        index=new HashMap<iEvent<E>,QuequeElement>();
    }

    public List<iEvent<E>> getPresent(){
    	QuequeElement present=null;
    	if (!timeList.isEmpty()){
    		present=timeList.remove(0);
    	}
    	if (present!=null){
    		return present.getElements();
    	}else{
    		 return null;
    	}
    }


    public void addObserver(iEventObserver<E> observer) {
        observers.add(observer);
    }

    private void setFuture(iEvent<E> element,long delay){
        QuequeElement curElement=null;

        long curDelay=delay;
        int curIndex=0;

        do{
           if (timeList.size()>curIndex){
            curElement=timeList.get(curIndex);
            curDelay-=curElement.count;
            }
        }while(curDelay>0&&curElement!=null);

        if (curElement==null&&curDelay>0){//New last element
            if (curDelay<Integer.MAX_VALUE){
                QuequeElement lastElement=new QuequeElement();
                lastElement.count=new Integer((int)curDelay);
                lastElement.elements.add(element);
                timeList.add(lastElement);
            }else{//curDelay>Integer.MAX_VALUE
                long splitDelay=curDelay;
                while(splitDelay>Integer.MAX_VALUE){
                   QuequeElement emptytElement=new QuequeElement();
                   emptytElement.count=new Integer(Integer.MAX_VALUE);
                   timeList.add(emptytElement);
                   splitDelay-=Integer.MAX_VALUE;
                }
                
                QuequeElement lastElement=new QuequeElement();
                lastElement.count=new Integer((int)splitDelay);
                lastElement.elements.add(element);
                timeList.add(lastElement);
            }
        }else if (curDelay==0){//Current position
             curElement.elements.add(element);
        }else{//insert element between
            int insertDelay=(int)curDelay*-1;
            QuequeElement insertElement=new QuequeElement();
            insertElement.count=new Integer(insertDelay);
            insertElement.elements.add(element);
            timeList.add(curIndex,insertElement);
            curElement.count-=insertDelay;//Subtract delay
        }
    }

    public boolean removeFuture(iLatentEffect<E> element){
    	boolean removed=false;
    	if (index.containsKey(element)){
    		QuequeElement queue=index.get(element);
    		removed=queue.getElements().remove(element);
    	}
        return removed;
    }
}

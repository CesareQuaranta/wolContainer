package wol.dom.time;

import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.iLatentEffect;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 1-feb-2010
 * Time: 23.45.29
 * To change this template use File | Settings | File Templates.
 */
public class TimeQueque implements iTime<iLatentEffect>{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5727897362144888852L;
    private List<iEventObserver> observers=new ArrayList<iEventObserver>();

    public void run() {
       List<iLatentEffect> latentEffects=getPresent();
        if (latentEffects!=null&&!latentEffects.isEmpty()){
            for(iLatentEffect latentEffect:latentEffects){
                 for(iEventObserver curObserver:observers){
                    curObserver.processEvent(latentEffect);
                    }
            }

        }
    }

    public void processEvent(iEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private class QuequeElement{
        private List<iLatentEffect> elements;
        private Integer count;

        public List<iLatentEffect> getElements() {
            return elements;
        }

        public void setElements(List<iLatentEffect> elements) {
            this.elements = elements;
        }
    }
    private List<QuequeElement> timeList;
    private Map<iLatentEffect,QuequeElement> index;
    public TimeQueque(){
         timeList=new  LinkedList<QuequeElement>();
        index=new HashMap<iLatentEffect,QuequeElement>();
    }

    public List<iLatentEffect> getPresent(){
    	QuequeElement present=timeList.remove(0);
    	if (present!=null){
    		return present.getElements();
    	}else{
    		 return null;
    	}
    }


    public void addObserver(iEventObserver observer) {
        observers.add(observer);
    }

    public void setFuture(iLatentEffect element,long delay){
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

    public boolean removeFuture(iLatentEffect element){
    	boolean removed=false;
    	if (index.containsKey(element)){
    		QuequeElement queue=index.get(element);
    		removed=queue.getElements().remove(element);
    	}
        return removed;
    }
}

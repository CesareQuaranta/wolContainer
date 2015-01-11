package wol.dom.time;

import wol.dom.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 1-feb-2010
 * Time: 23.45.29
 * To change this template use File | Settings | File Templates.
 */
public class TimeQueque<E extends Entity> implements iTimeManager<E> {

	private static final long serialVersionUID = -5727897362144888852L;
    private List<iEventObserver<E>> observers=new ArrayList<iEventObserver<E>>();
    private List<QuequeElement> timeList;
    private Map<E,List<QuequeElement>> entityIndex;
    private Map<Ichinen<E>,QuequeElement> index;
    
    public TimeQueque(){
         timeList=new  LinkedList<QuequeElement>();
         index=new HashMap<Ichinen<E>,QuequeElement>();
         entityIndex=new HashMap<E,List<QuequeElement>>();
    }
    
    public void run() {
       List<Ichinen<E>> present=getPresent();
        if (present!=null&&!present.isEmpty()){
	     for(iEventObserver<E> curObserver:observers){
	        curObserver.processEvent(new ManifestPresent<E>(present));
	        }
        }
    }

    public void processEvent(Phenomen<E> phenomen) {
 /*   	if (phenomen instanceof Effect /*&&((Effect<E>) phenomen).getDelay()>0){
    		Effect<E> tEvent=(Effect<E>) phenomen;
     	   setFuture(tEvent);
        }*/
    }


    public List<Ichinen<E>> getPresent(){
    	QuequeElement headTime=null;
    	if (!timeList.isEmpty()){
    		headTime=timeList.get(0);
    		headTime.count-=1;
    	}
    	if (headTime!=null && headTime.count<1){
    		timeList.remove(headTime);
    		for(Ichinen<E> presentIchinen:headTime.elements){
    			index.remove(presentIchinen);
    		}
    		
    		return headTime.elements;
    	}else{
    		 return null;
    	}
    }


    public void addObserver(iEventObserver<E> observer) {
        observers.add(observer);
    }

    public void addFuture(Ichinen<E> element,long delay){
    	
    	if(delay==0){
    		insertInHead(element,0);
    	}else{
    		int insertIndex=findInsertIndex(delay);
    		if(insertIndex>=timeList.size()){
    			long newDelay=delay-getSumDelay(timeList.size()-1);
    			insertLast(element,newDelay);
    		}else{
    			if(insertIndex==0){
    				insertInHead(element,delay);
    			}else{//insert element between
    				insertAt(element,insertIndex,delay);
    			}
    		}
    	}
    }
/*
	public boolean removeFuture(Effect<E> element){
    	boolean removed=false;
    	if (index.containsKey(element)){
    		QuequeElement queue=index.get(element);
    		removed=queue.elements.remove(element);
    	}
        return removed;
    }
*/
	@Override
	public void processEvent(iEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Time getTime(long time) {
		return new Time(this,time);
	}
	
	 private int findInsertIndex(long delay) {
	    	QuequeElement curElement=null;
	        long curDelay=delay;
	        int curIndex=0;
	        Iterator<QuequeElement> elements=timeList.iterator();
	        while(elements.hasNext()){
	        	curElement=elements.next();
	        	curDelay-=curElement.count;
	        	if(curDelay<=0)
	        		return curIndex;
	        	else
	        		curIndex++;
	        }
			return curIndex;
		}
	
	private void insertInHead(Ichinen<E> element, long delay){
		QuequeElement headElement=null;
		if(timeList.isEmpty()){
			headElement=new QuequeElement();
			headElement.count=delay;
			timeList.add(headElement);
		}else{
			headElement=timeList.get(0);
		}
		if(headElement.count>delay){
			QuequeElement newHeadElement=new QuequeElement();
			newHeadElement.count=delay;
			timeList.add(0,newHeadElement);
			headElement.count-=delay;
			headElement=newHeadElement;
		}
		index.put(element, headElement);
		headElement.elements.add(element);
		
	}
	
	private void insertLast(Ichinen<E> element,long delay){
		QuequeElement lastElement=new QuequeElement();
		lastElement.count=delay;
		lastElement.elements.add(element);
		timeList.add(lastElement);
		index.put(element, lastElement);
	}
	
	private void insertAt(Ichinen<E> element, int insertIndex,long delay) {
		long newDelay=delay-getSumDelay(insertIndex-1);
		if(newDelay==0){
			QuequeElement insertElement=timeList.get(insertIndex);
			insertElement.elements.add(element);
			index.put(element, insertElement);
		}else{
			QuequeElement nextElement=timeList.get(insertIndex);
			nextElement.count-=newDelay;
			
			QuequeElement newElement=new QuequeElement();
			newElement.count=newDelay;
			newElement.elements.add(element);
			timeList.add(insertIndex,newElement);
			index.put(element, newElement);
		}
		
	}
	
	private long getSumDelay(int index) {
		long sum=0;
		for(int i=0;i<=index;i++){
			sum+=timeList.get(i).count;
		}
		return sum;
	}

	@Override
	public void removeIchinen(Ichinen<E> ichinen) {
		QuequeElement QE=index.remove(ichinen);
		if(QE!=null){
			QE.elements.remove(ichinen);
		}
	}
	
    private class QuequeElement{
        private List<Ichinen<E>> elements;
        private Long count;

        public QuequeElement(){
        	count=0L;
        	elements=new LinkedList<Ichinen<E>>();
        }
    }
}

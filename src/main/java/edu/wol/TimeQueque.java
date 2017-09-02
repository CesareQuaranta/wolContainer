package edu.wol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.wol.dom.Phenomen;
import edu.wol.dom.WolEntity;
import edu.wol.dom.iEvent;
import edu.wol.dom.iEventObserver;
import edu.wol.dom.time.Ichinen;
import edu.wol.dom.time.ManifestPresent;
import edu.wol.dom.time.Time;
import edu.wol.dom.time.iTimeManager;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 1-feb-2010
 * Time: 23.45.29
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class TimeQueque<E extends WolEntity> implements iTimeManager<E> {

	public Long getLength() {
		return length;
	}

	private static final long serialVersionUID = -5727897362144888852L;
	@Id
	@GeneratedValue
	private long ID;
	
	private Float precision;
	private Long curTime;
	private Long length;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@OrderColumn(name="time_seq", nullable=false)
    private List<QuequeElement<WolEntity>> timeList;
    
	@Transient
    private List<iEventObserver<E>> observers=new ArrayList<iEventObserver<E>>();
    @Transient
    private Map<Long,List<QuequeElement<E>>> entityIndex;//Entity.ID->list of elements
    
    @Transient
    private Map<Ichinen<E>,QuequeElement<E>> index;
    
    protected TimeQueque(){
    	this(0);
    }
    public TimeQueque(float precision){
    	 this.precision = precision;
    	 curTime = 0L;
    	 length = 0L;
         timeList=new  LinkedList<QuequeElement<WolEntity>>();
         index=new HashMap<Ichinen<E>,QuequeElement<E>>();
         entityIndex=new HashMap<Long,List<QuequeElement<E>>>();
        
    }
    
    public void run() {
       List<Ichinen<E>> present=getPresent();
        if (present!=null&&!present.isEmpty()){
            length -= present.size();
	     for(iEventObserver<E> curObserver:observers){
	        curObserver.processEvent(new ManifestPresent<E>(present));
	        }
        }
        curTime++;
    }
    

    public void processEvent(Phenomen<E> phenomen) {
 /*   	if (phenomen instanceof Effect /*&&((Effect<E>) phenomen).getDelay()>0){
    		Effect<E> tEvent=(Effect<E>) phenomen;
     	   setFuture(tEvent);
        }*/
    }


    public List<Ichinen<E>> getPresent(){
    	QuequeElement<E> headTime=null;
    	if (!timeList.isEmpty()){
    		headTime=(TimeQueque<E>.QuequeElement<E>) timeList.get(0);
    		headTime.count-=1;
    	}
    	if (headTime!=null && headTime.count<1){
    		timeList.remove(headTime);
    		for(Ichinen<WolEntity> presentIchinen:headTime.elements){
    			index.remove(presentIchinen);
    		}
    		List<Ichinen<E>> rval=new ArrayList<Ichinen<E>>();
    		for(Ichinen<WolEntity> e:headTime.elements){
    			rval.add((Ichinen<E>)e);
    		}
    		return rval;
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
    	this.length++;
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
	    	QuequeElement<E> curElement=null;
	        long curDelay=delay;
	        int curIndex=0;
	        Iterator<QuequeElement<WolEntity>> elements=timeList.iterator();
	        while(elements.hasNext()){
	        	curElement=(TimeQueque<E>.QuequeElement<E>) elements.next();
	        	curDelay-=curElement.count;
	        	if(curDelay<=0)
	        		return curIndex;
	        	else
	        		curIndex++;
	        }
			return curIndex;
		}
	
	private void insertInHead(Ichinen<E> element, long delay){
		QuequeElement<E> headElement=null;
		if(timeList.isEmpty()){
			headElement=new QuequeElement<E>();
			headElement.count=delay;
			timeList.add((TimeQueque<E>.QuequeElement<WolEntity>) headElement);
		}else{
			headElement=(TimeQueque<E>.QuequeElement<E>) timeList.get(0);
		}
		if(headElement.count>delay){
			QuequeElement<E> newHeadElement=new QuequeElement<E>();
			newHeadElement.count=delay;
			timeList.add(0,(TimeQueque<E>.QuequeElement<WolEntity>) newHeadElement);
			headElement.count-=delay;
			headElement=newHeadElement;
		}
		index.put(element, headElement);
		headElement.elements.add((Ichinen<WolEntity>) element);
		
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
	
	@Entity
	@Table(name="WOL_TIMEQELEMENT")
    public class QuequeElement<ET extends WolEntity>{
		@Id
		@GeneratedValue
		private long ID;
		
		@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
		@OrderColumn(name="ichinen_seq", nullable=false)
        private List<Ichinen<WolEntity>> elements;
		
        private Long count;

        public QuequeElement(){
        	count=0L;
        	elements=new LinkedList<Ichinen<WolEntity>>();
        }
    }

	public Long getCurTime() {
		return curTime;
	}

	public float getPrecision() {
		return precision;
	}
}

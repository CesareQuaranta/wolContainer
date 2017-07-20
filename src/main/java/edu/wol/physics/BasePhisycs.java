package edu.wol.physics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import edu.wol.TimeQueque;
import edu.wol.dom.ExternalCause;
import edu.wol.dom.WolEntity;
import edu.wol.dom.iEvent;
import edu.wol.dom.iEventObserver;
import edu.wol.dom.phisycs.Acceleration;
import edu.wol.dom.phisycs.Force;
import edu.wol.dom.phisycs.Forces;
import edu.wol.dom.phisycs.Inertia;
import edu.wol.dom.phisycs.Velocity;
import edu.wol.dom.phisycs.iPhisycs;
import edu.wol.dom.space.Movement;
import edu.wol.dom.space.Position;
import edu.wol.dom.space.Space;
import edu.wol.dom.space.Vector3f;
import edu.wol.dom.time.Ichinen;

/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 17/08/12
 * Time: 23.25
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BasePhisycs<E extends WolEntity,S extends Space<E,Position>> implements iPhisycs<E>{
	@Id
	@GeneratedValue
	private long ID;
	
	protected float spacePrecision;
	protected float maxVelocity;
	protected float timePrecision;
	
	@OneToOne(cascade=CascadeType.ALL)
    protected S space;
	@OneToOne(cascade=CascadeType.ALL)
    protected TimeQueque<E> time;
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKeyJoinColumn(name="ID")
	protected Map<Long,E> entityMap;//TODO Centralizzare
	
	/*@ElementCollection
	@CollectionTable(
	        name="PHISYCS_FORCES",
	        joinColumns=@JoinColumn(name="PHISYC_ID")
	  )*/
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    protected Map<Long,Forces<E>> forcesIndex;
    @Transient
    protected Map<Long, Velocity> velocityIndex;
    @Transient
    protected Map<Force,Ichinen<E>> activeForces;
    @Transient
    protected List<Long> heap;//Heap of entity to process in next cycle
    @Transient
    protected Map<Long, Ichinen<E>> ichinens;

	@Transient
	protected List<iEventObserver<E>> observers = new ArrayList<iEventObserver<E>>();

	public void run() {
		while(!heap.isEmpty()){//Process Heap
			Long curEntity= heap.remove(0);
			Forces<E> activeForces=forcesIndex.get(curEntity);
			if(activeForces!=null && !activeForces.isEmpty()){//Process active forces
				insertAccellerationIchinen(curEntity,activeForces);
			}
			
			Velocity actualVelocity=velocityIndex.get(curEntity);
			if(actualVelocity!=null && !actualVelocity.isEmpty()){//Process inertial velocity
				insertInertiaIchinen(curEntity);
			}
			
		}
	}

	public void applyForce(E entity, Force force) {
		if(!entityMap.containsKey(entity.getID())){
			entityMap.put(entity.getID(), entity);
		}
		if(!forcesIndex.containsKey(entity.getID())){
			forcesIndex.put(entity.getID(), new Forces<E>());
		}
		forcesIndex.get(entity.getID()).add(force);
		if(!heap.contains(entity.getID())){
			heap.add(entity.getID());
		}
		
	}

	public void removeForce(E entity, Force force) {
		Forces<E> forces=forcesIndex.get(entity.getID());
		if(forces!=null && !forces.isEmpty() && forces.remove(force)){
	
			Ichinen<E> forceIchinen=(Ichinen<E>) activeForces.get(force);
			if(forceIchinen!=null){
				time.removeIchinen(forceIchinen);//TODO Apply partial ichinen before remove
			}
			
			if(forces.isEmpty()){
				forcesIndex.remove(entity.getID());
				insertInertiaIchinen(entity.getID());
			}else if(!heap.contains(entity.getID())){
				heap.add(entity.getID());
			}
		}		
	}

    protected Acceleration calcAcceleration(Long entity,Forces<E> forces){
    	Force resultForce=new Force();
        for (Force curForce : forces) {
        	resultForce.sum(curForce);
        }
        return resultForce.getAcceleration();
    }
    
    protected void insertAccellerationIchinen(Long entityId,Forces<E> forces){
    	E entity=entityMap.get(entityId);
    	if(!forces.isEmpty()){
			long future=-1;
			Velocity curVelocity=velocityIndex.get(entityId);
			if(curVelocity==null){
				curVelocity=new Velocity(0);
			}
			Acceleration accPowr = calcAcceleration(entityId,forces);
			Velocity finalVelocity = curVelocity.sum(accPowr);
			if(finalVelocity.getIntensity()>accPowr.getIntensity()){
				future=calculateFuture(finalVelocity);
			}else{
				future=calculateFuture(accPowr);
			}
			
			//Convert from m/s to spacePrecision/timePrecision
			float precisionFactor=(future+1)*timePrecision;
			Vector3f relativeAccelleration=accPowr.getVector().clone();
			relativeAccelleration.scale(precisionFactor);
			Vector3f newValocityVect=curVelocity.getVector().clone();
			newValocityVect.add(relativeAccelleration);
			Velocity newVelocity=new Velocity(newValocityVect);
			finalVelocity.getVector().scale(precisionFactor);
			Movement<E> effect=new Movement<E>(entity,finalVelocity.getVector());
			ExternalCause<E> externalCause=forces;
			Ichinen<E> ichinen=new Ichinen<E>(entity);
			ichinen.setAction(newVelocity);
			ichinen.setEffect(effect);
			ichinen.setExternalCause(externalCause);
			ichinen.setPower(accPowr);
			insertIchinen(ichinen,future);
			for(Force curForce:forces){
				activeForces.put(curForce, (Ichinen<E>) ichinen);
				}
			}
	}
    
    protected void insertInertiaIchinen(Long entityId){
    	E entity=entityMap.get(entityId);
		Velocity velocity=velocityIndex.get(entityId);
		if(velocity==null){
			System.err.println("No Velocity 4 "+entityId);
		}
		long future=calculateFuture(velocity);
		//Convert from m/s to spacePrecision/timePrecision
		float precisionFactor=(future+1)*timePrecision;
		velocity.getVector().scale(precisionFactor);
		Movement<E> effect=new Movement<E>(entity,velocity.getVector());
		Ichinen<E> ichinen=new Ichinen<E>(entity);
		ichinen.setAction(velocity);
		ichinen.setEffect(effect);
		ichinen.setPower(new Inertia());
		insertIchinen(ichinen,future);
	}
    protected void insertIchinen(Ichinen<E> ichinen,long future){
		ichinens.put(ichinen.getEntity().getID(), (Ichinen<E>) ichinen);
		if(future>-1){
			time.addFuture( ichinen,future);
		}else{
			System.out.println("Errore inserimento Ichinen "+ichinen);
		}
		
	}

	protected long calculateFuture(Velocity velocity){
		long future=-1;
			//TODO Da spostare la funzione max nella classe Vector
			float maxFactor=Math.max(Math.abs(velocity.getVector().getX()), Math.abs(velocity.getVector().getY()));
			maxFactor=Math.max(maxFactor, Math.abs(velocity.getVector().getZ()));
			double timeToMinMovement=velocity.getTime()/maxFactor;
			
			//convert from m/s a spacePrecision/timePrecision
			timeToMinMovement=timeToMinMovement*(spacePrecision/timePrecision);
			future=Math.round(timeToMinMovement);
		/*
			while((maxFactor*precisionFactor*future)<1)
				future++;*/
			if (future>0){
				future--;
			}
		return future;
	}
    public void fireEvent(iEvent phenomen) {
        for (iEventObserver<E> observer : observers) {
            observer.processEvent(phenomen);
        }
    }

    public void addObserver(iEventObserver<E> observer) {
        observers.add(observer);
    }
}

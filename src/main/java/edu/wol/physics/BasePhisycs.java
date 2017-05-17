package edu.wol.physics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

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
import edu.wol.dom.space.Vector;
import edu.wol.dom.space.Planetoid;
import edu.wol.dom.space.iSpace;
import edu.wol.dom.time.Ichinen;
import edu.wol.dom.time.iTimeManager;

/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 17/08/12
 * Time: 23.25
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BasePhisycs<E extends WolEntity> implements iPhisycs<E>{
	@Id
	@GeneratedValue
	private long ID;
	
	protected List<iEventObserver<E>> observers = new ArrayList<iEventObserver<E>>();
    protected iSpace<Planetoid, Position> space;
    protected iTimeManager<E> time;
    protected Map<E,Collection<Force>> forcesIndex;
    protected Map<Planetoid, Velocity> velocityIndex;
    protected Map<Force,Ichinen<E>> activeForces;
    protected List<E> heap;
	protected Map<E, Ichinen<E>> ichinens;
	protected float spacePrecision;
	protected float maxVelocity;
	protected float timePrecision;


   
	public void run() {
		while(!heap.isEmpty()){//Process new forces
			E curEntity=heap.remove(0);
			Collection<Force> activeForces=forcesIndex.get(curEntity);
			if(activeForces!=null && !activeForces.isEmpty()){
				insertAccellerationIchinen(curEntity,activeForces);
			}else{
				Velocity actualVelocity=velocityIndex.get(curEntity);
				if(actualVelocity!=null && !actualVelocity.isEmpty()){
					insertInertiaIchinen(curEntity);
				}
			}
			
		}
		//Process inertia
	}

	public void applyForce(E entity, Force force) {
		if(!forcesIndex.containsKey(entity)){
			forcesIndex.put(entity, new ArrayList<Force>());
		}
		forcesIndex.get(entity).add(force);
		if(!heap.contains(entity)){
			heap.add(entity);
		}
		
	}

	public void removeForce(E entity, Force force) {
		Collection<Force> forces=forcesIndex.get(entity);
		if(forces!=null && !forces.isEmpty() && forces.remove(force)){
	
			Ichinen<E> forceIchinen=activeForces.get(force);
			if(forceIchinen!=null){
				time.removeIchinen(forceIchinen);//TODO Apply partial ichinen before remove
			}
			
			if(forces.isEmpty()){
				forcesIndex.remove(entity);
				insertInertiaIchinen(entity);
			}else if(!heap.contains(entity)){
				heap.add(entity);
			}
		}		
	}

    protected Acceleration calcAcceleration(E entity,Collection<Force> forces){
    	Force resultForce=new Force();
        for (Force curForce : forces) {
        	resultForce.sum(curForce);
        }
        return resultForce.getAcceleration();
    }
    
    protected void insertAccellerationIchinen(E entity,Collection<Force> forces){
		if(!forces.isEmpty()){
			long future=-1;
			Velocity curVelocity=velocityIndex.get(entity);
			Acceleration accPowr = calcAcceleration(entity,forces);
			Velocity finalVelocity = curVelocity.sum(accPowr);
			
			if(finalVelocity.getIntensity()>accPowr.getIntensity()){
				future=calculateFuture(finalVelocity);
			}else{
				future=calculateFuture(accPowr);
			}
			
			//Convert from m/s to spacePrecision/timePrecision
			float precisionFactor=(future+1)*timePrecision;
			Vector relativeAccelleration=accPowr.getVector().multiply(precisionFactor);
			Velocity newVelocity=new Velocity(curVelocity.getVector().sum(relativeAccelleration));
			Vector moveVector=finalVelocity.getVector().multiply(precisionFactor);
			Movement<E> effect=new Movement<E>(entity,moveVector);
			ExternalCause<E> externalCause=new Forces<E>(forces);
			Ichinen<E> ichinen=new Ichinen<E>(entity);
			ichinen.setAction(newVelocity);
			ichinen.setEffect(effect);
			ichinen.setExternalCause(externalCause);
			ichinen.setPower(accPowr);
			insertIchinen(ichinen,future);
			for(Force curForce:forces){
				activeForces.put(curForce, ichinen);
				}
			}
	}
    
    protected void insertInertiaIchinen(E entity){
		Velocity velocity=velocityIndex.get(entity);
		if(velocity==null){
			System.err.println("No Velocity 4 "+entity);
		}
		long future=calculateFuture(velocity);
		//Convert from m/s to spacePrecision/timePrecision
		float precisionFactor=(future+1)*timePrecision;
		Vector moveVector=velocity.getVector().multiply(precisionFactor);
		Movement<E> effect=new Movement<E>(entity,moveVector);
		Ichinen<E> ichinen=new Ichinen<E>(entity);
		ichinen.setAction(velocity);
		ichinen.setEffect(effect);
		ichinen.setPower(new Inertia());
		insertIchinen(ichinen,future);
	}
    protected void insertIchinen(Ichinen<E> ichinen,long future){
		ichinens.put(ichinen.getEntity(), ichinen);
		if(future>-1){
			time.addFuture(ichinen,future);
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

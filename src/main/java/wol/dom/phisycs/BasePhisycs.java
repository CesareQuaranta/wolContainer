package wol.dom.phisycs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import wol.dom.Entity;
import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.iExternalCause;
import wol.dom.space.Movement;
import wol.dom.space.Position;
import wol.dom.space.Vector;
import wol.dom.space.iSpace;
import wol.dom.time.Ichinen;
import wol.dom.time.iTimeManager;
import wol.starsystem.planets.iPlanetoid;

/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 17/08/12
 * Time: 23.25
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasePhisycs<E extends Entity> implements iPhisycs<E>{
	private static final long serialVersionUID = 1L;
	protected List<iEventObserver<E>> observers = new ArrayList<iEventObserver<E>>();
    protected iSpace<iPlanetoid, Position> space;
    protected iTimeManager<E> time;
    protected Map<E,Collection<Force>> forcesIndex;
    protected Map<iPlanetoid, Velocity> velocityIndex;
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
			Vector relativeAccelleration=accPowr.multiply(precisionFactor);
			Velocity newVelocity=new Velocity(curVelocity.sum(relativeAccelleration));
			Vector moveVector=finalVelocity.multiply(precisionFactor);
			Movement<E> effect=new Movement<E>(entity,moveVector);
			iExternalCause<E> externalCause=new Forces<E>(forces);
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
		Vector moveVector=velocity.multiply(precisionFactor);
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
			float maxFactor=Math.max(Math.abs(velocity.getX()), Math.abs(velocity.getY()));
			maxFactor=Math.max(maxFactor, Math.abs(velocity.getZ()));
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

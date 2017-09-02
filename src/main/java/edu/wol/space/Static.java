/**
 * 
 */
package edu.wol.space;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import edu.wol.dom.WolEntity;
import edu.wol.dom.iEvent;
import edu.wol.dom.iEventObserver;
import edu.wol.dom.space.Position;
import edu.wol.dom.space.Space;
import edu.wol.dom.space.Vector3f;

/**
 * @author cesare
 * Spazio statico
 * Tutti i punti sono indefinitamente statici
 */
@Entity
public class Static<E extends WolEntity> extends Space<E,Position> {
   /**
	 * 
	 */
	private static final long serialVersionUID = 314658362855822928L;
	protected static final long spaceUnit=1L;
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(name="PlanetsMap",joinColumns = @JoinColumn( name = "Orbital_id"),inverseJoinColumns = @JoinColumn( name = "Planet_id"))
    protected Map<String,WolEntity> entitiesMap;//<SerializedPosition,Planetoid>
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    protected Map<Long,Position> posIndex;//Convenient posIndex idPlanetoid position
    @Transient
    protected List<iEventObserver<E>> observers;

    protected Static(){
    	this(0);
    }
    
    public Static(float precision){
    	this.precision = precision;
    	entitiesMap=new HashMap<String,WolEntity>();
        observers=new ArrayList<iEventObserver<E>>();
        posIndex=new HashMap<Long,Position>();
    }

    public Collection<E> getAllEntities(){
        return  (Collection<E>) entitiesMap.values();
    }
    
    public float getPrecision(){
    	return precision;
    }

    public boolean insertEntity(Position position,E entity){
    	boolean ok=false;
        if (!posIndex.containsKey(entity.getID())&&getEntity(position)==null){
        	boolean collision=false;
        	Iterator<E> planets=getAllEntities().iterator();
        	while(collision==false&&planets.hasNext()){
        		WolEntity checkPlanet=planets.next();
        		Vector3f checkPosition=posIndex.get(checkPlanet.getID());
        		double checkDistance=position.distance(checkPosition);
        		collision=checkDistance<(entity.getRadius()+checkPlanet.getRadius());
        		if(collision){
        			System.err.println("Impossibile inserire "+entity+" alle coordinate:"+position+" collisione con "+checkPlanet+" alle coordinate:"+checkPosition);
        		}
        	}
        	if(!collision){
        		String sPosition=position.serialize();
        		entitiesMap.put(sPosition,entity);
        		posIndex.put(entity.getID(),position);
        		
        		ok=true;
        	}	
        }
        return ok;
    }

	public E getEntity(Position position) {
        return (E) entitiesMap.get(position.serialize());  //To change body of implemented methods use File | Settings | File Templates.
    }
	
	public List<E> getEntities(Position position,long radius) {
		List<E> lEntities = new ArrayList<E>(0);
		for(Position curPosition:posIndex.values()){
			if(curPosition.distance(position)<radius){
				E e = (E) entitiesMap.get(curPosition.serialize());
				if(e != null){
					lEntities.add(e);
				}
			}
		}
        return lEntities;  
    }

    public Position getPosition(E entity) {
        return posIndex.get(entity.getID());  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void addObserver(iEventObserver<E> observer) {
        observers.add(observer);
    }
    
    protected void fireEvent(iEvent event){
    	for(iEventObserver<E> observer:observers){
            observer.processEvent(event);
    	}
    }

	@Override
	public boolean isEmpty() {
		return entitiesMap.isEmpty();
	}

	@Override
	public void processEvent(iEvent event) {
		// TODO Auto-generated method stub
	}
	
}

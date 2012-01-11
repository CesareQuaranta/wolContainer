/**
 * 
 */
package wol.dom.space.planets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.phisycs.Collision;
import wol.dom.space.Movement;
import wol.dom.space.Vector;
import wol.dom.space.iSpace;
import wol.dom.space.iSpaceEvent;

/**
 * @author cesare
 *
 */
public class PlanetSystem implements iSpace<Planetoid,Vector> {
   /**
	 * 
	 */
	private static final long serialVersionUID = 9186629741540928858L;
    private Map<Vector,Planetoid> space;
    private Map<Planetoid,Vector> index;
    private List<iEventObserver<Planetoid>> observers=new ArrayList<iEventObserver<Planetoid>>();

    public  PlanetSystem(){
            space=new HashMap<Vector,Planetoid>();//TODO da implementare hash map ottimizzata
            index=new HashMap<Planetoid,Vector>();
    }

    public Collection<Planetoid> getPlanets(){
        return index.keySet();
    }

    public boolean insertEntity(Vector position,Planetoid planet){
        if (!index.containsKey(planet)&&getEntity(position)==null){
        	 space.put(position,planet);
               index.put(planet,position);
            return true;
        }else{
            return false;
        }
    }

    public Planetoid getEntity(Vector position) {
        return space.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Vector getPosition(Planetoid planetoid) {
        return index.get(planetoid);  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void process(Planetoid planetoid, Movement<Planetoid> movement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void addObserver(iEventObserver<Planetoid> observer) {
        observers.add(observer);
    }


    public void processEvent(iEvent<Planetoid> event) {
    	if (event instanceof Movement&&((Movement)event).getDelay()==0){
    		internalProcessEvent((iSpaceEvent<Planetoid>)event);
         }
    }
    
    protected void internalProcessEvent(iSpaceEvent<Planetoid> event){
    	if(event instanceof Movement){
    		Movement<Planetoid> movement=(Movement<Planetoid>)event;
    		Planetoid planetoid=movement.getEntity();
    		Vector vector=movement.getVector();
    		Vector curPosition=getPosition(planetoid);
    		Vector newPosition=addVector(curPosition,vector);
    		List<Planetoid> collisionList=checkCollision(curPosition,newPosition);
    		if (collisionList!=null){
    			for(Planetoid curPlanet:collisionList){
    				fireEvent(new Collision<Planetoid>(planetoid,curPlanet));
    			}
    		}else{//Move
    			move(planetoid,newPosition);
    		}
    	}
    }
    private Vector addVector(Vector original,Vector vector){
    	Vector rValue=original.clone();
    	rValue.sum(vector);
    	return rValue;
    }
    
    private List<Planetoid> checkCollision(Vector startPoint,Vector endPoint){
    	List<Planetoid> collisionList=null;
    	//TODO Aggiungere circonferenza di collisione
    	Planetoid startPlanet=getEntity(startPoint);
    	Vector curPoint=startPoint.clone();
    	while(!curPoint.equals(endPoint)){
    		Planetoid curPlanet=getEntity(curPoint);
    		if (curPlanet!=null&&curPlanet!=startPlanet){
    			if (collisionList==null){
    				collisionList=new LinkedList<Planetoid>();
    			}
    			collisionList.add(curPlanet);
    		}
    		if (curPoint.getX()<endPoint.getX()){
    			curPoint.setX(curPoint.getX()+1);
    		}
    		if (curPoint.getY()<endPoint.getY()){
    			curPoint.setY(curPoint.getY()+1);
    		}
    		if (curPoint.getZ()<endPoint.getZ()){
    			curPoint.setZ(curPoint.getZ()+1);
    		}
    	}
    	
    	return collisionList;
    }
    
    public void move(Planetoid planet,Vector newPosition){
    	Vector oldPosition=getPosition(planet);
    	space.remove(oldPosition);
    	space.put(newPosition,planet);
    	index.put(planet, newPosition);
    }
    
    protected void fireEvent(iEvent<Planetoid> event){
    	for(iEventObserver<Planetoid> observer:observers){
            observer.processEvent(event);
    	}
    }
	
}

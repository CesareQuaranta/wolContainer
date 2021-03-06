/**
 * 
 */
package edu.wol.space;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.wol.dom.iEvent;
import edu.wol.dom.iEventObserver;
import edu.wol.dom.phisycs.Collision;
import edu.wol.dom.phisycs.ForceFactory;
import edu.wol.dom.shape.Shape;
import edu.wol.dom.space.BigVector;
import edu.wol.dom.space.Movement;
import edu.wol.dom.space.NewPosition;
import edu.wol.dom.space.Planetoid;
import edu.wol.dom.space.Position;
import edu.wol.dom.space.Space;
import edu.wol.dom.space.Vector3f;
import edu.wol.physics.starsystem.GravityAttraction;
import edu.wol.physics.starsystem.GravityField;
import edu.wol.starsystem.SolarSystem;

/**
 * @author cesare
 * Interstellar Space
 *
 */
//TODO Derivare da comune classe con campi gravitazionali
@Entity
public class Interstellar extends Space<SolarSystem,Position> {
   /**
	 * 
	 */
	private static final long serialVersionUID = 9186629741540928858L;
    private static final long spaceUnit=1L;
    @Transient
    private Map<Position,SolarSystem> space;
    @Transient
    private Map<Position,GravityField> gravityFields;
    @Transient
    private Map<SolarSystem,Position> index;
    @Transient
    private List<iEventObserver<SolarSystem>> observers;

    public Interstellar(){
            space=new HashMap<Position,SolarSystem>();//TODO da implementare hash map ottimizzata
            index=new HashMap<SolarSystem,Position>();
            gravityFields=new HashMap<Position,GravityField>();
            observers=new ArrayList<iEventObserver<SolarSystem>>();
    }

    public Collection<SolarSystem> getAllEntities(){
        return index.keySet();
    }

    public boolean insertEntity(Position position,SolarSystem ss){
    	boolean ok=false;
        if (!index.containsKey(ss)&&getEntity(position)==null){
        	boolean collision=false;
        	Iterator<SolarSystem> stars=index.keySet().iterator();
        	while(collision==false&&stars.hasNext()){
        		SolarSystem checkStarSystem=stars.next();
        		Position checkPosition=index.get(checkStarSystem);
        		double checkDistance=position.distance(checkPosition);
        		collision=checkDistance<(ss.getRadius()+checkStarSystem.getRadius());
        		if(collision){
        			System.err.println("Impossibile inserire "+ss+" alle coordinate:"+position+" collisione con "+checkStarSystem+" alle coordinate:"+checkPosition);
        		}
        	}
        	if(!collision){
        		space.put(position,ss);
        		index.put(ss,position);
        		if(ss.getMass()>0){
        			gravityFields.put(position, new GravityField(ss.getMass(),position));
        		}
        		
        		Collection<GravityField> GF=getEngagedGravityFields(position,ss.getMass());
        		Map<GravityField,BigVector> GfMap=new HashMap<GravityField,BigVector>(GF.size());
        		if(!GF.isEmpty()){
        			//Collection<Force> forces=new ArrayList<Force>(GF.size()-1);
	        		for(GravityField curGravityField:GF){
	        			BigVector distance=curGravityField.getCenter().distanceVector(position);
	        			GfMap.put(curGravityField, distance);
						//forces.add(curGravityField.getForce(planet, position));
	        		}
	        		fireEvent(new GravityAttraction(ss,GfMap));
	        		
					for(GravityField curGravityField:GF){//For all planet
						if(!curGravityField.getCenter().equals(position)){
							SolarSystem cSolarSystem=space.get(curGravityField.getCenter());
							Position curPlatetPosition=curGravityField.getCenter();
							//Collection<Force> forces2=new ArrayList<Force>(GF.size()-1);
							Collection<GravityField> GF2=getEngagedGravityFields(curPlatetPosition,cSolarSystem.getMass());
							Map<GravityField,BigVector> GfMap2=new HashMap<GravityField,BigVector>(GF2.size());
							for(GravityField curGravityField2:GF2){
								BigVector distance=curGravityField2.getCenter().distanceVector(curPlatetPosition);
			        			GfMap2.put(curGravityField2, distance);
								//forces2.add(curGravityField2.getForce(curPlanet, curPlatetPosition));
							}
							fireEvent(new GravityAttraction(cSolarSystem,GfMap2));
						}
					}
        		}
        		
        		ok=true;
        	}	
        }
        return ok;
    }

    private Collection<GravityField> getEngagedGravityFields(Position position,double mass) {
    	Collection<GravityField> engagedFields=new ArrayList<GravityField>();
		for(Position curGFPosition:gravityFields.keySet()){
			if(!curGFPosition.equals(position)){
				double curDistance=position.distance(curGFPosition);
				GravityField curGravityField=gravityFields.get(curGFPosition);
				double maxDistance=curGravityField.getRadius(mass);
				if(curDistance<maxDistance){
					engagedFields.add(curGravityField);
				}
			}
		}
		return engagedFields;
	}

	public SolarSystem getEntity(Position position) {
        return space.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Position getPosition(SolarSystem ss) {
        return index.get(ss);  //To change body of implemented methods use File | Settings | File Templates.
    }


    public boolean process(Movement<SolarSystem> movement) {
    	Position curPosition=index.get(movement.getEntity());
        Vector3f moveVector=movement.getVector();
        Position result=curPosition.clone();
        result.add(moveVector);
        	 List<SolarSystem> collisionList=null;//checkCollision(curPosition,result);
             if(collisionList==null){
             	move(movement.getEntity(),result);
             	//TODO fire event Move x ricalcolo forze
             	System.out.println("Pianeta "+movement.getEntity()+" mosso in "+result);
             	fireEvent(new NewPosition<SolarSystem>(movement.getEntity(),result));
             	return true;
             }else{
             	for(SolarSystem curSolarSystem:collisionList){
     				fireEvent(new Collision<SolarSystem>(movement.getEntity(),curSolarSystem));
     			}
             }
        return false;
       
    }


    public void addObserver(iEventObserver<SolarSystem> observer) {
        observers.add(observer);
    }


    public void processEvent(iEvent event) {
    	if (event instanceof Movement){
    		//internalProcessEvent((iSpacePhenomen<iPlanetoid>) event);
         }
    }
    
  /*  protected void internalProcessEvent(Event<iPlanetoid> event){
    	if(event instanceof Movement){
    		Movement<iPlanetoid> movement=(Movement<iPlanetoid>)event;
    		iPlanetoid planetoid=movement.getEntity();
    		Vector vector=movement.getVector();
    		Vector curPosition=getPosition(planetoid);
    		Vector newPosition=addVector(curPosition,vector);
    		List<iPlanetoid> collisionList=checkCollision(curPosition,newPosition);
    		if (collisionList!=null){
    			for(iPlanetoid curPlanet:collisionList){
    				fireEvent(new Collision<iPlanetoid>(planetoid,curPlanet));
    			}
    		}else{//Move
    			move(planetoid,newPosition);
    		}
    	}
    }*/

   
    
    private List<Planetoid> checkCollision(Position startPoint,Position endPoint){
    	List<Planetoid> collisionList=null;

    	SolarSystem startPlanet=getEntity(startPoint);
       /*Shape startPlanetShape=startPlanet.getShape();//TODO Check collision with shape
       Position curPoint=startPoint.clone();
       float curX=curPoint.getX();
       float curY=curPoint.getY();
       float curZ=curPoint.getZ();
       float difX=endPoint.getX()-startPoint.getX();
       float difY=endPoint.getY()-startPoint.getY();
       float difZ=endPoint.getZ()-startPoint.getZ(); 
       float difMax=Math.max(Math.max(difX,difY),difZ);
       float addX=difX/difMax*spaceUnit;
       float addY=difY/difMax*spaceUnit;
       float addZ=difZ/difMax*spaceUnit;
        double planetRadius=startPlanet.getRadius();
        while(!curPoint.equals(endPoint)){
        for(Planetoid curPlanet:index.keySet()){
            if(!curPlanet.equals(startPlanet)){
            	Position curVector=index.get(curPlanet);
            	double curRadius=curPlanet.getRadius();
               if(checkCollision(curPoint,planetRadius,curVector,curRadius)){
    			if (collisionList==null){
    				collisionList=new LinkedList<Planetoid>();
    			    }
    			collisionList.add(curPlanet);
    		    }
                }
             }
             curX+=addX;
             curY+=addY;
             curZ+=addZ;
             curPoint.setX((long)curX);
             curPoint.setY((long)curY);
             curPoint.setZ((long)curZ);
         }
        /*
    	Vector curPoint=startPoint.clone();
    	while(!curPoint.equals(endPoint)){
    		iPlanetoid curPlanet=getEntity(curPoint);
    		if (curPlanet!=null&&curPlanet!=startPlanet){
    			if (collisionList==null){
    				collisionList=new LinkedList<iPlanetoid>();
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
    	}     */
    	
    	return collisionList;
    }
    private boolean checkCollision(Position pointOne,double radiusOne,Position pointTwo,double radiusTwo){
        if(pointOne.getX()+radiusOne>pointTwo.getX()-radiusTwo&&pointOne.getX()-radiusOne<pointTwo.getX()+radiusTwo){
            //X collision
            if(pointOne.getY()+radiusOne>pointTwo.getY()-radiusTwo&&pointOne.getY()-radiusOne<pointTwo.getY()+radiusTwo){
                //Y collision
                if(pointOne.getZ()+radiusOne>pointTwo.getZ()-radiusTwo&&pointOne.getZ()-radiusOne<pointTwo.getZ()+radiusTwo){
                    //Z collision
                    //TODO accurate check collision
                    return true;
                }
            }
        }
        return false;
    }
    
    protected void move(SolarSystem planet,Position newPosition){
    	Position oldPosition=getPosition(planet);
    	space.remove(oldPosition);
    	space.put(newPosition,planet);
    	index.put(planet, newPosition);
    }
    
    protected void fireEvent(iEvent event){
    	for(iEventObserver<SolarSystem> observer:observers){
            observer.processEvent(event);
    	}
    }
/*
	@Override
	public Collection<Force> getAllForces(iPlanetoid entity) {
		ArrayList<Force> forces=new ArrayList<Force>();
		Position position=index.get(entity);
		Collection<GravityField> gravityFields=getEngagedGravityFields(position,entity.getMass());
		for(GravityField curGF:gravityFields){
			forces.add(curGF.getForce(entity, position));
		}
		return forces;
	}
*/
	

	@Override
	public boolean isEmpty() {
		return space.isEmpty();
	}
	
}

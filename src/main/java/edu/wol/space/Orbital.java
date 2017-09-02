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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import edu.wol.TimeQueque;
import edu.wol.dom.iEvent;
import edu.wol.dom.iEventObserver;
import edu.wol.dom.phisycs.Collision;
import edu.wol.dom.phisycs.ForceFactory;
import edu.wol.dom.phisycs.MassEntity;
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

/**
 * @author cesare
 * Spazio orbilate di un sistema solare
 * Tutti i punti sono definiti come orbite attorno ad un centro di gravità
 * TODO Aggiungere curvatura spazio da gravità
 * Indice dei corpi che si influnezano in base alla loro massa ed al raggio di hill
 */
@Entity
public class Orbital<E extends Planetoid> extends Inertial<E> {
   /**
	 * 
	 */
	private static final long serialVersionUID = 9186629741540928858L;
     
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private Map<String,GravityField> gravityFields;//<SerializedPosition,GravityField>
    
    protected Orbital(){
    	this(0,null);
    }
    
    public Orbital(float precision, TimeQueque<E> time){
    	super(precision, time);
        gravityFields=new HashMap<String,GravityField>();
    }

    @Override
    public boolean insertEntity(Position position,E planet){
    	boolean ok=super.insertEntity(position,planet);
    	if(ok && planet.getMass()>0){
    		String sPosition=position.serialize();
    		gravityFields.put(sPosition, new GravityField(planet.getMass(),position));
    		Collection<GravityField> GF=getEngagedGravityFields(position,planet.getMass());
    		Map<GravityField,BigVector> GfMap=new HashMap<GravityField,BigVector>(GF.size());
    		if(!GF.isEmpty()){
    			//Collection<Force> forces=new ArrayList<Force>(GF.size()-1);
        		for(GravityField curGravityField:GF){
        			BigVector distance=curGravityField.getCenter().distanceVector(position);
        			GfMap.put(curGravityField, distance);
					//forces.add(curGravityField.getForce(planet, position));
        		}
        		fireEvent(new GravityAttraction(planet,GfMap));
        		
				for(GravityField curGravityField:GF){//For all planet
					if(!curGravityField.getCenter().equals(position)){
						Planetoid curPlanet=(Planetoid) entitiesMap.get(curGravityField.getCenter().serialize());
						Position curPlatetPosition=curGravityField.getCenter();
						//Collection<Force> forces2=new ArrayList<Force>(GF.size()-1);
						Collection<GravityField> GF2=getEngagedGravityFields(curPlatetPosition,curPlanet.getMass());
						Map<GravityField,BigVector> GfMap2=new HashMap<GravityField,BigVector>(GF2.size());
						for(GravityField curGravityField2:GF2){
							BigVector distance=curGravityField2.getCenter().distanceVector(curPlatetPosition);
		        			GfMap2.put(curGravityField2, distance);
							//forces2.add(curGravityField2.getForce(curPlanet, curPlatetPosition));
						}
						fireEvent(new GravityAttraction(curPlanet,GfMap2));
					}
				}
    		}
    	}
       
        return ok;
    }

    private Collection<GravityField> getEngagedGravityFields(Position position,double mass) {
    	Collection<GravityField> engagedFields=new ArrayList<GravityField>();
		for(Position curGFPosition:posIndex.values()){
			if(!curGFPosition.equals(position)){
				double curDistance=position.distance(curGFPosition);
				GravityField curGravityField=gravityFields.get(curGFPosition.serialize());
				if(curGravityField!=null){
					double maxDistance=curGravityField.getRadius(mass);
					if(curDistance<maxDistance){
						engagedFields.add(curGravityField);
					}
				}
			}
		}
		return engagedFields;
	}

	
	public List<E> getEntities(Position position,long radius) {
		List<E> lEntities = new ArrayList<E>(0);
		for(Position curPosition:posIndex.values()){
			if(curPosition.distance(position)<radius){
				E p = (E) entitiesMap.get(curPosition.serialize());
				if(p != null){
					lEntities.add(p);
				}
			}
		}
        return lEntities;  
    }

/*
    public boolean process(Movement<Planetoid> movement) {
    	Position curPosition=posIndex.get(movement.getEntity().getID());
        Vector3f moveVector=movement.getVector();
        Position result=curPosition.clone();//TODO Da verificare se serve nuova istanza
        result.add(moveVector);
        
        	 List<Planetoid> collisionList=null;//checkCollision(curPosition,result);
             if(collisionList==null){
             	move(movement.getEntity(),result);
             	//TODO fire event Move x ricalcolo forze
             	System.out.println("Pianeta "+movement.getEntity()+" mosso in "+result);
             	fireEvent(new NewPosition<Planetoid>(movement.getEntity(),result));
             	return true;
             }else{
             	for(Planetoid curPlanet:collisionList){
     				fireEvent(new Collision<Planetoid>(movement.getEntity(),curPlanet));
     			}
             }
        return false;
       
    }
    */


   
    
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

   
    /*
    private List<Planetoid> checkCollision(Position startPoint,Position endPoint){
    	List<Planetoid> collisionList=null;

       Planetoid startPlanet=getEntity(startPoint);
       Shape startPlanetShape=startPlanet.getShape();//TODO Check collision with shape
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
        for(Planetoid curPlanet:planetsMap.values()){
            if(!curPlanet.equals(startPlanet)){
            	Position curVector=posIndex.get(curPlanet.getID());
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
    	/*
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
    
    protected void move(Planetoid planet,Position newPosition){
    	Position oldPosition=getPosition(planet);
    	planetsMap.remove(oldPosition.serialize());
    	planetsMap.put(newPosition.serialize(),planet);
    	posIndex.put(planet.getID(), newPosition);
    	//TODO Gravity field
    }
    */
  
/*
	@Override
	public Collection<Force> getAllForces(iPlanetoid entity) {
		ArrayList<Force> forces=new ArrayList<Force>();
		Position position=posIndex.get(entity);
		Collection<GravityField> gravityFields=getEngagedGravityFields(position,entity.getMass());
		for(GravityField curGF:gravityFields){
			forces.add(curGF.getForce(entity, position));
		}
		return forces;
	}
*/
	
}

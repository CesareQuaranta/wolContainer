/**
 * 
 */
package wol.starsystem.planets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.phisycs.Collision;
import wol.dom.phisycs.Force;
import wol.dom.phisycs.ForceFactory;
import wol.dom.shape.iShape;
import wol.dom.space.BigVector;
import wol.dom.space.Movement;
import wol.dom.space.NewPosition;
import wol.dom.space.Position;
import wol.dom.space.Vector;
import wol.dom.space.iSpace;
import wol.starsystem.physics.GravityAttraction;
import wol.starsystem.physics.GravityField;

/**
 * @author cesare
 *
 */
public class Cosmos implements iSpace<iPlanetoid,Position> {
   /**
	 * 
	 */
	private static final long serialVersionUID = 9186629741540928858L;
    private static final long spaceUnit=1L;

    private Map<Position,iPlanetoid> space;
    private Map<Position,GravityField> gravityFields;
    private Map<iPlanetoid,Position> index;
    private List<iEventObserver<iPlanetoid>> observers;

    public Cosmos(){
            space=new HashMap<Position,iPlanetoid>();//TODO da implementare hash map ottimizzata
            index=new HashMap<iPlanetoid,Position>();
            gravityFields=new HashMap<Position,GravityField>();
            observers=new ArrayList<iEventObserver<iPlanetoid>>();
    }

    public Collection<iPlanetoid> getAllEntities(){
        return index.keySet();
    }

    public boolean insertEntity(Position position,iPlanetoid planet){
    	boolean ok=false;
        if (!index.containsKey(planet)&&getEntity(position)==null){
        	boolean collision=false;
        	Iterator<iPlanetoid> planets=index.keySet().iterator();
        	while(collision==false&&planets.hasNext()){
        		iPlanetoid checkPlanet=planets.next();
        		Position checkPosition=index.get(checkPlanet);
        		double checkDistance=position.getDistance(checkPosition);
        		collision=checkDistance<(planet.getRadius()+checkPlanet.getRadius());
        		if(collision){
        			System.err.println("Impossibile inserire "+planet+" alle coordinate:"+position+" collisione con "+checkPlanet+" alle coordinate:"+checkPosition);
        		}
        	}
        	if(!collision){
        		space.put(position,planet);
        		index.put(planet,position);
        		if(planet.getMass()>0){
        			gravityFields.put(position, new GravityField(planet.getMass(),position));
        		}
        		
        		Collection<GravityField> GF=getEngagedGravityFields(position,planet.getMass());
        		Map<GravityField,BigVector> GfMap=new HashMap<GravityField,BigVector>(GF.size());
        		if(!GF.isEmpty()){
        			//Collection<Force> forces=new ArrayList<Force>(GF.size()-1);
	        		for(GravityField curGravityField:GF){
	        			BigVector distance=curGravityField.getCenter().getDistanceVector(position);
	        			GfMap.put(curGravityField, distance);
						//forces.add(curGravityField.getForce(planet, position));
	        		}
	        		fireEvent(new GravityAttraction(planet,GfMap));
	        		
					for(GravityField curGravityField:GF){//For all planet
						if(!curGravityField.getCenter().equals(position)){
							iPlanetoid curPlanet=space.get(curGravityField.getCenter());
							Position curPlatetPosition=curGravityField.getCenter();
							//Collection<Force> forces2=new ArrayList<Force>(GF.size()-1);
							Collection<GravityField> GF2=getEngagedGravityFields(curPlatetPosition,curPlanet.getMass());
							Map<GravityField,BigVector> GfMap2=new HashMap<GravityField,BigVector>(GF2.size());
							for(GravityField curGravityField2:GF2){
								BigVector distance=curGravityField2.getCenter().getDistanceVector(curPlatetPosition);
			        			GfMap2.put(curGravityField2, distance);
								//forces2.add(curGravityField2.getForce(curPlanet, curPlatetPosition));
							}
							fireEvent(new GravityAttraction(curPlanet,GfMap2));
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
				double curDistance=position.getDistance(curGFPosition);
				GravityField curGravityField=gravityFields.get(curGFPosition);
				double maxDistance=curGravityField.getRadius(mass);
				if(curDistance<maxDistance){
					engagedFields.add(curGravityField);
				}
			}
		}
		return engagedFields;
	}

	public iPlanetoid getEntity(Position position) {
        return space.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Position getPosition(iPlanetoid planetoid) {
        return index.get(planetoid);  //To change body of implemented methods use File | Settings | File Templates.
    }


    public boolean process(Movement<iPlanetoid> movement) {
    	Position curPosition=index.get(movement.getEntity());
        Vector moveVector=movement.getVector();
        Position result=curPosition.clone();
        result.sum(moveVector);
        	 List<iPlanetoid> collisionList=null;//checkCollision(curPosition,result);
             if(collisionList==null){
             	move(movement.getEntity(),result);
             	//TODO fire event Move x ricalcolo forze
             	System.out.println("Pianeta "+movement.getEntity()+" mosso in "+result);
             	fireEvent(new NewPosition<iPlanetoid>(movement.getEntity(),result));
             	return true;
             }else{
             	for(iPlanetoid curPlanet:collisionList){
     				fireEvent(new Collision<iPlanetoid>(movement.getEntity(),curPlanet));
     			}
             }
        return false;
       
    }


    public void addObserver(iEventObserver<iPlanetoid> observer) {
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

   
    
    private List<iPlanetoid> checkCollision(Position startPoint,Position endPoint){
    	List<iPlanetoid> collisionList=null;

       iPlanetoid startPlanet=getEntity(startPoint);
       iShape startPlanetShape=startPlanet.getShape();//TODO Check collision with shape
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
        for(iPlanetoid curPlanet:index.keySet()){
            if(!curPlanet.equals(startPlanet)){
            	Position curVector=index.get(curPlanet);
            	double curRadius=curPlanet.getRadius();
               if(checkCollision(curPoint,planetRadius,curVector,curRadius)){
    			if (collisionList==null){
    				collisionList=new LinkedList<iPlanetoid>();
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
    
    protected void move(iPlanetoid planet,Position newPosition){
    	Position oldPosition=getPosition(planet);
    	space.remove(oldPosition);
    	space.put(newPosition,planet);
    	index.put(planet, newPosition);
    }
    
    protected void fireEvent(iEvent event){
    	for(iEventObserver<iPlanetoid> observer:observers){
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
	public void insertForceFactory(ForceFactory f) {
		// TODO Auto-generated method stub
		
	}
	
}

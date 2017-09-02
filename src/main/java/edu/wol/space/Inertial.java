/**
 * 
 */
package edu.wol.space;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import edu.wol.TimeQueque;
import edu.wol.dom.WolEntity;
import edu.wol.dom.phisycs.Acceleration;
import edu.wol.dom.phisycs.Collision;
import edu.wol.dom.phisycs.MassEntity;
import edu.wol.dom.phisycs.Velocity;
import edu.wol.dom.space.Position;
import edu.wol.dom.space.Vector3d;
import edu.wol.dom.space.Vector3f;
import edu.wol.dom.time.Ichinen;

/**
 * @author cesare
 * Spazio statico
 * Tutti i punti sono indefinitamente statici
 */
@Entity
public class Inertial<E extends MassEntity> extends Static<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 220999456142984840L;
	@OneToOne(cascade=CascadeType.ALL)
    protected TimeQueque<MassEntity> time;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    protected Map<Long, InertialData> inertialIndex;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	protected List<FrictionField> fractionFields;
	
	private static final double frictionConst= 0.0000001;//TODO sostituire con frictionfileds
	
	protected Inertial(){
		this(0,null);
	}
    public Inertial(float precision, TimeQueque<E> time){
    		super(precision);
    		this.time = (TimeQueque<MassEntity>) time;
    		inertialIndex=new HashMap<Long,InertialData>();
    		fractionFields = new ArrayList<FrictionField>();
    }
    
    @Override
    public boolean insertEntity(Position position,E entity){
    	return insertEntity(position, entity, new Velocity(0,0,0,0),null);
    }

    public boolean insertEntity(Position position,E entity, Velocity velocity,Acceleration accelertation){
    	boolean ok = super.insertEntity(position, entity);
    	if(ok){
    		inertialIndex.put(entity.getID(), new InertialData(entity,velocity,accelertation,time.getCurTime()));
    		for(WolEntity curEntity : entitiesMap.values()){
    			if(!curEntity.equals(entity)){
    				long cFuture = checkCollision(entity,(E) curEntity);
    				if(cFuture>-1){
    					Ichinen<MassEntity> collisionIchinen=new Ichinen<MassEntity>();
    					collisionIchinen.setAction(velocity);
    					Collision<MassEntity> latentEffect=new Collision<MassEntity>();
    					latentEffect.setObject(entity);
    					latentEffect.setSubject((MassEntity) curEntity);
    					collisionIchinen.setEffect(latentEffect);
    					this.time.addFuture(collisionIchinen, cFuture);
    				}
    			}
    		}
    		
    		//TODO Check for collision
    	}
        return ok;
    }
    public void addVelocity(E entity, Vector3f velocity){
    	InertialData data = inertialIndex.get(entity.getID());
    	if(data != null){
    		if(data.velocity == null){
    			data.velocity = new Velocity(1);
    		}
    		data.velocity.getVector().add(velocity);
    	}
    }
    
    public void addAcceleration(E entity, Acceleration acceleration){
    	InertialData data = inertialIndex.get(entity.getID());
    	if(data != null){
    		if(data.accelertation == null){
    			data.accelertation = acceleration;
    		}else
    			data.accelertation.getVector().add(acceleration.getVector());
    	}
    }
    
    @Override
    public Position getPosition(E entity) {
    	InertialData data=inertialIndex.get(entity.getID());
    	if(data!=null && data.time<time.getCurTime()){
    		long diff = time.getCurTime() - data.time;
    		updateData(data, diff);
    	}
    	return super.getPosition(entity);
     }
    
    
    //TO OVERRIDE public E getEntity(Position position) {
    
    public Velocity getVelocity(E entity) {
		if(entity!=null && !entity.isNew() && inertialIndex.containsKey(entity.getID())){
			return inertialIndex.get(entity.getID()).velocity;
		}else{
			return null;
		}
	}
    
    public void addFrictionField(FrictionField field){
    	this.fractionFields.add(field);
    }
    
    private long checkCollision(E e1, E e2){
    	long collisionFuture = -1;
    	Position pos1 = posIndex.get(e1.getID());
    	InertialData data1=inertialIndex.get(e1.getID());
    	
    	Position pos2 = posIndex.get(e2.getID());
    	InertialData data2=inertialIndex.get(e2.getID());
    	
    	
    	//Determinazione delle superfici d'impatto
    	Vector3f versore1 = new Vector3f();
    	versore1.sub(pos2, pos1);
    	versore1.normalize();
    	versore1.scale(e1.getRadius());
    	
    	Vector3d surface1 = new Vector3d((Vector3f)pos1);
    	surface1.add(versore1);
    	
    	Vector3f versore2 = new Vector3f();
    	versore2.sub(pos1, pos2);
    	versore2.normalize();
    	versore2.scale(e2.getRadius());
    	
    	Vector3d surface2 = new Vector3d((Vector3f)pos2);
    	surface2.add(versore2);
    	
    	//Determinazione del discriminante (b^2 -4ac)
    	//Determinazione di a = ((v2*f2/2) - (v1*f1/2))
    	Vector3d a = new Vector3d();
    	Vector3d a2 = new Vector3d(data2.velocity.getVector());
    	a2.scale(frictionConst/2);
    	Vector3d a1 = new Vector3d(data1.velocity.getVector());
    	a1.scale(frictionConst/2);
    	a.sub(a1, a2);
    	//2a
    	a2 = a.clone();
    	a2.scale(2);
    	
    	// Detrminazione di b = v1 - v2
    	Vector3d b = new Vector3d(data1.velocity.getVector());
    	b.sub(new Vector3d(data2.velocity.getVector()));
    	//b^2
    	Vector3d bq = b.clone();
    	bq.multiply(b);
    	
    	//Determinazione c = surface1 - surface2
    	Vector3d c = new Vector3d();
    	c.sub(surface1, surface2);
    	
    	//4ac
    	Vector3d ac4=a.clone();
    	ac4.multiply(c);
    	ac4.scale(4);
    	
    	//Discriminante
    	Vector3d d = new Vector3d();
    	d.sub(bq,ac4);
    	
    	if(d.length() == 0 ){
    		//TODO -b/2a
    	}else{//(sqr(d)-b)/2a
    		Vector3d div=new Vector3d();
    		d.sqrt();
    		//div.sub(d, b);
    		div.add(d);
    		div.add(b);
    		double divX=div.x;
    		double divY=div.y;
    		double divZ=div.z;
    		
    		if(a2.x!=0){
    			divX = divX / a2.x;
    		}
    		if(a2.y!=0){
    			divY = divY / a2.y;
    		}
    		if(a2.z!=0){
    			divZ = divZ / a2.z;
    		}
    		
    		collisionFuture = (long) Math.sqrt(divX*divX + divY*divY +divZ+divZ);
    	}
    	return collisionFuture;
    }
    
    private void updateData(InertialData data, long time){
    	if((data.velocity != null && !data.velocity.isEmpty()) || (data.accelertation !=null &&  !data.accelertation.isEmpty())){
        	/*
        	 *  Update the positio with newtonian equation s(t) = s0 + v0*t + 1/2 * a * t * t
        	 */
        	double seconds = ((double)time)/this.time.getPrecision();
        	Position actualPosition = this.posIndex.get(data.entity.getID());
        	Vector3f velocityVector = data.velocity.getVector().clone();
        	if(velocityVector!=null && !velocityVector.isEmpty()){
        		velocityVector.scale(seconds);
            	
            	//Update Position
            	actualPosition.add(velocityVector);
        	}
        	
        	
        	
        	if(data.accelertation!=null && !data.accelertation.isEmpty()){
        		Vector3f accelerationVector = data.accelertation.getVector().clone();
        		accelerationVector.scale(seconds*seconds);
            	accelerationVector.scale(0.5);
            	actualPosition.add(accelerationVector);
            	
            	//Update Velocity
            	accelerationVector =data.accelertation.getVector().clone();
            	accelerationVector.scale(seconds);
            	data.velocity.getVector().add(accelerationVector);
        	}
        	
        	if(velocityVector!=null && !velocityVector.isEmpty()){
	        	//TODO calcolare frictionfields
	        	Vector3f frictionVector = data.velocity.getVector().clone();
	        	frictionVector.negate();
	        	frictionVector.scale(frictionConst);
	        	data.velocity.getVector().add(frictionVector);
        	}
        	data.time = this.time.getCurTime();
    	}

    }
    
    @Entity
    private static class InertialData{
    	@Id
    	@GeneratedValue
    	private long ID;
    	
    	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    	private MassEntity entity;
    	
    	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    	private Velocity velocity;
    	
    	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    	private Acceleration accelertation;
    	
    	private Long time;
    	
    	private InertialData(){
    		this(null,null,null,0L);
    	}
    	
		private InertialData(MassEntity entity, Velocity velocity, Acceleration accelertation,
				Long time) {
			this.entity = entity;
			this.velocity = velocity;
			this.accelertation = accelertation;
			this.time = time;
		}
    	
    }
	
}

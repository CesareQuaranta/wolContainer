package edu.wol.physics.starsystem;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import edu.wol.TimeQueque;
import edu.wol.dom.Effect;
import edu.wol.dom.Power;
import edu.wol.dom.iEvent;
import edu.wol.dom.phisycs.Acceleration;
import edu.wol.dom.phisycs.Collision;
import edu.wol.dom.phisycs.Force;
import edu.wol.dom.phisycs.Forces;
import edu.wol.dom.phisycs.Inertia;
import edu.wol.dom.phisycs.Velocity;
import edu.wol.dom.space.Movement;
import edu.wol.dom.space.Planetoid;
import edu.wol.dom.space.Position;
import edu.wol.dom.time.Ichinen;
import edu.wol.dom.time.ManifestPresent;
import edu.wol.physics.BasePhisycs;
import edu.wol.space.Orbital;

/**
 * Created by IntelliJ IDEA. User: cesare Date: 06/10/11 Time: 0.12 To change
 * this template use File | Settings | File Templates.
 */
@Entity
public class SolarSystemPhisycs extends BasePhisycs<Planetoid,Orbital> {
	private static final long serialVersionUID = -7499754647514879204L;
	public static final int LIGHT_VELOCITY = (int) 3e7;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<Planetoid> planets;
	@Transient
	private Map<GravityField,Force> gravityFieldsIndex;

	public SolarSystemPhisycs(){
		this.planets = new HashSet<Planetoid>();
		this.gravityFieldsIndex = new HashMap<GravityField,Force>();
		this.activeForces = new HashMap<Force,Ichinen<Planetoid>>();
		this.forcesIndex = new HashMap<Planetoid,Collection<Force>>(); 
	    this.heap = new LinkedList<Planetoid>();
	    this.ichinens = new HashMap<Planetoid, Ichinen<Planetoid>>();
	    this.velocityIndex = new HashMap<Planetoid, Velocity>();
	}
	public SolarSystemPhisycs(Orbital space, TimeQueque<Planetoid> time) {
		this(space,time,1, 1);
	}

	public SolarSystemPhisycs(Orbital space, TimeQueque<Planetoid> time,float spacePrecision, float maxVelocity) {
		//Super class initialize
		this();
		this.space=space;
		this.time=time;
		this.spacePrecision = spacePrecision;
		this.maxVelocity=maxVelocity;
		this.timePrecision = spacePrecision/maxVelocity;
		for(Planetoid curPlanet:space.getAllEntities()){
			if(!planets.contains(curPlanet)){
				initialize(curPlanet);
				
			}
		}
	}

	public void run() {//Time run -> process event & super.run
		time.run();
		super.run();
	}

	public void insert(Planetoid planet,Position coordinate) {
		if(space.insertEntity((Position)coordinate, planet)){
			initialize(planet);
		}
	}
	
	private void initialize(Planetoid planet) {
		planets.add(planet);
		velocityIndex.put(planet, new Velocity(1));
	}
	  @Override
		public void processEvent(iEvent event) {
		  if(event instanceof ManifestPresent){
			  processPresent(((ManifestPresent<Planetoid>)event).getPresent());
		  }else if(event instanceof GravityAttraction){
	    		GravityAttraction GA=(GravityAttraction)event;
	    		for(GravityField curGravityField:GA.getGravityFields().keySet()){
	    			Force curAttractionForce=curGravityField.getForce(GA.getEntity(), GA.getGravityFields().get(curGravityField));
	    			gravityFieldsIndex.put(curGravityField, curAttractionForce);
	    			applyForce((Planetoid) GA.getEntity(),curAttractionForce);
	    		}
	    		//insertAccellerationIchinen(GA.getEntity(),forces);
	    	}else if(event instanceof Collision){
	    		System.out.println("Collision non gestita");
	    		System.out.println(event.toString());
	    	}
			//if (phenomen instanceof Acceleration
			//		&& !((Acceleration<iPlanetoid>) phenomen).isLatent()) {
				/*Acceleration acceleration = (Acceleration) event;
				Planetoid planet = acceleration.;
				Vector curVelocity = this.velocity.get(planet);
				Set<Effect<Entity>> latents = planet
						.getLatentEffects(Movement.class);
				curVelocity.sum(acceleration.getVector());
				long future = 1;
				if (curVelocity.getLenght() < 1) {
					// recalculate vector& future
				}
				Movement<Planetoid> movement = new Movement<Planetoid>(planet,
						future, curVelocity);
				if (latents.isEmpty()) {
					planet.addLatentEffect((Effect) movement);
					fireEvent(movement);
				} else {// TODO Karma transformation, Remove phenomen, recalculate,
						// insert

				}   */
			}
	
	private void processPresent(List<Ichinen<Planetoid>> present) {
		for(Ichinen<Planetoid> curIchinen:present){
			Power power=curIchinen.getPower();
			Effect<Planetoid> curEffect=curIchinen.getEffect();
			Planetoid entity=curIchinen.getEntity();
			if(power instanceof Acceleration){
				Velocity curVelocity=velocityIndex.get(entity);
				Velocity newVelocity=(Velocity)curIchinen.getAction();
				velocityIndex.put(entity, newVelocity);

				
				if(curEffect instanceof Movement){
					space.process((Movement<Planetoid>)curEffect);
				}
				Forces<Planetoid> extrenalCause=(Forces<Planetoid>) curIchinen.getExternalCause();
				insertAccellerationIchinen(curIchinen.getEntity(), extrenalCause.getForces());//TODO Ottimizzazzione inserire lo stesso ichinen non ricalcolarlo ogni volta
			}
			else if(power instanceof Inertia){
				if(curEffect instanceof Movement){
					space.process((Movement<Planetoid>)curEffect);
				}
				insertInertiaIchinen(curIchinen.getEntity());//TODO Ottimizzazzione inserire lo stesso ichinen non ricalcolarlo ogni volta
			}
			
		}
	}
	public Collection<Planetoid> getPlanets() {
		return planets;
	}
	
	
/*
	public Force calculateAttraction(iPlanetoid subject, iPlanetoid object) {

		Position objectPosition = space.getPosition(object);
		Position subjectPosition = space.getPosition(subject);

		double radiusGravity = subjectPosition.getDistance(objectPosition);
		double f = (COSTANTE_GRAVITAZIONALE * object.getMass() * subject
				.getMass()) / (radiusGravity * radiusGravity); // F=GMm/r^2

		double difX = (objectPosition.getX() - subjectPosition.getX())
				/ radiusGravity;
		double difY = (objectPosition.getY() - subjectPosition.getY())
				/ radiusGravity;
		double difZ = (objectPosition.getZ() - subjectPosition.getZ())
				/ radiusGravity;
		Vector forceVector = new Vector(Math.round(difX * f), Math.round(difY * f), Math.round(difZ * f));
		Force force = new Force(f, (Acceleration) forceVector);
		return force;
		/*
		 * if(isFixed || gravityIndex.contains(planet.hashCode()) ||
		 * planet.getGravityIndex().contains(this.hashCode())) { return; }
		 * double radiusGravity = this.getCenter().distance(planet.getCenter());
		 * double force = (Constants.G * this.mass *
		 * planet.mass)/(radiusGravity*radiusGravity); // F=GMm/r^2 double
		 * frameRateMs = (Constants.FrameRate/1000.0); double gravityAngle =
		 * Math.atan2(Math.max(location.y,planet.location.y) -
		 * Math.min(location.y,planet.location.y),
		 * Math.max(location.x,planet.location.x) -
		 * Math.min(location.x,planet.location.x)) + (Math.PI/2); double
		 * gravityDistance =
		 * ((force*frameRateMs)/this.mass)*Constants.SpriteGravityMultiplier;
		 * if(gravityAngle < 0){ // keep angle positive gravityAngle +=
		 * MaxAngle; } //
		 * System.out.println("--"+name+"<->"+planet.getName()+"-"
		 * +gravityDistance+"px @ "+Math.round(gravityAngle)+"rad");
		 * if(planet.IsFixed()) { // gravity acts only on this body, pulling it
		 * towards the planet gravityCache.add(new Vector2D(gravityAngle,
		 * gravityDistance)); gravityIndex.add(planet.hashCode()); } else { //
		 * gravity acts on both, pulling them toward each other double
		 * resultantGravAngle = (gravityAngle/2), resultantGravDistance =
		 * (gravityDistance /2); gravityCache.add(new
		 * Vector2D(resultantGravAngle, resultantGravDistance));
		 * gravityIndex.add(planet.hashCode()); planet.getGravityCache().add(new
		 * Vector2D(resultantGravAngle, resultantGravDistance));
		 * planet.getGravityIndex().add(planet.hashCode()); }
		 * 
		 * 
		 * gravityAngle = gravityAngle % MaxAngle;
		 *
	}*/
/*
	protected void generateAcceleration(iPlanetoid planet) {
		//Set<iPower> attractions = planet.getPowers();
        Set<Force> forces=new HashSet<Force>();
		/*for (iPower curPower : attractions) {
			if (curPower instanceof Force) {
                forces.add((Force) curPower);
			}
		}
        Acceleration acceleration = calcAcceleration(planet,forces);
		// Seed<Planetoid> seed = new Seed<Planetoid>(planet);
        //planet.getKarma().add(new Attraction(planet),acceleration);
		//fireEvent(acceleration);
	}*/

  

	
	}

	/*
	 * protected List<Seed<Planetoid>> generateSeeds(Planetoid planet) {
	 * List<Seed<Planetoid>> seeds = new LinkedList<Seed<Planetoid>>();
	 * ArrayList<Planetoid> lPlanets = new ArrayList<Planetoid>(
	 * planets.getPlanets()); lPlanets.remove(planet); if (lPlanets != null &&
	 * !lPlanets.isEmpty()) { // Generate Inner Couse for (Planetoid curPlanet :
	 * lPlanets) { Seed<Planetoid> entitySeed = new Seed<Planetoid>(planet);
	 * Attraction entityAttraction = new Attraction(curPlanet);
	 * entitySeed.setInternalCause(entityAttraction); seeds.add(entitySeed);
	 * Seed<Planetoid> curSeed = new Seed<Planetoid>(curPlanet); Attraction
	 * curAttraction = new Attraction(planet);
	 * curSeed.setInternalCause(curAttraction); seeds.add(curSeed); } } return
	 * seeds; }
	 * 
	 * protected void procesSeeds(List<Seed<Planetoid>> seeds) { // Generate
	 * Effect for (Seed<Planetoid> curSeed : seeds) { Planetoid subject =
	 * curSeed.getEntity();
	 * 
	 * Planetoid object = (Planetoid) ((Attraction) curSeed
	 * .getInternalCause()).getMagnet();
	 * 
	 * Vector objectPosition = planets.getPosition(object); Vector
	 * subjectPosition = planets.getPosition(subject);
	 * 
	 * /* TODO DA ottimizzare double mass = (subject).getMass() *
	 * (object).getMass(); double distance =
	 * subjectPosition.getDistance(objectPosition);
	 * 
	 * double distanceX = subjectPosition.getX() - objectPosition.getX(); double
	 * distanceY = subjectPosition.getY() - objectPosition.getY(); double
	 * distanceZ = subjectPosition.getZ() - objectPosition.getZ(); double
	 * minDistance = 0; if (distanceX != 0) { minDistance = Math.abs(distanceX);
	 * } if (distanceY != 0) { minDistance = Math.min(minDistance,
	 * Math.abs(distanceY)); } if (distanceZ != 0) { minDistance =
	 * Math.min(minDistance, Math.abs(distanceZ)); }
	 * 
	 * double force = (mass * COSTANTE_GRAVITAZIONALE) / (Math.pow(distance,
	 * 2));
	 * 
	 * Vector vettore = new Vector(distanceX / minDistance, distanceY /
	 * minDistance, distanceZ / minDistance); Movement<Planetoid> movement = new
	 * Movement<Planetoid>(object, vettore); curSeed.setLatentEffect(movement);
	 * fireEvent(new TimeEvent<Planetoid>(curSeed, (int)
	 * Math.floor(Integer.MAX_VALUE - force))); } }
	 *
}     */

package wol.dom.phisycs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wol.dom.Entity;
import wol.dom.LatentEffect;
import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.iPower;
import wol.dom.space.Movement;
import wol.dom.space.Vector;
import wol.dom.space.planets.PlanetSystem;
import wol.dom.space.planets.Planetoid;

/**
 * Created by IntelliJ IDEA. User: cesare Date: 06/10/11 Time: 0.12 To change
 * this template use File | Settings | File Templates.
 */
public class SolarSystemPhisycs implements iPhisycs<Planetoid> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7499754647514879204L;
	private PlanetSystem planets;
	private List<iEventObserver<Planetoid>> observers = new ArrayList<iEventObserver<Planetoid>>();
	private static final float COSTANTE_GRAVITAZIONALE = 6.67e-11f;
	private static final int LIGHT_VELOCITY = (int) 3e7;
	private Map<Planetoid, Vector> velocity = new HashMap<Planetoid, Vector>();
	private Collection<GravitationalField> gravitySpheres = new LinkedList<GravitationalField>();
	private float spacePrecision;
	private float timePrecision;

	public SolarSystemPhisycs() {
		this(1, 1);
	}

	public SolarSystemPhisycs(float spacePrecision, float timePrecision) {
		super();
		this.spacePrecision = spacePrecision;
		this.timePrecision = timePrecision;
		gravitySpheres.add(new GravitationalField());// Default Gravity Center
	}

	public void run() {
	}

	public void insert(Planetoid planet) {
		velocity.put(planet, new Vector(0.0d, 0.0d, 0.0d));
		Vector planetPosition = planets.getPosition(planet);
		for (GravitationalField gravitySphere : gravitySpheres) {
			if (gravitySphere.contains(planetPosition)) {
				// TODO calculate attraction
			}
		}
		// TODO calculate and insert gravitysphere

		ArrayList<Planetoid> lPlanets = new ArrayList<Planetoid>(
				planets.getPlanets());
		lPlanets.remove(planet);
		if (lPlanets != null && !lPlanets.isEmpty()) {
			for (Planetoid curPlanet : lPlanets) {
				Force curAttraction = calculateAttraction(planet, curPlanet);
				planet.addPower(curAttraction);
			}
			generateAcceleration(planet);

		}
		// List<Seed<Planetoid>> seeds = generateSeeds(planet);
		// procesSeeds(seeds);
	}

	public Force calculateAttraction(Planetoid subject, Planetoid object) {

		Vector objectPosition = planets.getPosition(object);
		Vector subjectPosition = planets.getPosition(subject);

		double radiusGravity = subjectPosition.getDistance(objectPosition);
		double f = (COSTANTE_GRAVITAZIONALE * object.getMass() * subject
				.getMass()) / (radiusGravity * radiusGravity); // F=GMm/r^2

		double difX = (objectPosition.getX() - subjectPosition.getX())
				/ radiusGravity;
		double difY = (objectPosition.getY() - subjectPosition.getY())
				/ radiusGravity;
		double difZ = (objectPosition.getZ() - subjectPosition.getZ())
				/ radiusGravity;
		Vector forceVector = new Vector(difX * f, difY * f, difZ * f);
		Force force = new Force(f, forceVector);
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
		 */
	}

	protected void generateAcceleration(Planetoid planet) {
		Set<iPower> attractions = planet.getPowers();
		double a = 0;
		Vector accelerationVector = new Vector();
		for (iPower curPower : attractions) {
			if (curPower instanceof Force) {
				Force curAttraction = (Force) curPower;
				Vector forceVector = curAttraction.getVector().multiply(
						1 / curAttraction.getIntensity());
				a = curAttraction.getIntensity() / planet.getMass();
				accelerationVector.sum(forceVector.multiply(a));
			}

		}

		Acceleration<Planetoid> acceleration;
		if (a > 1) {
			acceleration = new Acceleration<Planetoid>(planet, 1,
					accelerationVector);
		} else {
			double dFuture = 1 / a;
			long future = (long) Math.floor(dFuture);
			double diff = dFuture - future;
			Vector newAccelerationVector = accelerationVector.multiply(1 / a);
			acceleration = new Acceleration<Planetoid>(planet, future,
					newAccelerationVector);
		}

		// Seed<Planetoid> seed = new Seed<Planetoid>(planet);
		planet.addLatentEffect((Acceleration) acceleration);
		fireEvent(acceleration);
	}

	protected void fireEvent(iEvent<Planetoid> event) {
		for (iEventObserver<Planetoid> observer : observers) {
			observer.processEvent(event);
		}
	}

	public void addObserver(iEventObserver<Planetoid> observer) {
		observers.add(observer);
	}

	public PlanetSystem getPlanets() {
		return planets;
	}

	public void setPlanets(PlanetSystem planets) {
		this.planets = planets;
	}

	public void processEvent(iEvent<Planetoid> event) {
		if (event instanceof Acceleration
				&& ((Acceleration<Planetoid>) event).getDelay() == 0) {
			Acceleration<Planetoid> acceleration = (Acceleration<Planetoid>) event;
			Planetoid planet = acceleration.getEntity();
			Vector curVelocity = this.velocity.get(planet);
			Set<LatentEffect<Entity>> latents = planet
					.getLatentEffects(Movement.class);
			curVelocity.sum(acceleration.getVector());
			long future = 1;
			if (curVelocity.getLenght() < 1) {
				// recalculate vector& future
			}
			Movement<Planetoid> movement = new Movement<Planetoid>(planet,
					future, curVelocity);
			if (latents.isEmpty()) {
				planet.addLatentEffect((LatentEffect) movement);
				fireEvent(movement);
			} else {// TODO Karma transformation, Remove event, recalculate,
					// insert

			}
		}
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
	 * LatentEffect for (Seed<Planetoid> curSeed : seeds) { Planetoid subject =
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
	 */
}

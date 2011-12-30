package wol.dom.phisycs;

import wol.dom.Entity;
import wol.dom.Seed;
import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.space.Vector;
import wol.dom.space.Movement;
import wol.dom.space.planets.PlanetSystem;
import wol.dom.space.planets.Planetoid;
import wol.dom.time.TimeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: cesare Date: 06/10/11 Time: 0.12 To change
 * this template use File | Settings | File Templates.
 */
public class UniversalGravityPhisycs implements iPhisycs<Planetoid> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7499754647514879204L;
	private PlanetSystem planets;
	private List<iEventObserver<Planetoid>> observers = new ArrayList<iEventObserver<Planetoid>>();
	private static final float COSTANTE_GRAVITAZIONALE = (float) 6.667;

	public void run() {
		List<Seed<Planetoid>> seeds = new LinkedList<Seed<Planetoid>>();
		for (Planetoid planet:planets.getPlanets()){
			ArrayList<Planetoid> lPlanets=new ArrayList<Planetoid>(planets.getPlanets());
			lPlanets.remove(planet);
			if (lPlanets != null&&!lPlanets.isEmpty()) {
				
				// Generate Inner Couse
				for (Planetoid curPlanet :lPlanets ) {
					Seed<Planetoid> entitySeed = new Seed<Planetoid>(planet);
					Attraction entityAttraction = new Attraction(curPlanet);
					entitySeed.setInternalCause(entityAttraction);
					seeds.add(entitySeed);
					Seed<Planetoid> curSeed = new Seed<Planetoid>(curPlanet);
					Attraction curAttraction = new Attraction(planet);
					curSeed.setInternalCause(curAttraction);
					seeds.add(curSeed);
				}
			}
		}
		procesSeeds(seeds);
		
	}
	
	protected void procesSeeds(List<Seed<Planetoid>> seeds){
		// Generate LatentEffect
					for (Seed<Planetoid> curSeed : seeds) {
						Planetoid subject = curSeed.getEntity();

						Planetoid object = (Planetoid) ((Attraction) curSeed
								.getInternalCause()).getMagnet();
						
						Vector objectPosition = planets.getPosition(object);
						Vector subjectPosition = planets.getPosition(subject);

						/* TODO DA ottimizzare */
						double mass = (subject).getMass() * (object).getMass();
						double distance = subjectPosition.getDistance(objectPosition);

						double distanceX = subjectPosition.getX()
								- objectPosition.getX();
						double distanceY = subjectPosition.getY()
								- objectPosition.getY();
						double distanceZ = subjectPosition.getZ()
								- objectPosition.getZ();
						double minDistance = 0;
						if (distanceX != 0) {
							minDistance = Math.abs(distanceX);
						}
						if (distanceY != 0) {
							minDistance = Math.min(minDistance, Math.abs(distanceY));
						}
						if (distanceZ != 0) {
							minDistance = Math.min(minDistance, Math.abs(distanceZ));
						}

						double force = (mass * COSTANTE_GRAVITAZIONALE)
								/ (Math.pow(distance, 2));

						Vector vettore = new Vector( distanceX
								/ minDistance, distanceY
								/ minDistance, distanceZ
								/ minDistance);
						Movement<Planetoid> movement = new Movement<Planetoid>(object,
								vettore);
						curSeed.setLatentEffect(movement);
						fireEvent(new TimeEvent<Planetoid>(curSeed,
								(int) Math.floor(Integer.MAX_VALUE - force)));
					}
	}

	public void insert(Planetoid entity) {
		
	}
	public void calculateGravity(Planetoid planet) {
		/*if(isFixed || gravityIndex.contains(planet.hashCode()) || planet.getGravityIndex().contains(this.hashCode())) {
		    return;
		}
		double radiusGravity = this.getCenter().distance(planet.getCenter());
		double force = (Constants.G * this.mass * planet.mass)/(radiusGravity*radiusGravity); // F=GMm/r^2
		double frameRateMs = (Constants.FrameRate/1000.0);
		double gravityAngle = Math.atan2(Math.max(location.y,planet.location.y) - Math.min(location.y,planet.location.y),
		                                 Math.max(location.x,planet.location.x) - Math.min(location.x,planet.location.x)) + (Math.PI/2);
		double gravityDistance = ((force*frameRateMs)/this.mass)*Constants.SpriteGravityMultiplier;
		if(gravityAngle < 0){ // keep angle positive
		    gravityAngle += MaxAngle;
		}
		//        System.out.println("--"+name+"<->"+planet.getName()+"-"+gravityDistance+"px @ "+Math.round(gravityAngle)+"rad");
		if(planet.IsFixed()) { // gravity acts only on this body, pulling it towards the planet
		    gravityCache.add(new Vector2D(gravityAngle, gravityDistance));
		    gravityIndex.add(planet.hashCode());
		} else { // gravity acts on both, pulling them toward each other
		    double resultantGravAngle = (gravityAngle/2),
		           resultantGravDistance = (gravityDistance /2);
		    gravityCache.add(new Vector2D(resultantGravAngle, resultantGravDistance));
		    gravityIndex.add(planet.hashCode());
		    planet.getGravityCache().add(new Vector2D(resultantGravAngle, resultantGravDistance));
		    planet.getGravityIndex().add(planet.hashCode());
		}
		
		*
		*gravityAngle = gravityAngle % MaxAngle;
		**/
	}

	protected void fireEvent(iEvent<Planetoid> event) {
		for (iEventObserver<Planetoid> observer : observers) {
			observer.processEvent(event);
		}
	}

	public void addObserver(iEventObserver observer) {
		observers.add(observer);
	}

	public void processSeeds(List<Seed<Planetoid>> seeds) {
		// Causa esterna effetto manifesto

	}

	public PlanetSystem getPlanets() {
		return planets;
	}

	public void setPlanets(PlanetSystem planets) {
		this.planets = planets;
	}

	public void processEvent(iEvent<Planetoid> event) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}
}

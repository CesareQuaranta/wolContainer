package wol;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;
import wol.dom.phisycs.Acceleration;
import wol.dom.phisycs.Force;
import wol.dom.space.BigVector;
import wol.dom.space.Position;
import wol.dom.time.TimeQueque;
import wol.starsystem.SolarSystemPhisycs;
import wol.starsystem.StarsContainer;
import wol.starsystem.planets.Cosmos;
import wol.starsystem.planets.LivingPlanet;
import wol.starsystem.planets.Planet;
import wol.starsystem.planets.Star;
import wol.starsystem.planets.iPlanetoid;

public class FunctionalTest {

	private static final double EatrhRadius=6.37101e6D;
	private static final double EarthMass=5.9736e24D;
	private static final double SunMass=1.9891e30D;
	private static final double SunRadius=6.958e8D;
	private static final double MoonMass=7.347673e22D;
	private static final double MoonRadius=1.738e6D;
	private static final long DistanceSunEarth=149597870691L;
	private static final long DistanceMoonEarth=384400000L;
	/**
	 * @return the suite of tests being tested
	 * 
	 *         public static Test suite() { return new TestSuite(
	 *         WorldTest.class ); }
	 */

	public FunctionalTest() {
		// TODO Auto-generated constructor stub
	}
	
	@org.junit.Test
	public void basePhisicTest(){
		Cosmos space = new Cosmos();
		TimeQueque<iPlanetoid> tq = new TimeQueque<iPlanetoid>();
		SolarSystemPhisycs phisycs = new SolarSystemPhisycs(space, tq, 1,1);
		space.addObserver(phisycs);
	    tq.addObserver(phisycs);
	    Planet unitTestPlanet=new Planet(1, 1);
	    Position startPosition=new Position();
	    phisycs.insert(unitTestPlanet,startPosition );
	    Force unitTestFore=new Force((double) 1,new Acceleration(1,1,1));
	    phisycs.applyForce(unitTestPlanet, unitTestFore);
	    phisycs.run();
	    phisycs.run();
	    Position endPosition=space.getPosition(unitTestPlanet);
	    Assert.assertTrue("Test fallito Position:"+endPosition,endPosition.getX()==endPosition.getY() && endPosition.getY()==endPosition.getZ() && endPosition.getZ()==1);
	    phisycs.removeForce(unitTestPlanet, unitTestFore);
	    phisycs.run();
	    Assert.assertTrue("Test fallito Position:"+endPosition,endPosition.getX()==endPosition.getY() && endPosition.getY()==endPosition.getZ() && endPosition.getZ()==2);
	}

	@org.junit.Test
	public void testSolarSystem() {
		float maxVelocity=100000;//SolarSystemPhisycs.LIGHT_VELOCITY
		float spacePrecision=1000;// in metri, 1km x simulazioni stellari
		int oneSecond=(int) (maxVelocity/spacePrecision);
		long numRun=(long) oneSecond*3;
		long start=System.currentTimeMillis();
		StarsContainer sc = new StarsContainer();

		sc.init(spacePrecision,maxVelocity);
		
		Star sun=insertSun(sc);
		Position sunCoordinate = sc.getSpace().getPosition(sun);
		
		Planet earth=insertEarth(sc);
		Position earthCoordinate = sc.getSpace().getPosition(earth);
		Force orbitalForceEarth=new Force(earth.getMass(),new Acceleration(0,0,29783));
		sc.getPhisycs().applyForce(earth, orbitalForceEarth);
		
		Planet moon=insertMoon(sc);
		Position moonCoordinate=sc.getSpace().getPosition(moon);
		Force orbitalForceMoon=new Force(moon.getMass(),new Acceleration(-1000,0,0));
		sc.getPhisycs().applyForce(moon, orbitalForceMoon);
		
		iPlanetoid sat=insertSatellite(sc);
		Position satCoordinate=sc.getSpace().getPosition(sat);
		Force orbitalForceSat=new Force(sat.getMass(),new Acceleration(-6000,0,0));
		sc.getPhisycs().applyForce(sat, orbitalForceSat);
		
		//Apply force x 1 second
		for (long i = 0; i < oneSecond+1; i++) {
			sc.run();
		}
		System.out.println("Stop apply forcs");
		sc.getPhisycs().removeForce(earth, orbitalForceEarth);
		sc.getPhisycs().removeForce(moon, orbitalForceMoon);
		sc.getPhisycs().removeForce(sat, orbitalForceSat);
		
		for (long i = 0; i < numRun-(oneSecond+1); i++) {
			sc.run();
		}
		
		System.out.println("Solar System test successull terminated");
		System.out.println("Results:");
		long secondsSimulated=(long) (numRun/oneSecond);
		System.out.println(secondsSimulated+" seconds simulated in "+((System.currentTimeMillis()-start)/1000)+"s");
		
		Position sunFinalPosition=sc.getSpace().getPosition(sun);
		BigVector sunDistance=sunCoordinate.getDistanceVector(sunFinalPosition);
		System.out.println("Sun movements:"+sunDistance);
		
		Position earthFinalPosition=sc.getSpace().getPosition(earth);
		BigVector earthDistance=earthCoordinate.getDistanceVector(earthFinalPosition);
		System.out.println("Earth movements:"+earthDistance);
		
		Position moonFinalPosition=sc.getSpace().getPosition(moon);
		BigVector moonDistance=moonCoordinate.getDistanceVector(moonFinalPosition);
		System.out.println("Moon movements:"+moonDistance);
		
		Position satFinalPosition=sc.getSpace().getPosition(sat);
		BigVector satDistance=satCoordinate.getDistanceVector(satFinalPosition);
		System.out.println("Satellite movements:"+satDistance);
	}

	//Dati reali espressi in Kg e metri
	private Planet generateEarth(){
		Planet earth=new LivingPlanet(EarthMass,EatrhRadius );
		earth.setUID("EARTH");
		return earth;
	}
	
	private Star generateSun(){
		Star sun=new Star(SunMass,SunRadius);
		sun.setUID("SUN");
		return sun;
	}
	private Planet insertEarth(StarsContainer sc) {
		Planet earth=generateEarth();
		sc.insertEntity(new Position(DistanceSunEarth, 0L, 0L),earth );
		return earth;
	}
	
	private Planet insertMoon(StarsContainer sc) {
		Planet earth=(Planet) findPlanetoid(sc.getAllEntities(),"Earth");
		Position earthPosition=sc.getSpace().getPosition(earth);
		Planet moon=new Planet(MoonMass, MoonRadius);
		moon.setUID("MOON");
		Position moonCoordinate = new Position(earthPosition.getX(),(long)(earthPosition.getY()+DistanceMoonEarth),earthPosition.getZ());
		sc.insertEntity(moonCoordinate,moon );
		return moon;
	}
	private iPlanetoid insertSatellite(StarsContainer sc) {
		Planet earth=(Planet) findPlanetoid(sc.getAllEntities(),"Earth");
		Position earthPosition=sc.getSpace().getPosition(earth);
		Planet satellite=new Planet(10, 10);
		satellite.setUID("SatBX213");
		long altitude=10000;
		Position satelliteCoordinate = new Position(earthPosition.getX(),(long)(earthPosition.getY()+EatrhRadius+altitude),earthPosition.getZ());
		sc.insertEntity(satelliteCoordinate,satellite );
		return satellite;
	}

	private Star insertSun(StarsContainer sc) {
		Star sun=generateSun();
		sc.insertEntity(new Position(0L, 0L, 0L), sun);
		return sun;
	}
	
	private iPlanetoid findPlanetoid(Collection<iPlanetoid> planets,String UID){
		Iterator<iPlanetoid> planetIterator=planets.iterator();
		while(planetIterator.hasNext()){
			iPlanetoid curPlanetoid=planetIterator.next();
			if(curPlanetoid.getUID().equalsIgnoreCase(UID)){
				return curPlanetoid;
			}
		}
		return null;
	}

}

package wol;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import wol.dom.Entity;
import wol.dom.WorldContainer;
import wol.dom.phisycs.UniversalGravityPhisycs;
import wol.dom.phisycs.iPhisycs;
import wol.dom.shape.PlanetShape;
import wol.dom.shape.StarShape;
import wol.dom.shape.iShape;
import wol.dom.space.Vector;
import wol.dom.space.iCoordinate;
import wol.dom.space.iSpace;
import wol.dom.space.planets.Planet;
import wol.dom.space.planets.PlanetSystem;
import wol.dom.space.planets.Planetoid;
import wol.dom.space.planets.Star;
import wol.dom.time.TimeQueque;

/**
 * Unit test for simple App.
 */
public class WorldTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WorldTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( WorldTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        WorldContainer<Planetoid> wc=new WorldContainer<Planetoid>();
        PlanetSystem ps=new PlanetSystem();
        UniversalGravityPhisycs phisics=new UniversalGravityPhisycs();
        phisics.setPlanets(ps);
        TimeQueque<Planetoid> tq=new TimeQueque<Planetoid>();
        wc.setPhisycs(phisics);
        wc.setSpace((iSpace)ps);
        wc.setTime(tq);
        wc.init();
        Vector earthCoordnate=new Vector(new Double(99),new Double(0),new Double(0));  
        wc.insertEntity(earthCoordnate, generateEarth());
        Vector sunCoordnate=new Vector(new Double(0),new Double(0),new Double(0));  
        wc.insertEntity(sunCoordnate, generateSun());
        for (int i=0;i<100;i++){
           wc.run();
        }
        assertTrue(true);
    }
    
    private Planet generateEarth(){
    	iShape earthShape=new PlanetShape();
    	Planet earth=new Planet(new Double(40055),new Float(6000),earthShape);
    	return earth;
    }
    private Planet generateSun(){
    	iShape sunShape=new StarShape();
    	Planet sun=new Star(new Double(4005500),new Float(600000),sunShape);
    	return sun;
    }
}

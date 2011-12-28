package wol;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import wol.dom.WorldContainer;
import wol.dom.phisycs.UniversalGravityPhisycs;
import wol.dom.space.planets.PlanetSystem;
import wol.dom.time.TimeQueque;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        WorldContainer wc=new WorldContainer();
        PlanetSystem ps=new PlanetSystem();
        UniversalGravityPhisycs phisics=new UniversalGravityPhisycs();
        TimeQueque tq=new TimeQueque();
        wc.setPhisycs(phisics);
        wc.setSpace(ps);
        wc.setTime(tq);
        wc.init();
        for (int i=0;i<100;i++){
           wc.run();
        }
        assertTrue(true);
    }
}

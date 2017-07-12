package edu.wol;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.wol.dom.shape.AsteroidShapeFactory;
import edu.wol.dom.space.Asteroid;
import edu.wol.dom.space.Planetoid;
import edu.wol.dom.space.Position;
import edu.wol.starsystem.SolarSystem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ JpaTestConfig.class})
public class JPATest {
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
    EntityManager em;
   
    @Test
	public void testSolarSystem() throws Exception{
    	SolarSystem ss= new SolarSystem();
    	ss.init(1, 1);
    	em.persist(ss);
    	long ssID=ss.getID();
    	SolarSystem ssLoaded=em.find(SolarSystem.class, ssID);
    	Assert.assertNotNull("Find SolarSystem Failed", ssLoaded);
    }
    
    @Test
	public void testInsertPlanetoids() throws Exception{
    	SolarSystem ss= new SolarSystem();
    	ss.init(1, 1);
    	em.persist(ss);
    	Asteroid a1=new Asteroid(Collections.singletonList("h2"),1,1);
    	a1.setShape(AsteroidShapeFactory.getInstance().generateShape());
    	Position p1=new Position();
    	em.persist(a1);
    	em.persist(p1);
    	ss.insertEntity(p1, a1);
    	em.merge(ss);
    	Asteroid a2=new Asteroid(Collections.singletonList("h2"),2,2);
    	a2.setShape(AsteroidShapeFactory.getInstance().generateShape());
    	Position p2=new Position(2,2,2);
    	em.persist(a2);
    	em.persist(p2);
    	ss.insertEntity(p2, a2);
    	em.merge(ss);
    	long ssID=ss.getID();
    	SolarSystem ssLoaded=em.find(SolarSystem.class, ssID);
    	Assert.assertNotNull("Find SolarSystem Failed", ssLoaded);
    	Collection<Planetoid> entities=ssLoaded.getAllEntities();
    	Assert.assertTrue("Errore nel resuperare il coretto numero di entities",entities.size()==2);
    }
}

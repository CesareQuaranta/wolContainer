package edu.wol;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}

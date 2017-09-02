package edu.wol;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import edu.wol.dom.phisycs.Acceleration;
import edu.wol.dom.phisycs.Velocity;
import edu.wol.dom.space.Planet;
import edu.wol.dom.space.Planetoid;
import edu.wol.dom.space.Position;
import edu.wol.dom.time.Ichinen;
import edu.wol.space.Inertial;
import edu.wol.space.Orbital;
import edu.wol.space.Static;

public class UnitTest {

	public UnitTest() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testStaticSpace() {
		Static<Planetoid> space = new Static<Planetoid>(1f);
		Assert.assertTrue("Insert Error", space.insertEntity(new Position(), generateRandomPlanet()));
		Assert.assertFalse("Duplicate Insert with same coordinate",space.insertEntity(new Position(), generateRandomPlanet()));
	}
	
	@Test
	public void testInertialSpace() {
		TimeQueque<Planetoid> time = new TimeQueque<Planetoid>(1f);
		Inertial<Planetoid> space = new Inertial<Planetoid>(1f,time);
		Position pos = new Position();
		//Mock Planet 
		Planetoid p = Mockito.mock(Planet.class);
		Mockito.when(p.getID()).thenReturn(1L);
		Mockito.when(p.isNew()).thenReturn(false);
		Mockito.when(p.getRadius()).thenReturn(1d);
		
		Assert.assertTrue("Insert Error", space.insertEntity(pos, p, new Velocity(1,1,0,0), new Acceleration(1,0,0)));
		Position pos2 = space.getPosition(p);
		Assert.assertTrue("Error initial position",pos.equals(pos2));
		time.run();
		pos2 = space.getPosition(p);
		Assert.assertFalse("Error inertial position", pos2.x == 0);
		
		//TODO insert 2°entity && check collision
		pos2 = new Position(10,0,0);
		//Mock Planet2 
		Planetoid p2 = Mockito.mock(Planet.class);
		Mockito.when(p2.getID()).thenReturn(2L);
		Mockito.when(p2.isNew()).thenReturn(false);
		Mockito.when(p2.getRadius()).thenReturn(1d);

		Assert.assertTrue("Second Insert Error", space.insertEntity(pos2, p2, new Velocity(1,-1,0,0), new Acceleration(-1,0,0)));
		Assert.assertTrue("Fail to detect future collision", time.getLength() == 1);
	}
	
	@Test
	public void testTime() {
		TimeQueque<Planetoid> timeQ=new  TimeQueque<Planetoid>(1f);
		List<Ichinen<Planetoid>> ichinenList=new ArrayList<Ichinen<Planetoid>>(5);
		for(int i=0;i<5;i++)
			ichinenList.add(i,new Ichinen<Planetoid>(null));
		
		timeQ.addFuture(ichinenList.get(2), 3);
		timeQ.addFuture(ichinenList.get(0), 0);
		timeQ.addFuture(ichinenList.get(1), 1);
		timeQ.addFuture(ichinenList.get(4), 10);
		timeQ.addFuture(ichinenList.get(3), 5);
		
		List<Integer> emptyIndex=new ArrayList<Integer>(6);
		emptyIndex.add(2);
		emptyIndex.add(4);
		emptyIndex.add(6);
		emptyIndex.add(7);
		emptyIndex.add(8);
		emptyIndex.add(9);
		int j=0;
		for(int i=0;i<11;i++){
			List<Ichinen<Planetoid>> present=timeQ.getPresent();
			if(emptyIndex.contains(i)){
				Assert.assertNull("Get present error: unespected present",present);
			}else{
				checkPresent(present,Collections.singletonList(ichinenList.get(j)));
				j++;
			}
		}
			
	}
	
	private void checkPresent(List<Ichinen<Planetoid>> present,List<Ichinen<Planetoid>> expected){
		Assert.assertNotNull("Get present error 1: Null present", present);
		Assert.assertFalse("Get present error 2: Empty present",present.isEmpty());
		Assert.assertFalse("Get present error 3: expected "+expected.size()+" present ichinen found "+present.size(),present.size()!=expected.size());
		Assert.assertTrue("Present error: invalid ichinen",present.containsAll(expected));
	}

	private Planetoid generateRandomPlanet(){
		return new Planet(Math.random(),Math.random());
	}
	
}

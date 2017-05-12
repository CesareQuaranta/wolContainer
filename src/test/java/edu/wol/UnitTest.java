package edu.wol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.wol.TimeQueque;
import edu.wol.dom.space.Planet;
import edu.wol.dom.space.Position;
import edu.wol.dom.space.Vector;
import edu.wol.dom.space.iPlanetoid;
import edu.wol.dom.time.Ichinen;
import edu.wol.starsystem.planets.Cosmos;

public class UnitTest {

	public UnitTest() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testSpace() {
		long px=149597870691L;
		Position p1=new Position(px,0,0);
		p1.sum(new Vector(-1.5f,-0.9f,1.001f));
		Assert.assertTrue("",p1.getX()<px);
		Assert.assertTrue("",p1.getY()==-1);
		Assert.assertTrue("",p1.getZ()==1);
		Cosmos space = new Cosmos();
		Assert.assertTrue("Insert Error", space.insertEntity(new Position(0L, 0L, 0L), generateRandomPlanet()));
		Assert.assertFalse("Duplicate Insert with same coordinate",space.insertEntity(new Position(0L, 0L, 0L), generateRandomPlanet()));
	}
	
	@Test
	public void testTime() {
		TimeQueque<iPlanetoid> timeQ=new  TimeQueque<iPlanetoid>();
		List<Ichinen<iPlanetoid>> ichinenList=new ArrayList<Ichinen<iPlanetoid>>(5);
		for(int i=0;i<5;i++)
			ichinenList.add(i,new Ichinen<iPlanetoid>(null));
		
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
			List<Ichinen<iPlanetoid>> present=timeQ.getPresent();
			if(emptyIndex.contains(i)){
				Assert.assertNull("Get present error: unespected present",present);
			}else{
				checkPresent(present,Collections.singletonList(ichinenList.get(j)));
				j++;
			}
		}
			
	}
	
	private void checkPresent(List<Ichinen<iPlanetoid>> present,List<Ichinen<iPlanetoid>> expected){
		Assert.assertNotNull("Get present error 1: Null present", present);
		Assert.assertFalse("Get present error 2: Empty present",present.isEmpty());
		Assert.assertFalse("Get present error 3: expected "+expected.size()+" present ichinen found "+present.size(),present.size()!=expected.size());
		Assert.assertTrue("Present error: invalid ichinen",present.containsAll(expected));
	}

	private iPlanetoid generateRandomPlanet(){
		return new Planet(Math.random(),Math.random());
	}
	
}

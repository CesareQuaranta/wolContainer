package wol.dom.phisycs;

import wol.dom.Entity;
import wol.dom.Seed;
import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.space.Coordinate;
import wol.dom.space.planets.PlanetSystem;
import wol.dom.space.planets.Planetoid;
import wol.dom.time.TimeEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 06/10/11
 * Time: 0.12
 * To change this template use File | Settings | File Templates.
 */
public class UniversalGravityPhisycs implements iPhisycs<PlanetSystem> {
    private PlanetSystem planets;
    private List<iEventObserver> observers=new ArrayList<iEventObserver>();

    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void insert(Entity entity) {
        List<Seed> seeds=new LinkedList<Seed>();
        //Generate Inner Couse
        for(Planetoid curPlanet:planets.getPlanets()){
            Seed entitySeed=new Seed(entity);
            Attraction entityAttraction=new Attraction(curPlanet);
            entitySeed.setInternalCause(entityAttraction);
            seeds.add(entitySeed);
            Seed curSeed=new Seed(curPlanet);
            Attraction curAttraction=new Attraction(entity);
            curSeed.setInternalCause(curAttraction);
            seeds.add(curSeed);
        }

        //Generate LatentEffect
        for (Seed curSeed:seeds){
            Entity subject=curSeed.getEntity();
            Coordinate subjectPosition=planets.getPosition((Planetoid)subject);

            Entity object=((Attraction)curSeed.getInternalCause()).getMagnet();
            Coordinate objectPosition=planets.getPosition((Planetoid)object);

            Double rapp=((Planetoid) subject).getMass()/((Planetoid) object).getMass();//* distanza
            Coordinate direction=new Coordinate();
            Movement movement=new Movement(direction);
            curSeed.setLatentEffect(movement);

             TimeEvent event=new TimeEvent();
            event.setFuture(1000);
            event.setSeed(curSeed);
            for(iEventObserver observer:observers){
                     observer.processEvent(event);
            }
        }


    }


    public void addObserver(iEventObserver observer) {
        observers.add(observer);
    }

    public void processSeeds(List<Seed> seeds){
            //Causa esterna effetto manifesto


    }


    public PlanetSystem getPlanets() {
        return planets;
    }

    public void setPlanets(PlanetSystem planets) {
        this.planets = planets;
    }


    public void processEvent(iEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

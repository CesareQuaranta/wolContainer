/**
 * 
 */
package wol.dom.space.planets;

import wol.dom.iEvent;
import wol.dom.iEventObserver;
import wol.dom.phisycs.Movement;
import wol.dom.space.Coordinate;
import wol.dom.space.iSpace;

import java.util.*;

/**
 * @author cesare
 *
 */
public class PlanetSystem implements iSpace<Planetoid,Coordinate> {
   Map<Coordinate,Planetoid> space;
    private List<iEventObserver> observers=new ArrayList<iEventObserver>();

    public  PlanetSystem(){
            space=new HashMap<Coordinate,Planetoid>();
    }

    public Collection<Planetoid> getPlanets(){
        return space.values();
    }

    public boolean addPlanet(Coordinate position,Planetoid planet){
        if (!space.containsKey(position)){
               space.put(position,planet);
            return true;
        }else{
            return false;
        }
    }

    public Planetoid getEntity(Coordinate position) {
        return space.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Coordinate getPosition(Planetoid planetoid) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void process(Planetoid planetoid, Movement movement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void addObserver(iEventObserver observer) {
        observers.add(observer);
    }


    public void processEvent(iEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

package wol.dom;

import wol.dom.phisycs.iPhisycs;
import wol.dom.space.Movement;
import wol.dom.space.iCoordinate;
import wol.dom.space.iSpace;
import wol.dom.time.iTime;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.50.16
 * To change this template use File | Settings | File Templates.
 */
public class WorldContainer<E extends Entity> implements Runnable, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8786030279582189151L;
	private iTime<E> time;
    private iSpace<E, iCoordinate> space;
    private iPhisycs<E> phisycs;


    public void init(){
        phisycs.addObserver(time);
        space.addObserver(phisycs);
        time.addObserver(space);
        time.addObserver(phisycs);
    }
    public void run() {
        time.run();
        //phisycs.run();
    }

    /*protected void processSeeds(List<Seed<E>> seeds){
          for(Seed<E> curSeed:seeds){
            iLatentEffect latentEffect=curSeed.getLatentEffect();
              E entity=curSeed.getEntity();
            if (latentEffect instanceof Movement){
                  Movement movement=(Movement)latentEffect;
                  space.process(entity,movement);
            }
       }
    }*/

       public iSpace<E, iCoordinate> getSpace() {
        return space;
    }

    public void setSpace(iSpace<E, iCoordinate> space) {
        this.space = space;
    }

    public iPhisycs<E> getPhisycs() {
        return phisycs;
    }

    public void setPhisycs(iPhisycs<E> phisycs) {
        this.phisycs = phisycs;
    }

    public iTime<E> getTime() {

        return time;
    }

    public void setTime(iTime<E> time) {
        this.time = time;
    }
    
    public boolean insertEntity(iCoordinate coordinate,E entity){
    	boolean insert=space.insertEntity(coordinate,entity);
    	if (insert){
    		phisycs.insert(entity);
    	}
    	return insert;
    }
}

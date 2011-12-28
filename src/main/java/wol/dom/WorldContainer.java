package wol.dom;

import wol.dom.phisycs.Movement;
import wol.dom.phisycs.iPhisycs;
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
public class WorldContainer implements Runnable, Serializable {
    private iTime time;
    private iSpace space;
    private iPhisycs phisycs;


    public void init(){
         phisycs.addObserver(time);
        time.addObserver(space);
        space.addObserver(phisycs);
    }
    public void run() {
        time.run();
        phisycs.run();
    }

    protected void processSeeds(List<Seed> seeds){
          for(Seed curSeed:seeds){
            iLatentEffect latentEffect=curSeed.getLatentEffect();
              Entity entity=curSeed.getEntity();
            if (latentEffect instanceof Movement){
                  Movement movement=(Movement)latentEffect;
                  space.process(entity,movement);
            }
       }
    }

       public iSpace getSpace() {
        return space;
    }

    public void setSpace(iSpace space) {
        this.space = space;
    }

    public iPhisycs getPhisycs() {
        return phisycs;
    }

    public void setPhisycs(iPhisycs phisycs) {
        this.phisycs = phisycs;
    }

    public iTime getTime() {

        return time;
    }

    public void setTime(iTime time) {
        this.time = time;
    }
}

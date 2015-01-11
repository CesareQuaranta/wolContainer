package wol.dom.phisycs;


import java.io.Serializable;
import java.util.Collection;

import wol.dom.Entity;
import wol.dom.iEventGenerator;
import wol.dom.iEventObserver;
import wol.dom.space.Position;
import wol.dom.space.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.51.52
 * To change this template use File | Settings | File Templates.
 */
public interface iPhisycs<E extends Entity> extends iEventGenerator<E>,iEventObserver<E>,Runnable,Serializable{
    public void insert(E entity,Position coordinate);
    //public Acceleration calcAcceleration(E entity,Collection<Force> forces);
    public void applyForce(E entity,Force force);
    public void removeForce(E entity,Force force);
}

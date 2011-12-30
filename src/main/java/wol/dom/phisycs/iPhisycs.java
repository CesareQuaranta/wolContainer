package wol.dom.phisycs;


import wol.dom.Entity;
import wol.dom.iEventObserver;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.51.52
 * To change this template use File | Settings | File Templates.
 */
public interface iPhisycs<E extends Entity> extends iEventObserver<E>,Runnable,Serializable{
    public void insert(E entity);
    public void addObserver(iEventObserver<E> observer);
}

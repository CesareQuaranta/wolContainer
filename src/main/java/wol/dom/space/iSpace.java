package wol.dom.space;

import wol.dom.Entity;
import wol.dom.iEventObserver;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.53.19
 * To change this template use File | Settings | File Templates.
 */
public interface iSpace<E extends Entity,C extends iCoordinate> extends iEventObserver<E>,Serializable{
    public E getEntity(C position);
    public C getPosition(E entity);
    public void process(E entity,Movement<E> movement);
    public void addObserver(iEventObserver<E> observer);
    public boolean insertEntity(C coordinate,E entity);
}

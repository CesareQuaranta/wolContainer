package wol.dom.space;

import wol.dom.iEventObserver;
import wol.dom.phisycs.Movement;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.53.19
 * To change this template use File | Settings | File Templates.
 */
public interface iSpace<Entity,tCoordinate> extends iEventObserver,Serializable{
    public Entity getEntity(tCoordinate position);
    public tCoordinate getPosition(Entity entity);
    public void process(Entity entity,Movement movement);
    public void addObserver(iEventObserver observer);
}

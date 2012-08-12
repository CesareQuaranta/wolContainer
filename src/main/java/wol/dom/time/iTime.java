package wol.dom.time;

import wol.dom.Entity;
import wol.dom.iEventObserver;
import wol.dom.LatentEffect;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.51.26
 * To change this template use File | Settings | File Templates.
 */
public interface iTime<E extends Entity> extends iEventObserver<E>,Runnable,Serializable{
	public List<LatentEffect<E>> getPresent();
     public void addObserver(iEventObserver<E> observer);
}

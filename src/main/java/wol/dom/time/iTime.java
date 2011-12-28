package wol.dom.time;

import wol.dom.iEventObserver;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.51.26
 * To change this template use File | Settings | File Templates.
 */
public interface iTime<T> extends iEventObserver,Runnable,Serializable{
	public List<T> getPresent();
     public void addObserver(iEventObserver observer);
}

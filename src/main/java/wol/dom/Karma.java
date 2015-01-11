package wol.dom;

import java.util.LinkedList;


/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 19/08/12
 * Time: 20.29
 * To change this template use File | Settings | File Templates.
 */
public class Karma<E extends Entity> {
	private LinkedList<Phenomen<E>> current=new LinkedList<Phenomen<E>>();
	private LinkedList<Phenomen<E>> past=new LinkedList<Phenomen<E>>();
	/*
    protected Map<iInternalCause<E>,Effect<E>> karmaSingleton=new HashMap<iInternalCause<E>,Effect<E>>();
    protected Map<iInternalCause<E>,Collection<Effect<E>>> karmaCollection=new HashMap<iInternalCause<E>,Collection<Effect<E>>>();

    public void add(iInternalCause<E> cause,Effect<E> effect){
        if(cause.isSingleton()){
            karmaSingleton.put(cause,effect);
        }else{
            Collection<Effect<E>> curEffects=karmaCollection.get(cause);
            if(curEffects==null){
                curEffects=new LinkedList<Effect<E>>();
                karmaCollection.put(cause,curEffects);
            }
            curEffects.add(effect);
        }
    }

    public Effect<E> getEffect(Phenomen<E> cause){
        return  karmaSingleton.get(cause);
    }

    public Collection<Effect<E>> getEffects(Phenomen<E> cause){
        return  karmaCollection.get(cause);
    }
*/
}

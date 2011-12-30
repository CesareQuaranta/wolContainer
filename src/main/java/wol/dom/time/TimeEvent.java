package wol.dom.time;

import wol.dom.Entity;
import wol.dom.Seed;
import wol.dom.iEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 10/10/11
 * Time: 23.12
 * To change this template use File | Settings | File Templates.
 */
public class TimeEvent<E extends Entity> implements iEvent<E> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4387449006747666951L;
	private int future;
    private Seed<E> seed;

    public TimeEvent(Seed<E> seed,int future){
    	this.seed=seed;
    	this.future=future;
    }
    
    public int getFuture() {
        return future;
    }

    public void setFuture(int future) {
        this.future = future;
    }

    public Seed<E> getSeed() {
        return seed;
    }

    public void setSeed(Seed<E> seed) {
        this.seed = seed;
    }
}

package wol.dom;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.10
 * To change this template use File | Settings | File Templates.
 */
public abstract class LatentEffect<E extends Entity> implements iEvent<E> {
    protected E entity;
    protected long delay;
    
    public LatentEffect(E entity,long delay){
    	this.entity=entity;
    	this.delay=delay;
    }
    
    public LatentEffect(E entity){
    	this(entity,0);
    }
    
	public E getEntity(){
		return entity;
	}
    public void setEntity(E entity){
    	this.entity=entity;
    }
	public long getDelay() {
		return delay;
	}
	public void setDelay(long delay) {
		this.delay = delay;
	}
}

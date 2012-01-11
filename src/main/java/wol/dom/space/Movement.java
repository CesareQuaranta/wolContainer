package wol.dom.space;

import wol.dom.Entity;
import wol.dom.LatentEffect;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.20
 * To change this template use File | Settings | File Templates.
 */
public class Movement<E extends Entity> extends LatentEffect<E> implements iSpaceEvent<E> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3053579265734463310L;
	private Vector vector;

    public Movement(E entity,long delay,Vector vector){
    	super(entity,delay);
        this.vector=vector;
    }

    public Movement(E entity,Vector vector){
    	this(entity,0,vector);
    }
    
	public Vector getVector() {
		return vector;
	}

	public void setVector(Vector vector) {
		this.vector = vector;
	}
}

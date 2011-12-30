package wol.dom.space;

import wol.dom.Entity;
import wol.dom.iLatentEffect;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.20
 * To change this template use File | Settings | File Templates.
 */
public class Movement<E extends Entity> implements iLatentEffect<E>,iSpaceEvent<E> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3053579265734463310L;
	private Vector vector;
    private E entity;

    public Movement(E entity,Vector vector){
    	this.entity=entity;
        this.vector=vector;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity=entity;
    }

	public Vector getVector() {
		return vector;
	}

	public void setVector(Vector vector) {
		this.vector = vector;
	}
}

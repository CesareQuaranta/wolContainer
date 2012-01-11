package wol.dom.phisycs;

import wol.dom.Entity;
import wol.dom.LatentEffect;
import wol.dom.space.Vector;

public class Acceleration<E extends Entity> extends LatentEffect<E>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8543988811101335479L;
	private Vector vector;
	
	public Acceleration(E entity, Vector vector) {
		this(entity,0,vector);
	}
	public Acceleration(E entity,long delay, Vector vector) {
		super(entity,delay);
		this.vector = vector;
	}
	public Vector getVector() {
		return vector;
	}
	public void setVector(Vector vector) {
		this.vector = vector;
	}
	
}

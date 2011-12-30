package wol.dom.phisycs;

import wol.dom.Entity;
import wol.dom.iEvent;

public class Collision<E extends Entity> implements iEvent<E>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6011658638891963136L;
	private E object;
	private E subject;
	
	public Collision(E object, E subject) {
		super();
		this.object = object;
		this.subject = subject;
	}
	
	public E getObject() {
		return object;
	}
	public void setObject(E object) {
		this.object = object;
	}
	public E getSubject() {
		return subject;
	}
	public void setSubject(E subject) {
		this.subject = subject;
	}
}

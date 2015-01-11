package wol.dom.space;

import wol.dom.Entity;
import wol.dom.iEvent;

public class NewPosition<E extends Entity> implements iEvent {
	private static final long serialVersionUID = 1L;
	private E entity;
	private Position newPosition;
	
	public NewPosition(E entity,Position newPosition) {
		this.entity=entity;
		this.newPosition=newPosition;
	}

	public E getEntity() {
		return entity;
	}

	public Position getNewPosition() {
		return newPosition;
	}

}

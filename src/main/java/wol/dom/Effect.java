package wol.dom;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.10
 * To change this template use File | Settings | File Templates.
 */
public abstract class Effect<E extends Entity> implements iEvent {
	private static final long serialVersionUID = 5692499734770881520L;
	private E entity;

	public Effect(E entity) {
		super();
		this.entity = entity;
	}

	public E getEntity() {
		return entity;
	}
	
}

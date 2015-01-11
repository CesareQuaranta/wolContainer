package wol.dom.time;

import wol.dom.Effect;
import wol.dom.Entity;
import wol.dom.iAction;
import wol.dom.iExternalCause;
import wol.dom.iInternalCause;
import wol.dom.iLatentEffect;
import wol.dom.iPower;

/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 22/08/12
 * Time: 17.28
 * To change this template use File | Settings | File Templates.
 */
public class Ichinen<E extends Entity> {
	private E entity;
	private iPower power;
    private iAction action;
    private iInternalCause<E> internalCause;
    private iExternalCause<E> externalCause;
    private iLatentEffect latentEffect;
    private Effect<E> effect;

	public Ichinen(E entity) {
		this.entity=entity;
	}


	@Override
	public String toString() {
		return "Ichinen ["
				+ (entity != null ? "entity=" + entity + ", " : "")
				+ (power != null ? "power=" + power + ", " : "")
				+ (action != null ? "action=" + action + ", " : "")
				+ (internalCause != null ? "internalCause=" + internalCause
						+ ", " : "")
				+ (externalCause != null ? "externalCause=" + externalCause
						+ ", " : "")
				+ (latentEffect != null ? "latentEffect=" + latentEffect + ", "
						: "") + (effect != null ? "effect=" + effect : "")
				+ "]";
	}

	public E getEntity() {
		return entity;
	}

	public void setEntity(E entity) {
		this.entity = entity;
	}

	public iPower getPower() {
		return power;
	}

	public void setPower(iPower power) {
		this.power = power;
	}

	public iAction getAction() {
		return action;
	}

	public void setAction(iAction action) {
		this.action = action;
	}

	public iInternalCause<E> getInternalCause() {
		return internalCause;
	}

	public void setInternalCause(iInternalCause<E> internalCause) {
		this.internalCause = internalCause;
	}

	public iExternalCause<E> getExternalCause() {
		return externalCause;
	}

	public void setExternalCause(iExternalCause<E> externalCause) {
		this.externalCause = externalCause;
	}

	public iLatentEffect getLatentEffect() {
		return latentEffect;
	}

	public void setLatentEffect(iLatentEffect latentEffect) {
		this.latentEffect = latentEffect;
	}

	public Effect<E> getEffect() {
		return effect;
	}

	public void setEffect(Effect<E> effect) {
		this.effect = effect;
	}
}

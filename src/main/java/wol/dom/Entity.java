package wol.dom;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.22
 * To change this template use File | Settings | File Templates.
 */
public abstract class Entity implements Serializable{
	protected Set<LatentEffect<Entity>> latentEffects=new HashSet<LatentEffect<Entity>>();
	protected Set<iInternalCause<Entity>> internalCauses=new HashSet<iInternalCause<Entity>>();
	protected Set<iPower> powers=new HashSet<iPower>();
	
	public Set<LatentEffect<Entity>> getLatentEffects() {
		return latentEffects;
	}
	public Set<LatentEffect<Entity>> getLatentEffects(Class<?> type) {
		Set<LatentEffect<Entity>> rValue=new HashSet<LatentEffect<Entity>>();
		for(LatentEffect<Entity> curLatentEffect:latentEffects){
			if(curLatentEffect.getClass().isInstance(type)){
				rValue.add(curLatentEffect);
			}
		}
		return rValue;
	}
	public void setLatentEffects(Set<LatentEffect<Entity>> latentEffects) {
		this.latentEffects = latentEffects;
	}
	public boolean addLatentEffect(LatentEffect<Entity> latentEffect) {
		return this.latentEffects.add(latentEffect);
	}
	public Set<iInternalCause<Entity>> getInternalCauses() {
		return internalCauses;
	}
	public void setInternalCauses(Set<iInternalCause<Entity>> internalCauses) {
		this.internalCauses = internalCauses;
	}
	public boolean addInternalCause(iInternalCause<Entity> internalCause) {
		return this.internalCauses.add(internalCause);
	}
	public Set<iPower> getPowers() {
		return powers;
	}
	public void setPowers(Set<iPower> powers) {
		this.powers = powers;
	}
	public boolean addPower(iPower power) {
		return this.powers.add(power);
	}
	
}

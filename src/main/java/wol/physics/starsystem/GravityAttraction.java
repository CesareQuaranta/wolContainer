package wol.physics.starsystem;

import java.util.Map;

import wol.dom.iInternalCause;
import wol.dom.space.BigVector;
import wol.dom.space.iPlanetoid;

public class GravityAttraction implements iInternalCause<iPlanetoid> {
	private static final long serialVersionUID = 1L;
	private iPlanetoid entity;
	private Map<GravityField,BigVector> gravityFields;


	public GravityAttraction(iPlanetoid curPlanet, Map<GravityField,BigVector> gravityFields) {
		this.entity=curPlanet;
		this.gravityFields=gravityFields;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}


	public iPlanetoid getEntity() {
		return entity;
	}

	public void setEntity(iPlanetoid entity) {
		this.entity = entity;
	}

	public Map<GravityField,BigVector> getGravityFields() {
		return gravityFields;
	}

	@Override
	public String toString() {
		return "GA ["
				+ (entity != null ? "entity=" + entity + ", " : "")
				+ (gravityFields != null ? "Gfields=" + gravityFields
						: "") + "]";
	}

}

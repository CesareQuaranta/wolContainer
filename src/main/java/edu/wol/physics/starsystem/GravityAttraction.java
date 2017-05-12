package edu.wol.physics.starsystem;

import java.util.Map;

import edu.wol.dom.iInternalCause;
import edu.wol.dom.space.BigVector;
import edu.wol.dom.space.Planetoid;

public class GravityAttraction implements iInternalCause<Planetoid> {
	private static final long serialVersionUID = 1L;
	private Planetoid entity;
	private Map<GravityField,BigVector> gravityFields;


	public GravityAttraction(Planetoid curPlanet, Map<GravityField,BigVector> gravityFields) {
		this.entity=curPlanet;
		this.gravityFields=gravityFields;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}


	public Planetoid getEntity() {
		return entity;
	}

	public void setEntity(Planetoid entity) {
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

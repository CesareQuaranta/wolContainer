package edu.wol.physics.starsystem;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import edu.wol.dom.InternalCause;
import edu.wol.dom.phisycs.MassEntity;
import edu.wol.dom.space.BigVector;
import edu.wol.dom.space.Planetoid;

@Entity
public class GravityAttraction extends InternalCause<MassEntity> {
	private static final long serialVersionUID = 1L;
	@OneToOne(cascade=CascadeType.ALL)
	private MassEntity entity;
	@Transient
	private Map<GravityField,BigVector> gravityFields;

	public GravityAttraction() {
		this.entity=null;
		this.gravityFields=null;
	}
	public GravityAttraction(MassEntity entity, Map<GravityField,BigVector> gravityFields) {
		this.entity=entity;
		this.gravityFields=gravityFields;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}


	public MassEntity getEntity() {
		return entity;
	}

	public void setEntity(MassEntity entity) {
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

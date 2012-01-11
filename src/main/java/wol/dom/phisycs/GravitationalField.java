package wol.dom.phisycs;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import wol.dom.space.Vector;
import wol.dom.space.planets.Planetoid;;

public class GravitationalField implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -330847504887565414L;
	private Vector position;
	private Double radius;
	private Double totalMass;
	private Collection<Planetoid> planteoids;
	
	public GravitationalField() {
		this(new Vector(0.0d,0.0d,0.0d),0.0d,0.0d);
		
	}
	
	public GravitationalField(Vector position, Double radius, Double totalMass) {
		super();
		this.position = position;
		this.radius = radius;
		this.totalMass = totalMass;
		planteoids=new LinkedList<Planetoid>();
	}

	public Vector getPosition() {
		return position;
	}
	public void setPosition(Vector position) {
		this.position = position;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	public Double getTotalMass() {
		return totalMass;
	}

	public void setTotalMass(Double totalMass) {
		this.totalMass = totalMass;
	}

	public Collection<Planetoid> getPlanteoids() {
		return planteoids;
	}
	public void setPlanteoids(Collection<Planetoid> planteoids) {
		this.planteoids = planteoids;
	}
	public boolean addPlanteoids(Planetoid planteoid) {
		return this.planteoids.add(planteoid);
	}
	
	public boolean contains(Vector point){
		return(Math.abs(position.getDistance(point))<=radius);
	}
	
	

}

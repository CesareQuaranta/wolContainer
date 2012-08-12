package wol.dom.phisycs;

import wol.dom.iPower;
import wol.dom.space.Vector;

public class Force implements iPower{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2732542351134968687L;
	private Double intensity;
	private Vector vector;
	
	public Force(Double intensity, Vector vector) {
		super();
		this.intensity = intensity;
		this.vector = vector;
	}
	
	public Double getIntensity() {
		return intensity;
	}
	public void setIntensity(Double intensity) {
		this.intensity = intensity;
	}
	public Vector getVector() {
		return vector;
	}
	public void setVector(Vector vector) {
		this.vector = vector;
	}
	
	public void sum(Force force){
		this.vector.sum(force.vector);
		this.intensity+=force.intensity;
	}

}

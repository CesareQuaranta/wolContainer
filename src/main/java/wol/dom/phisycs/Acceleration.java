package wol.dom.phisycs;

import wol.dom.iPower;
import wol.dom.space.Vector;

public class Acceleration extends Velocity implements iPower  {
	private static final long serialVersionUID = -8543988811101335479L;
	
	public Acceleration() {
		super(0);
	}
	
	public Acceleration(float x, float y, float z) {
		super(1,x,y,z);
	}

	public Acceleration(Vector vector) {
		this(1, vector);
	}
	public Acceleration(float time, Vector vector) {
		super(time, vector);
	}
	
	
	public Velocity clone(){
		return new Acceleration(time,this);
	}
	

}

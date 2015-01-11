package wol.dom.phisycs;

import wol.dom.iAction;
import wol.dom.iLatentEffect;
import wol.dom.space.Vector;

public class Velocity extends Vector implements iAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8543988811101335479L;
	protected float time;
	
	private Velocity() {
		super();
		time=0;
	}
	public Velocity(float time) {
		super();
		this.time=time;
	}
	
	public Velocity(Vector vector) {
		this(1,vector);
	}
	
	public Velocity(float time,Vector vector) {
		super(vector.getX(),vector.getY(),vector.getZ());
		this.time=time;
	}
	
	public Velocity(float time,float x,float y,float z) {
		super(x,y,z);
		this.time=time;
	}
	protected void copy(Velocity source){
		this.x=source.x;
		this.y=source.y;
		this.z=source.z;
		this.time=source.time;
		
	}

	public float getTime() {
		return time;
	}
	
	public Velocity getMinimized4Space(){
		Velocity minimized=new Velocity();
		float max=Math.max(Math.abs(x), Math.abs(y));
		max=Math.max(max, Math.abs(z));
		minimized.x=x/max;
		minimized.y=y/max;
		minimized.z=z/max;
		minimized.time=time/max;
		return minimized;
	}
	
	public Velocity getMaximized4Time(){
		
		if(time>0 && time<1){
			Velocity maximized=new Velocity(1/time,div(time));
			return maximized;
		}
		return clone();
	}
	public Velocity sum(Velocity addend){
		if(this.isEmpty() && addend!=null){
			return addend.clone();
		}else if (!addend.isEmpty()){
			Velocity result=getMaximized4Time();;
			Velocity resultAddend=addend.getMaximized4Time();
			result.time*=resultAddend.time;
			return new Velocity(result.sum((Vector)resultAddend));
		}else 
			return new Velocity();
	}
	
	public float getIntensity(){
		return(time*getLenght());
	}
	
	public Velocity clone(){
		return new Velocity(time,this);
	}

}

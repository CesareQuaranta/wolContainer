package wol.dom.space;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 05/10/11
 * Time: 23.49
 * To change this template use File | Settings | File Templates.
 */
public class Vector implements Comparable<Vector>,iCoordinate,Cloneable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5130651281630047189L;
	protected float x;
	protected float y;
	protected float z;

    public Vector(){
    	this(0,0,0);
    }
    public Vector(float x, float y, float z){
    	this.x=x;
    	this.y=y;
    	this.z=z;
    }
    
	public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

	public int getDimensions() {
		return 3;
	}
	
	public double getDistance(Position point){
		double rValue=0;
        double distX=x-point.x;
        double distY=y-point.y;
        double distZ=z-point.z;
		double sum=Math.pow(distX, 2)+Math.pow(distY, 2)+Math.pow(distZ, 2);
		rValue=Math.sqrt(sum);
		return rValue;
	}

	public float getLenght(){
		float rValue=0;
		Double sum=Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2);
		rValue=(float)Math.sqrt(sum);
		return rValue;
	}
	public void minimize(){
		float max=Math.max(Math.abs(x), Math.abs(y));
		max=Math.max(max, Math.abs(z));
		x=x/max;
		y=y/max;
		z=z/max;	
	}
	
	public Vector sum(Vector addend){
		return sum(addend,1);
	}
	
	public Vector sum(Vector addend,double d){
		return new Vector((float)(this.x+(addend.x*d)),(float)(this.y+(addend.y*d)),(float)(this.z+(addend.z*d)));
		
	}
	public Vector multiply(long multiplier){
		return new Vector(x*multiplier,y*multiplier,z*multiplier);
	}
    public Vector multiply(float multiplier){
        return new Vector(x*multiplier,y*multiplier,z*multiplier);
    }
    
    public Vector div(float divisor){
        return new Vector(x/divisor,y/divisor,z/divisor);
    }
    
	public Vector clone(){
		return new Vector(x,y,z);
	}

	public boolean equals(Vector comp){
		return compareTo(comp)==0;
	}
	public int compareTo(Vector comp) {
		return Float.valueOf(x).compareTo(comp.y)+Float.valueOf(y).compareTo(comp.z)+Float.valueOf(z).compareTo(comp.z);
	}
	
	public boolean isEmpty(){
		return x==0 && y==0 && z==0;
	}
	
	public String toString(){
		return "v < x:"+x+" y:"+y+" z:"+z+" >";
	}

}

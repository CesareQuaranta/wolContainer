package wol.dom.space;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 05/10/11
 * Time: 23.49
 * To change this template use File | Settings | File Templates.
 */
public class Vector implements Comparable<Vector>,iCoordinate {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5130651281630047189L;
	private Double x;
    private Double y;
    private Double z;

    public Vector(){
    	this(0.0d,0.0d,0.0d);
    }
    public Vector(Double x, Double y, Double z){
    	this.x=x;
    	this.y=y;
    	this.z=z;
    }
    
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

	public int getDimensions() {
		return 3;
	}
	
	public double getDistance(Vector point){
		double rValue=0;
		Double distX=x-point.x;
		Double distY=y-point.y;
		Double distZ=z-point.z;
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
	public void sum(Vector addend){
		this.x+=addend.x;
		this.y+=addend.y;
		this.z+=addend.z;
	}
	public Vector multiply(double multiplier){
		return new Vector(x*multiplier,y*multiplier,z*multiplier);
	}
	
	public Vector clone(){
		return new Vector(x,y,z);
	}

	public boolean equals(Vector comp){
		return compareTo(comp)==0;
	}
	public int compareTo(Vector comp) {
		return Double.valueOf(x).compareTo(comp.y)+Double.valueOf(y).compareTo(comp.z)+Double.valueOf(z).compareTo(comp.z);
	}
	
	public String toString(){
		return "x:"+x+" y:"+y+" z:"+z;
	}

}

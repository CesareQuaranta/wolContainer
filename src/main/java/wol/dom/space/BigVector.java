package wol.dom.space;

public class BigVector implements Comparable<BigVector>,iCoordinate,Cloneable{
	private static final long serialVersionUID = 1L;
	protected double x;
	protected double y;
	protected double z;
	
	public BigVector() {
		x=0d;
		y=0d;
		z=0d;
	}

	public BigVector(double x, double y, double z) {
		this.x=x;
    	this.y=y;
    	this.z=z;
	}

	@Override
	public int getDimensions() {
		return 3;
	}

	public BigVector clone(){
		return new BigVector(x,y,z);
	}

	public boolean equals(BigVector comp){
		return compareTo(comp)==0;
	}
	public int compareTo(BigVector comp) {
		return Double.valueOf(x).compareTo(comp.y)+Double.valueOf(y).compareTo(comp.z)+Double.valueOf(z).compareTo(comp.z);
	}
	
	public boolean isEmpty(){
		return x==0 && y==0 && z==0;
	}
	
	public String toString(){
		return "v < x:"+x+" y:"+y+" z:"+z+" >";
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Vector minimize() {
		Double max=Math.max(Math.abs(x), Math.abs(y));
		max=Math.max(max, Math.abs(z));
		return new Vector((float)(x/max),(float)(y/max),(float)(z/max));
	}

}

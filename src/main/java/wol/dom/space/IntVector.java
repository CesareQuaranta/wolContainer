package wol.dom.space;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 05/10/11
 * Time: 23.49
 * To change this template use File | Settings | File Templates.
 */
public class IntVector implements Comparable<IntVector>,iCoordinate,Cloneable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5130651281630047189L;
	protected int x;
	protected int y;
	protected int z;

    public IntVector(){
    	this(0,0,0);
    }
    public IntVector(int x, int y, int z){
    	this.x=x;
    	this.y=y;
    	this.z=z;
    }
    
	public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
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
	
	
	public IntVector sum(IntVector addend){
		return sum(addend,1);
	}
	
	public IntVector sum(IntVector addend,int i){
		return new IntVector((int)(this.x+(addend.x*i)),(int)(this.y+(addend.y*i)),(int)(this.z+(addend.z*i)));
		
	}
	public IntVector multiply(int multiplier){
		return new IntVector(x*multiplier,y*multiplier,z*multiplier);
	}
   
    
    public IntVector div(int divisor){
        return new IntVector(x/divisor,y/divisor,z/divisor);
    }
    
	public IntVector clone(){
		return new IntVector(x,y,z);
	}

	public boolean equals(IntVector comp){
		return compareTo(comp)==0;
	}
	public int compareTo(IntVector comp) {
		return Integer.valueOf(x).compareTo(comp.y)+Integer.valueOf(y).compareTo(comp.z)+Integer.valueOf(z).compareTo(comp.z);
	}
	
	public boolean isEmpty(){
		return x==0 && y==0 && z==0;
	}
	
	public String toString(){
		return "v < x:"+x+" y:"+y+" z:"+z+" >";
	}

}

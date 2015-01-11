package wol.dom.space;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 05/10/11
 * Time: 23.49
 * To change this template use File | Settings | File Templates.
 */
public class Position implements Comparable<Position>,iCoordinate {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5130651281630047189L;
	protected long x;
	private double fx=0d; 
	protected long y;
	private double fy=0d; 
	protected long z;
	private double fz=0d; 
	
    public Position(){
    	this(0L,0L,0L);
    }
    public Position(long x, long y, long z){
    	this.x=x;
    	this.y=y;
    	this.z=z;
    }
    
	public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

	public int getDimensions() {
		return 3;
	}
	
	public double getDistance(Position point){
		double rValue=0;
        long distX=x-point.x;
        long distY=y-point.y;
        long distZ=z-point.z;
		double sum=Math.pow(distX, 2)+Math.pow(distY, 2)+Math.pow(distZ, 2);
		rValue=Math.sqrt(sum);
		return rValue;
	}
	
	public BigVector getDistanceVector(Position point){
        long distX=x-point.x;
        long distY=y-point.y;
        long distZ=z-point.z;
		return new BigVector(distX,distY,distZ);
	}

	public float getLenght(){
		float rValue=0;
		Double sum=Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2);
		rValue=(float)Math.sqrt(sum);
		return rValue;
	}
	public Position clone(){
		return new Position(x,y,z);
	}

	public Vector getDirectionVector(Position point){
		BigVector result=getDistanceVector(point);
		return result.minimize();
	}
	public void sum(Vector addend) {
		double xFractionSum=addend.x+fx;
		fx=xFractionSum-Math.round(xFractionSum);
		x+=Math.round(xFractionSum);
		
		double yFractionSum=addend.y+fy;
		fy=yFractionSum-Math.round(yFractionSum);
		y+=Math.round(yFractionSum);
		
		double zFractionSum=addend.z+fz;
		fz=zFractionSum-Math.round(zFractionSum);
		z+=Math.round(zFractionSum);
	}
	public boolean equals(Position comp){
		return compareTo(comp)==0;
	}
	public int compareTo(Position comp) {
		if(x==comp.x && y==comp.y && z==comp.z){
			return 0;
		}else 
			return new Float(getLenght()).compareTo(comp.getLenght());
	}
	
	public String toString(){
		return "x:"+x+" y:"+y+" z:"+z;
	}
	

}

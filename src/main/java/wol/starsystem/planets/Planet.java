package wol.starsystem.planets;

import wol.dom.Karma;
import wol.dom.shape.iShape;
import wol.starsystem.PlanetShape;

public class Planet implements iPlanetoid {
	private static final long serialVersionUID = 1L;
	protected String UID;
	protected Karma<Planet> karma;
    protected double mass;
    protected double radius;


    protected iShape shape;


    public Planet(double mass,double radius){
        this(mass,radius,new PlanetShape(radius));
        
    }
    
    protected Planet(double mass,double radius,iShape shape){
    	UID=String.valueOf(System.currentTimeMillis());
        this.mass=mass;
        this.radius=radius;
        this.shape=shape;
        karma=new Karma<Planet>();
        
    }


    public iShape getShape() {
        return shape;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setShape(iShape shape) {
        this.shape = shape;
    }

    @Override
    public String getUID() {
        return UID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public double getRadius() {
        return radius;

    }


    public int compareTo(iPlanetoid o) {
    	if(o==null){
    		return -1;
    	}else{
    		return this.UID.compareTo(o.getUID());
    	}
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public void setMass(double mass) {
        this.mass=mass;

    }

	@Override
	public Karma<Planet> getKarma() {
		// TODO Auto-generated method stub
		return karma;
	}

	@Override
	public String toString() {
		return "Planet [" + UID + ", mass="
				+ mass + ", radius=" + radius + "]";
	}
}

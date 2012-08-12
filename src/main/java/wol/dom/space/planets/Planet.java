package wol.dom.space.planets;

import wol.dom.shape.iShape;

public class Planet extends Planetoid {
    protected String UID;
    protected Double mass;
    protected Float radius;

    protected iShape shape;


    public Planet(Double mass,Float radius,iShape shape){
        this.mass=mass;
        this.radius=radius;
        this.shape=shape;
    }


    public iShape getShape() {
        return shape;
    }

    public String getUID() {
        return null;
    }

    public Double getMass() {
        return mass;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public void setShape(iShape shape) {
        this.shape = shape;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Float getRadius() {
        return radius;

    }


    public int compareTo(Planetoid o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

package wol.dom.space.planets;

import wol.dom.shape.iShape;

public class Planet extends Planetoid {
    protected String UID;
    protected Double mass;
    protected Double circumference;

    protected iShape shape;


    public Planet(Double mass,Double circumference,iShape shape){
        this.mass=mass;
        this.circumference=circumference;
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

    public void setCircumference(Double circumference) {
        this.circumference = circumference;
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

    public Double getCircumference() {
        return circumference;

    }


    public int compareTo(Planetoid o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

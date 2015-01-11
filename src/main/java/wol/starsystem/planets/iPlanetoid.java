package wol.starsystem.planets;

import wol.dom.phisycs.iMassEntity;
import wol.dom.shape.iShape;

public interface iPlanetoid extends iMassEntity,Comparable<iPlanetoid>{
    public double getRadius();
    public iShape getShape();
}

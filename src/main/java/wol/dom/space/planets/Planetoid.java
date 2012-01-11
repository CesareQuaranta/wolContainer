package wol.dom.space.planets;

import wol.dom.Entity;
import wol.dom.LatentEffect;
import wol.dom.shape.iShape;

public abstract class Planetoid extends Entity implements Comparable<Planetoid>{
    public abstract String getUID();
    public abstract Double getMass();
    public abstract Float getRadius();
    public abstract iShape getShape();
}

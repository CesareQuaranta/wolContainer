package wol.dom.phisycs;

import wol.dom.Entity;
import wol.dom.iLatentEffect;
import wol.dom.space.Coordinate;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.20
 * To change this template use File | Settings | File Templates.
 */
public class Movement implements iLatentEffect {
    private Coordinate direction;
    private Entity entity;

    public Movement(Coordinate direction){
        this.direction=direction;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity=entity;
    }
}

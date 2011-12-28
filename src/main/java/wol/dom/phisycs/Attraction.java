package wol.dom.phisycs;

import wol.dom.Entity;
import wol.dom.iInternalCause;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.19
 * To change this template use File | Settings | File Templates.
 */
public class Attraction implements iInternalCause{
    private Entity magnet;

    public Attraction(Entity magnet){
        this.magnet=magnet;
    }

    public Entity getMagnet() {
        return magnet;
    }
}

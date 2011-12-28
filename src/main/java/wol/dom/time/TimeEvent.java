package wol.dom.time;

import wol.dom.Seed;
import wol.dom.iEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 10/10/11
 * Time: 23.12
 * To change this template use File | Settings | File Templates.
 */
public class TimeEvent implements iEvent {
    private int future;
    private Seed seed;

    public int getFuture() {
        return future;
    }

    public void setFuture(int future) {
        this.future = future;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }
}

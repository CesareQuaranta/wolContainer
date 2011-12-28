package wol.dom;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.08
 * To change this template use File | Settings | File Templates.
 */
public class Seed {
    private Entity entity;
    private iInternalCause internalCause;
    private iLatentEffect latentEffect;

    public Seed(Entity entity){
        this.entity=entity;
    }

    public Entity getEntity(){
        return this.entity;
    }
    public iInternalCause getInternalCause() {
        return internalCause;
    }

    public void setInternalCause(iInternalCause internalCause) {
        this.internalCause = internalCause;
    }

    public iLatentEffect getLatentEffect() {
        return latentEffect;
    }

    public void setLatentEffect(iLatentEffect latentEffect) {
        this.latentEffect = latentEffect;
    }
}

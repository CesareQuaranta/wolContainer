package wol.dom;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.08
 * To change this template use File | Settings | File Templates.
 */
public class Seed<E extends Entity> {
    private E entity;
    private iInternalCause internalCause;
    private iLatentEffect<E> latentEffect;

    public Seed(E entity){
        this.entity=entity;
    }

    public E getEntity(){
        return this.entity;
    }
    public iInternalCause getInternalCause() {
        return internalCause;
    }

    public void setInternalCause(iInternalCause internalCause) {
        this.internalCause = internalCause;
    }

    public iLatentEffect<E> getLatentEffect() {
        return latentEffect;
    }

    public void setLatentEffect(iLatentEffect<E> latentEffect) {
        this.latentEffect = latentEffect;
    }
}

package wol.dom;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.09
 * To change this template use File | Settings | File Templates.
 */
public interface iInternalCause<E extends Entity> extends iEvent {
    public boolean isSingleton();
}

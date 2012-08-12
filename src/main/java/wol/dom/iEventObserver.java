package wol.dom;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 10/10/11
 * Time: 23.01
 * To change this template use File | Settings | File Templates.
 */
public interface iEventObserver<E extends Entity> {
    public void processEvent(iEvent<E> event);
}

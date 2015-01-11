package wol.dom;

/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 18/08/13
 * Time: 18.03
 * To change this template use File | Settings | File Templates.
 */
public interface iEventGenerator<E extends Entity> {
    public void addObserver(iEventObserver<E> observer);
}

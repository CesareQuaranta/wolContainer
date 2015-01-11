package wol.dom;

import java.io.Serializable;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 14/08/13
 * Time: 10.21
 * To change this template use File | Settings | File Templates.
 */
public interface Entity extends Serializable {
    String getUID();

    void setUID(String UID);

   Karma<? extends Entity> getKarma();/*

    Set<iPower> getPowers();

    void setPowers(Set<iPower> powers);

    boolean addPower(iPower power); */
}

package wol.dom.phisycs;

import java.util.Collection;

import wol.dom.Entity;
import wol.dom.iExternalCause;
import wol.dom.iInternalCause;
import wol.starsystem.planets.iPlanetoid;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 07/10/11
 * Time: 0.19
 * To change this template use File | Settings | File Templates.
 */
public class Forces<E extends Entity> implements iExternalCause<E>{
	private static final long serialVersionUID = 1L;
	private Collection<Force> forces;

    public Forces(Collection<Force> forces){
        this.forces=forces;
    }

	public Collection<Force> getForces() {
		return forces;
	}
}

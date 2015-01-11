package wol.dom.phisycs;

import wol.dom.iPower;

public interface ForceFactory extends iPower {
	public Force getForce(iForceElements elements);
}

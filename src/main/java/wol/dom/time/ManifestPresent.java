package wol.dom.time;

import java.util.List;

import wol.dom.Entity;
import wol.dom.iEvent;

public class ManifestPresent<E extends Entity> implements iEvent {
	private static final long serialVersionUID = 1L;
	private List<Ichinen<E>> present;
	
	public ManifestPresent(List<Ichinen<E>> present) {
		this.present=present;
	}

	public List<Ichinen<E>> getPresent() {
		return present;
	}
	

}

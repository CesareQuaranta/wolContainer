package wol;

import java.util.List;

import wol.dom.Entity;
import wol.dom.WorldContainer;
import wol.dom.iEvent;
import wol.dom.space.Position;
import wol.dom.Window;
public abstract class WolContainer<T extends WorldContainer<E,Position>,E extends Entity> implements Runnable {
	
	public abstract Window openWindow(Position pos);
	
	public abstract List<iEvent> getEvents(String windowIdentifier);
}

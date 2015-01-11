package wol.dom;

import java.util.UUID;

import wol.dom.space.IntVector;
import wol.dom.space.Position;

public class Window {
	private String UID;
	private Position position;
	private IntVector dimensions; 
	
	
	public Window(){
		this(null,null);
	}
	
	public Window(Position position,IntVector dimensions) {
		super();
		this.position = position;
		this.dimensions=dimensions;
		UID="W"+UUID.randomUUID().toString();

	}
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public IntVector getDimensions() {
		return dimensions;
	}

	public void setDimensions(IntVector dimensions) {
		this.dimensions = dimensions;
	}
	

}

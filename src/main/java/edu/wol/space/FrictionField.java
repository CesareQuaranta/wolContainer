package edu.wol.space;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import edu.wol.dom.space.Vector3f;

@Entity
public class FrictionField {
	@Id
	@GeneratedValue
	private long ID;
	
	@Lob
	private Vector3f start;
	@Lob
	private Vector3f end;
	
	public FrictionField(Vector3f start, Vector3f end) {
		super();
		this.start = start;
		this.end = end;
	}
	public Vector3f getStart() {
		return start;
	}
	public void setStart(Vector3f start) {
		this.start = start;
	}
	public Vector3f getEnd() {
		return end;
	}
	public void setEnd(Vector3f end) {
		this.end = end;
	}

}

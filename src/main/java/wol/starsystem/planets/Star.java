package wol.starsystem.planets;

import wol.starsystem.StarShape;

public class Star extends Planet {
	private static final long serialVersionUID = 1L;

	public Star(double mass, double radius) {
        super(mass, radius, new StarShape(radius));
    }
}

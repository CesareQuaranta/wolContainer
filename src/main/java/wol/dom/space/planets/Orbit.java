package wol.dom.space.planets;

import wol.dom.space.Coordinate;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 05/10/11
 * Time: 23.43
 * To change this template use File | Settings | File Templates.
 */
public class Orbit {
    private Coordinate center;
    private Double radius;
    private Double eccentricity;

    public Coordinate getCenter() {
        return center;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(Double eccentricity) {
        this.eccentricity = eccentricity;
    }
}

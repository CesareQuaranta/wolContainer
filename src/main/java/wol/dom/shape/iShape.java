package wol.dom.shape;

import wol.dom.space.Position;

/**
 * Created by IntelliJ IDEA.
 * User: cesare
 * Date: 06/10/11
 * Time: 23.53
 * To change this template use File | Settings | File Templates.
 */
public interface iShape {
	boolean checkInterseption(Position position,iShape otherShape,Position otherPosition);
}

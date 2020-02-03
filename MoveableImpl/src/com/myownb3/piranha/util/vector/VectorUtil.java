/**
 * 
 */
package com.myownb3.piranha.util.vector;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.gridelement.Position;

/**
 * Contains some useful methods for creating {@link Float64Vector} from
 * {@link Position} or a {@link Direction}
 * 
 * @author Dominic
 *
 */
public class VectorUtil {

    /**
     * Creates a {@link Float64Vector} for the given {@link Position} and its x,-
     * and y-coordinates
     * 
     * @param pos
     *            the {@link Position}
     * @return a {@link Float64Vector}
     */
    public static Float64Vector getVector(Position pos) {
	return Float64Vector.valueOf(pos.getX(), pos.getY(), 0);
    }

    /**
     * Creates a {@link Float64Vector} for the given {@link Position} and its x,-
     * and y-forwarding coordinates
     * 
     * @param direction
     *            the {@link Direction}
     * @return a {@link Float64Vector}
     * 
     * @see Direction#getForwardX()
     * @see Direction#getForwardY()
     */
    public static Float64Vector getVector(Direction direction) {
	return Float64Vector.valueOf(direction.getForwardX(), direction.getForwardY(), 0);
    }
}

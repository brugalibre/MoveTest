/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.Direction;

/**
 * @author Dominic
 *
 */
public class Positions {

    /**
     * Creates a new {@link Position} with the given coordinates
     * 
     * @param x
     *            the x-axis coordinate
     * @param y
     *            the y-axis coordinate
     * @return a new created Position
     */
    public static Position of(double x, double y) {
	return new PositionImpl(x, y);
    }

    /**
     * Creates a new {@link Position} with the given coordinates
     * 
     * @param x
     *            the x-axis coordinate
     * @param y
     *            the y-axis coordinate
     * @return a new created Position
     */
    public static Position of(Direction direction, double x, double y) {
	return new PositionImpl(direction, x, y);
    }
}

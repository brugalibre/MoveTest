/**
 * 
 */
package com.myownb3.piranha.grid.direction.util;

import com.myownb3.piranha.grid.direction.Direction;

/**
 * @author Dominic
 *
 */
public class DirectionUtil {

    /**
     * Compares the given two {@link Direction}. Returns <code>false</code> if one
     * of them is <code>null</code>. Otherwise this methode compares both Directions
     * using {@link Direction#equals(Object)}
     * 
     * @param direction
     *            the {@link Direction}
     * @param direction2Compare
     *            the {@link Direction} to compare with
     * @return <code>true</code> if both are same of <code>false</code>if not or if
     *         one of them is <code>null</code>
     */
    public static boolean isSame(Direction direction, Direction direction2Compare) {
	if (direction == null || direction2Compare == null) {
	    return false;
	}
	return direction.equals(direction2Compare);
    }
}

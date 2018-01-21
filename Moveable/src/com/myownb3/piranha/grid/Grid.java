/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.Moveable;

/**
 * The {@link Grid} defines the place where a {@link Moveable} can be moved and
 * {@link Position}s can be placed.
 * 
 * @author Dominic
 *
 */
public interface Grid {

    /**
     * @param position
     * @return
     */
    Position moveBackward(Position position);

    /**
     * @param position
     * @return
     */
    Position moveForward(Position position);

}

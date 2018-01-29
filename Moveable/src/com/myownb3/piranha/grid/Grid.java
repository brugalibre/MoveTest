/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.GridElement;
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
     * Moves the given Position backward by one unit
     * 
     * @param position
     *            the {@link Position} to move
     * @return a new instance of the moved Position
     */
    Position moveBackward(Position position);

    /**
     * Moves the given Position forward by one unit
     * 
     * @param position
     *            the {@link Position} to move
     * @return a new instance of the moved Position
     */
    Position moveForward(Position position);

    /**
     * @param gridElement
     *            the element to verify
     * @return <code>true</code> if this {@link Grid} contains the given
     *         {@link GridElement} or <code>false</code> if not
     */
    boolean containsElement(GridElement gridElement);

    /**
     * Adds the given {@link GridElement} to this {@link Grid}
     * 
     * @param abstractGridElement
     */
    void addElement(GridElement gridElement);
}

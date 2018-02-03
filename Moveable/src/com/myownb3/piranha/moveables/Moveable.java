/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Grid;

/**
 * A Moveable is an object which is able to move itself on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface Moveable extends GridElement {

    /**
     * Moves this {@link Moveable} one unit forward, considering the current
     * {@link Direction}
     */
    void moveForward();

    /**
     * Moves this {@link Moveable} for the given amount of times forward,
     * considering the current {@link Direction}
     * 
     * @param amount
     *            the amount of units to move forward
     */
    void moveForward(int amount);

    /**
     * Moves this {@link Moveable} one unit backward considering the current
     * {@link Direction}
     */
    void moveBackward();

    /**
     * Moves this {@link Moveable} for the given amount of times forward,
     * considering the current {@link Direction}
     * 
     * @param amount
     *            the amount of units to move backward
     */
    void moveBackward(int amount);

    /**
     * Turns this {@link Moveable} to the right
     */
    void turnRight();

    /**
     * Turns this {@link Moveable} to the left
     */
    void turnLeft();

    /**
     * Turns this {@link Moveable} for the given amount of degrees
     * 
     * @param degree
     */
    void makeTurn(double degree);
}

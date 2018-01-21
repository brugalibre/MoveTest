/**
 * 
 */
package com.myownb3.piranha;

/**
 * @author Dominic
 *
 */
public interface Moveable {

    /**
     * Moves this {@link Moveable} one unit forward, considering the current
     * {@link Direction}
     */
    void moveForward();

    /**
     * Moves this {@link Moveable} for the given amount of times forward,
     * considering the current {@link Direction}
     */
    void moveForward(int amount);

    /**
     * Moves this {@link Moveable} one unit backward considering the current
     * {@link Direction}
     */
    void moveBackward();

    /**
     * @return the current {@link Position} of this {@link Moveable}
     */
    Position getPosition();

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
     * @param dregree
     */
    void turnDegree(int dregree);
}

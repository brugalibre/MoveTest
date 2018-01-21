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
    void makeTurn(int dregree);

}

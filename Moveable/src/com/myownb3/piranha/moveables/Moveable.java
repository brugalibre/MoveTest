/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.direction.Direction;

/**
 * A Moveable is an object which is able to move itself on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface Moveable extends GridElement {

    /**
     * The stepwitdh is used by the {@link Direction} as a divisor for dividing the 'move-forward-unit' that any
     * {@link Moveable} can move forward
     * 
     * @see Direction#getForwardX()
     * @see Direction#getForwardY()
     * @see Direction#getBackwardX()
     * @see Direction#getBackwardY()
     */
    public static final int STEP_WITDH = 10;
    
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

    /**
     * Moves this {@link Moveable} forward and makes a turn for the given amount of
     * degrees as one atomic action
     * 
     * @param degree
     */
    void moveMakeTurnAndForward(double degree);

    /**
     * 
     * @return a List with all the Points this {@link Moveable} has recently passed
     */
    List<Position> getPositionHistory();
    
    /**
     * Returns the smallest unit this Moveable can move forward or backward
     * @return
     */
    default double getSmallestStepWith() {
	return 1d / STEP_WITDH;
    }
}

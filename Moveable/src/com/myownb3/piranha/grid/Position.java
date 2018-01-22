/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.Direction;

/**
 * Defines a specific position on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface Position {

    /**
     * Rotate this {@link Position} for the given amount of degrees
     * 
     * @param dregree
     */
    void rotate(int dregree);

    /**
     * @return the Direction in which this Positions shows
     */
    Direction getDirection();

    /**
     * @return the y-axis value
     */
    double getY();

    /**
     * @return the x-axis value
     */
    double getX();

    /**
     * Calculates the distance between this and the given {@link Position}
     * 
     * @param position
     * @return the calculated distance
     */
    double calcDistanceTo(Position position);

    /**
     * @return
     */
    double calcAbsolutAngle();

}

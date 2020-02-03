/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.direction.Direction;

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
    void rotate(double dregree);

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
     * @return the angle of this Position (not its direction) relatively to the
     *         given coordinate system or {@link Grid}
     */
    double calcAbsolutAngle();

    /**
     * Calculates the angle between this point relatively to their coordinates on
     * the {@link Grid}
     * 
     * @param position
     *            the position
     * @return the relatively angle between this and the other position
     */
    double calcAngleRelativeTo(Position position);

}

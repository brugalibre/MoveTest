/**
 * 
 */
package com.myownb3.piranha.grid.direction;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * The {@link Direction} indicates the orientation on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface Direction {

    /**
     * @return value on the y-axis by which {@link Position} can be moved backward
     */
    double getBackwardY();

    /**
     * @return value on the x-axis by which {@link Position} can be moved backward
     */
    double getBackwardX();

    /**
     * @return value on the y-axis by which {@link Position} can be moved forward
     */
    double getForwardY();

    /**
     * @return value on the x-axis by which {@link Position} can be moved forward
     */
    double getForwardX();

    /**
     * Rotate this Direction for the given amount of degrees
     * 
     * @param degree
     * @return a new and turned instance of a {@link Direction}
     */
    Direction rotate(double degree);

    /**
     * @return the angle of this Direction
     */
    double getAngle();
}

/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.AbstractGridElement;

/**
 * @author Dominic
 *
 */
public class ObstacleImpl extends AbstractGridElement implements Obstacle {

    /**
     * @param grid
     * @param position
     */
    public ObstacleImpl(Grid grid, Position position) {
	super(position, grid);
    }
}

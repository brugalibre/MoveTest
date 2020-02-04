/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.AbstractMoveable;

/**
 * @author Dominic
 *
 */
public class MoveableObstacleImpl extends AbstractMoveable implements Obstacle {

    /**
     * @param grid
     * @param position
     */
    public MoveableObstacleImpl(Grid grid, Position position) {
	super(grid, position);
    }
}

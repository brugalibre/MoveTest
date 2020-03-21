/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;

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
      super(grid, position);
   }
}

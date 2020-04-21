/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.Shape;

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

   /**
    * @param grid
    * @param position
    */
   public ObstacleImpl(Grid grid, Position position, Shape shape) {
      super(grid, position, shape);
   }
}

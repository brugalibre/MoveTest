/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

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

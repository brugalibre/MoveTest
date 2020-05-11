/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import static java.util.Objects.isNull;

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
   private ObstacleImpl(Grid grid, Position position) {
      super(grid, position);
   }

   /**
    * @param grid
    * @param position
    */
   private ObstacleImpl(Grid grid, Position position, Shape shape) {
      super(grid, position, shape);
   }

   public static class ObstacleBuilder extends AbstractGridElementBuilder<ObstacleImpl> {

      private ObstacleBuilder() {
         // private
      }

      public static ObstacleBuilder builder() {
         return new ObstacleBuilder();
      }

      @Override
      public ObstacleImpl build() {
         if (isNull(shape)) {
            return new ObstacleImpl(grid, position);
         }
         return new ObstacleImpl(grid, position, shape);
      }
   }
}

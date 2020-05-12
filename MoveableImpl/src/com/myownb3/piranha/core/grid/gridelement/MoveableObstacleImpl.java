/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import static java.util.Objects.isNull;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveable;

/**
 * @author Dominic
 *
 */
public class MoveableObstacleImpl extends AbstractMoveable implements Obstacle {

   /**
    * @param grid
    * @param position
    */
   private MoveableObstacleImpl(Grid grid, Position position) {
      super(grid, position);
   }

   /**
    * @param grid
    * @param position
    */
   private MoveableObstacleImpl(Grid grid, Position position, Shape shape) {
      super(grid, position, shape);
   }

   public static class MoveableObstacleBuilder extends AbstractGridElementBuilder<MoveableObstacleImpl> {

      private MoveableObstacleBuilder() {
         // private
      }

      public static MoveableObstacleBuilder builder() {
         return new MoveableObstacleBuilder();
      }

      @Override
      public MoveableObstacleImpl build() {
         if (isNull(shape)) {
            return new MoveableObstacleImpl(grid, position);
         }
         return new MoveableObstacleImpl(grid, position, shape);
      }
   }
}

/**
 * 
 */
package com.myownb3.piranha.grid;

import static java.util.Objects.isNull;

import java.util.Objects;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.moveables.Moveable;

/**
 * The {@link MirrorGrid} is a {@link Grid} which mirrors any {@link Moveable}
 * as soon as this moveable is crossing the grids borders
 * 
 * @author Dominic
 *
 */
public class MirrorGrid extends DefaultGrid {

   /**
    * 
    * @param maxX
    * @param maxY
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   private MirrorGrid(int maxX, int maxY, CollisionDetectionHandler collisionDetectionHandler) {
      super(maxX, maxY, 0, 0, collisionDetectionHandler);
   }

   /**
    * 
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   private MirrorGrid(int maxX, int maxY, int minX, int minY, CollisionDetectionHandler collisionDetectionHandler) {
      super(maxX, maxY, minX, minY, collisionDetectionHandler);
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * com.myownb3.piranha.grid.DefaultGrid#moveForward(com.myownb3.piranha.grid.
    * Position)
    */
   @Override
   public Position moveForward(GridElement gridElement) {

      Position movedPos = super.moveForward(gridElement);

      if (movedPos.getX() == minX || movedPos.getX() == maxX) {
         movedPos = movedPos.rotate(180 - 2 * movedPos.getDirection().getAngle());
      }
      if (movedPos.getY() == minY || movedPos.getY() == maxY) {
         movedPos = movedPos.rotate(360 - 2 * movedPos.getDirection().getAngle());
      }

      return movedPos;
   }

   /**
    * Evaluates the new y value. Additionally it checks weather or not the new
    * value is in bounds. If not, the value is swapped
    * 
    * @param newY
    * @return the new y-Value within grid bounds
    */
   @Override
   protected double getNewYValue(GridElement gridElement, double forwardY) {
      double newY = super.getNewYValue(gridElement, forwardY);
      double newYValue = getNewYValue(gridElement.getFurthermostFrontPosition(), forwardY);

      if (newYValue >= maxY) {
         return maxY;
      } else if (newYValue <= minY) {
         return minY;
      }
      return newY;
   }

   /**
    * Evaluates the new x value. Additionally it checks weather or not the new
    * value is in bounds. If not, the value is swapped
    * 
    * @param newX
    * @return the new x-Value within grid bounds
    */
   @Override
   protected double getNewXValue(GridElement gridElement, double forwardX) {
      double newX = super.getNewXValue(gridElement, forwardX);
      double newXValue = getNewXValue(gridElement.getFurthermostFrontPosition(), forwardX);

      if (newXValue >= maxX) {
         return maxX;
      } else if (newXValue <= minX) {
         return minX;
      }
      return newX;
   }

   /**
    * The {@link MirrorGridBuilder} helps to build a {@link MirrorGrid}
    * 
    * @author Dominic
    *
    */
   public static class MirrorGridBuilder extends AbstractGridBuilder<MirrorGrid> {

      public static MirrorGridBuilder builder() {
         return new MirrorGridBuilder()
               .withMaxX(10)
               .withMaxY(10);
      }

      public static MirrorGridBuilder builder(int maxX, int maxY) {
         return new MirrorGridBuilder()
               .withMaxX(maxX)
               .withMaxY(maxY);
      }

      /**
       * Creates a new {@link MirrorGrid}
       * 
       * @return a new {@link MirrorGrid}
       */
      @Override
      public MirrorGrid build() {
         Objects.requireNonNull(maxX, "We need a max x value!");
         Objects.requireNonNull(maxY, "We need a max y value!");
         MirrorGrid mirrorGrid;
         if (isNull(minX) || isNull(minY)) {
            mirrorGrid = new MirrorGrid(maxX, maxY, collisionDetectionHandler);
         } else {
            mirrorGrid = new MirrorGrid(maxX, maxY, minX, minY, collisionDetectionHandler);
         }
         return mirrorGrid;
      }
   }
}

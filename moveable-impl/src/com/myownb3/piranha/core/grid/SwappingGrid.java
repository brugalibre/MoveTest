/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static java.util.Objects.isNull;

import java.util.Objects;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Positions.PositionImpl;

/**
 * 
 * A {@link SwappingGrid} makes sure, that {@link PositionImpl} are not
 * misplaced. That means, if any Position is placed out of bounds the Grid
 * places the Position on its reverse location
 * 
 * @author Dominic
 *
 */
public class SwappingGrid extends DefaultGrid {

   /**
    * 
    * @param maxX
    * @param maxY
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   private SwappingGrid(int maxX, int maxY, CollisionDetectionHandler collisionDetectionHandler) {
      super(maxX, maxY, 0, 0, collisionDetectionHandler);
   }

   /**
    * 
    * @param maxX
    * @param maxY
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   private SwappingGrid(int maxX, int maxY, int minX, int minY, CollisionDetectionHandler collisionDetectionHandler) {
      super(maxX, maxY, minX, minY, collisionDetectionHandler);
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
      double newYValue = getNewYValue(gridElement.getForemostPosition(), forwardY);
      if (newYValue > maxY) {
         newY = (newYValue - maxY) + minY;
      } else if (newYValue < minY) {
         newY = maxY - (minY - newYValue);
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
      double newXValue = getNewXValue(gridElement.getForemostPosition(), forwardX);
      if (newXValue > maxX) {
         newX = (newXValue - maxX) + minX;
      } else if (newXValue < minX) {
         newX = maxX - (minX - newXValue);
      }
      return newX;
   }

   /**
     * @formatter:off
     * 
     *10 ___________ 
     *   |__|__|__|__|
     *   |__|__|__|__|
     *   |__|__|__|__|
     *   |__|__|__|__|
     *  5|__|__|__|__|
     *   5       10 
     * 
     * @formatter:on
     */


   /**
    * The {@link SwappingGridBuilder} helps to build a {@link SwappingGrid}
    * 
    * @author Dominic
    *
    */
   public static class SwappingGridBuilder extends AbstractGridBuilder<SwappingGrid, SwappingGridBuilder> {

      public static SwappingGridBuilder builder() {
         return new SwappingGridBuilder()
               .withMaxX(10)
               .withMaxY(10);
      }

      public static SwappingGridBuilder builder(int maxX, int maxY) {
         return new SwappingGridBuilder()
               .withMaxX(maxX)
               .withMaxY(maxY);
      }

      /**
       * Creates a new {@link SwappingGrid}
       * 
       * @return a new {@link SwappingGrid}
       */
      @Override
      public SwappingGrid build() {
         Objects.requireNonNull(maxX, "We need a max x value!");
         Objects.requireNonNull(maxY, "We need a max y value!");
         SwappingGrid swappingGrid;
         setDefaultCollisionDetectionHandlerIfNull();
         if (isNull(minX) || isNull(minY)) {
            swappingGrid = new SwappingGrid(maxX, maxY, collisionDetectionHandler);
         } else {
            swappingGrid = new SwappingGrid(maxX, maxY, minX, minY, collisionDetectionHandler);
         }
         return swappingGrid;
      }

      @Override
      protected SwappingGridBuilder getThis() {
         return this;
      }
   }
}

/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape.position;

import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static com.myownb3.piranha.util.MathUtil.round;

import java.util.List;
import java.util.function.Predicate;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.collision.CollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.detection.AbstractCollisionDetector;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.util.vector.VectorUtil;

/**
 * The {@link PositionCollisionDetectorImpl} implements the {@link CollisionDetector}. It is able to check if a {@link Position} has
 * collided with another {@link Position} on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public class PositionCollisionDetectorImpl extends AbstractCollisionDetector {

   private int collisionDistance;

   private PositionCollisionDetectorImpl(int collisionDistance) {
      super();
      this.collisionDistance = collisionDistance;
   }

   @Override
   public void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement, Position oldPosition,
         Position newPosition, List<GridElement> gridElements2Check) {
      Float64Vector lineFromOldToNew = VectorUtil.getVector(oldPosition.getDirection());
      gridElements2Check.stream()
            .filter(isCollision(oldPosition, newPosition, lineFromOldToNew))
            .forEach(handleCollision(collisionDetectionHandler, newPosition, movedGridElement));
   }

   private Predicate<? super GridElement> isCollision(Position oldPosition, Position newPosition, Float64Vector lineFromOldToNew) {
      return gridElement2Check -> isCollision(oldPosition, newPosition, lineFromOldToNew, gridElement2Check);
   }

   private boolean isCollision(Position oldPosition, Position newPosition, Float64Vector lineFromOldToNew,
         GridElement gridElement) {
      if (hasSameCoordinates(newPosition, gridElement.getPosition())) {
         return true;
      }
      boolean isPositionOnLine = isPositionOnLine(oldPosition, lineFromOldToNew, gridElement);
      boolean isCloseEnoughToOtherGridElement = isCloseEnough(newPosition, gridElement);
      return isPositionOnLine && isCloseEnoughToOtherGridElement;
   }

   private boolean hasSameCoordinates(Position newPosition, Position gridElementPosition) {
      return newPosition.getX() == gridElementPosition.getX() && newPosition.getY() == gridElementPosition.getY();
   }

   private boolean isCloseEnough(Position newPosition, GridElement gridElement) {
      return newPosition.calcDistanceTo(gridElement.getPosition()) <= collisionDistance;
   }

   private static boolean isPositionOnLine(Position oldPosition, Float64Vector lineFromOldToNew, GridElement gridElement) {
      double gridElementDistanceToLine = round(
            calcDistanceFromPositionToLine(gridElement.getPosition(), oldPosition, lineFromOldToNew), 10);
      return gridElementDistanceToLine == 0.0d;
   }

   public static class PositionCollisionDetectorBuilder {

      private int collisionDistance;

      private PositionCollisionDetectorBuilder() {
         super();
         collisionDistance = 2;
      }

      public static PositionCollisionDetectorBuilder builder() {
         return new PositionCollisionDetectorBuilder();
      }

      public PositionCollisionDetectorBuilder withCollisionDistance(int collisionDistance) {
         this.collisionDistance = collisionDistance;
         return this;
      }

      public CollisionDetector build() {
         return new PositionCollisionDetectorImpl(collisionDistance);
      }
   }
}

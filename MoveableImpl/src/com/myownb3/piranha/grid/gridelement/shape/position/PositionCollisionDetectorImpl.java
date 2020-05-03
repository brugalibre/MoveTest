/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape.position;

import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static com.myownb3.piranha.util.MathUtil.round;

import java.util.List;
import java.util.function.Predicate;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.detection.AbstractCollisionDetector;
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
   public void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement gridElement, Position oldPosition,
         Position newPosition, List<Avoidable> allAvoidables) {
      Float64Vector lineFromOldToNew = VectorUtil.getVector(oldPosition.getDirection());
      allAvoidables.stream()
            .filter(isCollision(oldPosition, newPosition, lineFromOldToNew))
            .forEach(handleCollisionWithAvoidable(collisionDetectionHandler, newPosition, gridElement));
   }

   private Predicate<? super Avoidable> isCollision(Position oldPosition, Position newPosition, Float64Vector lineFromOldToNew) {
      return avoidable -> isCollision(oldPosition, newPosition, lineFromOldToNew, avoidable);
   }

   private boolean isCollision(Position oldPosition, Position newPosition, Float64Vector lineFromOldToNew,
         Avoidable avoidable) {
      if (hasSameCoordinates(newPosition, avoidable.getPosition())) {
         return true;
      }
      boolean isPositionOnLine = isPositionOnLine(oldPosition, lineFromOldToNew, avoidable);
      boolean isCloseEnoughToAvoidable = isCloseEnoughToAvoidable(newPosition, avoidable);
      return isPositionOnLine && isCloseEnoughToAvoidable;
   }

   private boolean hasSameCoordinates(Position newPosition, Position avoidablePosition) {
      return newPosition.getX() == avoidablePosition.getX() && newPosition.getY() == avoidablePosition.getY();
   }

   private boolean isCloseEnoughToAvoidable(Position newPosition, Avoidable avoidable) {
      return newPosition.calcDistanceTo(avoidable.getPosition()) <= collisionDistance;
   }

   private static boolean isPositionOnLine(Position oldPosition, Float64Vector lineFromOldToNew, Avoidable avoidable) {
      double avoidableDistanceToLine = round(
            calcDistanceFromPositionToLine(avoidable.getPosition(), oldPosition, lineFromOldToNew), 10);
      return avoidableDistanceToLine == 0.0d;
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

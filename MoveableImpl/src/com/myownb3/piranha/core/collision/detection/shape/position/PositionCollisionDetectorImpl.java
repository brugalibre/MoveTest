/**
 * 
 */
package com.myownb3.piranha.core.collision.detection.shape.position;

import static com.myownb3.piranha.util.MathUtil.round;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionDetector;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.CollisionGridElementImpl;
import com.myownb3.piranha.core.collision.IntersectionImpl;
import com.myownb3.piranha.core.collision.detection.AbstractCollisionDetector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;

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
   public CollisionDetectionResult checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement,
         Position oldPosition, Position newPosition, List<GridElement> gridElements2Check) {
      Float64Vector lineFromOldToNew = oldPosition.getDirection().getVector();
      return gridElements2Check.stream()
            .filter(isCollision(oldPosition, newPosition, lineFromOldToNew))
            .map(buildCollisionGridElement(newPosition))
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                  returnCollisionDetectionResult(collisionDetectionHandler, movedGridElement, newPosition)));
   }

   private Function<GridElement, CollisionGridElement> buildCollisionGridElement(Position newPosition) {
      return gridElement -> {
         PathSegmentImpl pathSegment = new PathSegmentImpl(gridElement.getPosition(), gridElement.getPosition());
         return CollisionGridElementImpl.of(IntersectionImpl.of(pathSegment, newPosition), gridElement);
      };
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
      double gridElementDistanceToLine = round(gridElement.getPosition().calcDistanceToLine(oldPosition, lineFromOldToNew), 10);
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

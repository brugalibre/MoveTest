package com.myownb3.piranha.core.collision.detection.shape.circle;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.AbstractCollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link CircleCollisionDetectorImpl} detects collision within a circle
 * 
 * @author Dominic
 *
 */
public class CircleCollisionDetectorImpl extends AbstractCollisionDetector {

   private Circle circle;

   private CircleCollisionDetectorImpl(Circle circle) {
      super();
      this.circle = circle;
   }

   @Override
   public CollisionDetectionResult checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement,
         Position oldPosition, Position newPosition, List<GridElement> gridElements2Check) {
      Shape ourCircleAtNewPos = getOurShapeAtNewPos(newPosition, circle);
      return gridElements2Check.stream()
            .map(getNearestIntersectionWithGridElement(newPosition, ourCircleAtNewPos))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                  returnCollisionDetectionResult(collisionDetectionHandler, movedGridElement, newPosition)));
   }

   public static class CircleCollisionDetectorBuilder {

      private Circle circle;

      private CircleCollisionDetectorBuilder() {
         super();
      }

      public static CircleCollisionDetectorBuilder builder() {
         return new CircleCollisionDetectorBuilder();
      }

      public CircleCollisionDetectorBuilder withCircle(Circle circle) {
         this.circle = circle;
         return this;
      }

      public CircleCollisionDetectorImpl build() {
         return new CircleCollisionDetectorImpl(circle);
      }
   }

}

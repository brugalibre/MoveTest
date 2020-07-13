package com.myownb3.piranha.core.collision.detection;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link DefaultCollisionDetectorImpl} detects collision within a shape
 * 
 * @author Dominic
 *
 */
public class DefaultCollisionDetectorImpl extends AbstractCollisionDetector {

   private Shape shape;

   private DefaultCollisionDetectorImpl(Shape shape) {
      super();
      this.shape = shape;
   }

   @Override
   public CollisionDetectionResult checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement,
         Position oldPosition, Position newPosition, List<GridElement> gridElements2Check) {
      Shape ourCircleAtNewPos = getOurShapeAtNewPos(newPosition, shape);
      return gridElements2Check.parallelStream()
            .map(getNearestIntersectionWithGridElement(movedGridElement, newPosition, ourCircleAtNewPos))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                  returnCollisionDetectionResult(collisionDetectionHandler, movedGridElement, newPosition)));
   }

   public static class DefaultCollisionDetectorBuilder {

      private Shape shape;

      private DefaultCollisionDetectorBuilder() {
         super();
      }

      public static DefaultCollisionDetectorBuilder builder() {
         return new DefaultCollisionDetectorBuilder();
      }

      public DefaultCollisionDetectorBuilder withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      public DefaultCollisionDetectorImpl build() {
         return new DefaultCollisionDetectorImpl(shape);
      }
   }

}

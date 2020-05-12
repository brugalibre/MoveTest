package com.myownb3.piranha.core.grid.collision.bounce.impl;

import java.util.List;
import java.util.function.Function;

import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.collision.CollisionGridElement;
import com.myownb3.piranha.core.grid.collision.Intersection;
import com.myownb3.piranha.core.grid.collision.bounce.BouncedPositionEvaluator;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

public class BouncingCollisionDetectionHandlerImpl implements CollisionDetectionHandler {

   private BouncedPositionEvaluator evaluator;

   protected BouncingCollisionDetectionHandlerImpl(BouncedPositionEvaluator evaluator) {
      this.evaluator = evaluator;
   }

   @Override
   public CollisionDetectionResult handleCollision(List<CollisionGridElement> collisionGridElements, GridElement movedGridElement,
         Position newPosition) {
      return collisionGridElements.stream()
            .findFirst()
            .map(CollisionGridElement::getIntersection)
            .map(calculateBouncedPosition(movedGridElement))
            .map(Positions::movePositionForward)
            .map(CollisionDetectionResultImpl::new)
            .orElse(new CollisionDetectionResultImpl(newPosition));
   }

   private Function<Intersection, ? extends Position> calculateBouncedPosition(GridElement movedGridElement) {
      return intersection -> {
         PathSegment pathSegment = intersection.getPathSegment();
         return evaluator.calculateBouncedPosition(pathSegment, movedGridElement.getPosition());
      };
   }

   public static class BouncingCollisionDetectionHandlerBuilder {

      private BouncedPositionEvaluator evaluator;

      private BouncingCollisionDetectionHandlerBuilder() {
         // private
      }

      public static BouncingCollisionDetectionHandlerBuilder builder() {
         return new BouncingCollisionDetectionHandlerBuilder();
      }

      public BouncingCollisionDetectionHandlerBuilder withBouncedPositionEvaluator(BouncedPositionEvaluator evaluator) {
         this.evaluator = evaluator;
         return this;
      }

      public BouncingCollisionDetectionHandlerImpl build() {
         return new BouncingCollisionDetectionHandlerImpl(evaluator);
      }
   }
}

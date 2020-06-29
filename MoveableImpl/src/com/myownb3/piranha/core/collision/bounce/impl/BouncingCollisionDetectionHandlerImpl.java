package com.myownb3.piranha.core.collision.bounce.impl;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.Intersection;
import com.myownb3.piranha.core.collision.bounce.BouncedPositionEvaluator;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.collision.detection.handler.CommonCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

public class BouncingCollisionDetectionHandlerImpl extends CommonCollisionDetectionHandlerImpl {

   private BouncedPositionEvaluator evaluator;

   protected BouncingCollisionDetectionHandlerImpl(BouncedPositionEvaluator evaluator) {
      this.evaluator = evaluator;
   }

   @Override
   public CollisionDetectionResult handleCollision(List<CollisionGridElement> collisionGridElements, GridElement movedGridElement,
         Position newPosition) {
      CollisionDetectionResult collisionDetectionResult = super.handleCollision(collisionGridElements, movedGridElement, newPosition);
      return collisionGridElements.stream()
            .filter(isBouncable(movedGridElement))
            .findFirst()
            .map(CollisionGridElement::getIntersection)
            .map(calculateBouncedPosition(movedGridElement))
            .map(Position::movePositionForward)
            .map(CollisionDetectionResultImpl::new)
            .orElse(getDefault(collisionDetectionResult));
   }


   private Predicate<? super CollisionGridElement> isBouncable(GridElement movedGridElement) {
      return colGridElem -> BouncableLookupTable.isBouncable(movedGridElement, colGridElem.getGridElement());

   }

   private Function<Intersection, ? extends Position> calculateBouncedPosition(GridElement movedGridElement) {
      return intersection -> {
         PathSegment pathSegment = intersection.getPathSegment();
         return evaluator.calculateBouncedPosition(pathSegment, movedGridElement.getPosition());
      };
   }

   private static CollisionDetectionResultImpl getDefault(CollisionDetectionResult collisionDetectionResult) {
      return (CollisionDetectionResultImpl) collisionDetectionResult;// ugly cast. I'm not sure, is this because 'CollisionDetectionResultImpl::new' implies there must be a 'CollisionDetectionResultImpl' ? 
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

package com.myownb3.piranha.core.collision.detection;

import static com.myownb3.piranha.core.grid.gridelement.shape.path.PathSeg2Distance.fillupPathSegment2DistanceMap;
import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionDetector;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.CollisionGridElementImpl;
import com.myownb3.piranha.core.collision.Intersection;
import com.myownb3.piranha.core.collision.IntersectionImpl;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.destruction.Destructible;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSeg2Distance;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The base implementation of a {@link CollisionDetector}
 * 
 * 
 * @author Dominic
 *
 */
public abstract class AbstractCollisionDetector implements CollisionDetector {

   private double margin;

   protected AbstractCollisionDetector() {
      margin = 1d / Moveable.STEP_WITDH;
   }

   protected Shape getOurShapeAtNewPos(Position newPosition, Shape shape) {
      Shape transformedShape = shape.clone();
      transformedShape.transform(newPosition);
      return transformedShape;
   }

   protected Function<List<CollisionGridElement>, CollisionDetectionResult> returnCollisionDetectionResult(
         CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement, Position newPosition) {
      return collisionGridElements -> filterDestroyedGridElements(collisionGridElements).isEmpty() ? new CollisionDetectionResultImpl(newPosition)
            : collisionDetectionHandler.handleCollision(filterDestroyedGridElements(collisionGridElements), movedGridElement, newPosition);
   }

   private List<CollisionGridElement> filterDestroyedGridElements(List<CollisionGridElement> collisionGridElements) {
      return collisionGridElements.stream()
            .filter(this::isNotDestroyed)
            .collect(Collectors.toList());
   }

   @Visible4Testing
   boolean isNotDestroyed(CollisionGridElement gridElement) {
      return gridElement.getGridElement() instanceof Destructible ? !((Destructible) gridElement.getGridElement()).isDestroyed() : true;
   }

   protected Function<GridElement, Optional<CollisionGridElement>> getNearestIntersectionWithGridElement(Position newPosition,
         Shape ourShapeAtNewPos) {
      return gridElement -> {
         Shape shape2Check = gridElement.getShape();
         return shape2Check.getPath()
               .stream()
               .map(pathSeg2Check -> getNearestIntersectionWithPathSegment(pathSeg2Check, ourShapeAtNewPos, gridElement, newPosition))
               .filter(Objects::nonNull)
               .findAny();
      };
   }

   protected Function<Position, CollisionGridElement> createCollisionGridElement(PathSegment pathSegment2Check,
         GridElement gridElement, Position newPosition) {
      return pathSegPos -> {
         // We need to turn the collision-Position into the same direction then the shape is heading
         double angle = pathSegPos.calcAngleRelativeTo(newPosition);
         Intersection intersection = createIntersection(pathSegment2Check, pathSegPos.rotate(angle));
         return CollisionGridElementImpl.of(intersection, gridElement);
      };
   }

   private CollisionGridElement getNearestIntersectionWithPathSegment(PathSegment pathSegment2Check, Shape ourShapeAtNewPos,
         GridElement gridElement, Position newPosition) {
      List<PathSeg2Distance> pathSegments2Distances = fillupPathSegment2DistanceMap(pathSegment2Check.getBegin(), ourShapeAtNewPos.getPath());
      return pathSegments2Distances.stream()
            .sorted(Comparator.comparing(PathSeg2Distance::getDistance))
            .map(PathSeg2Distance::getPathSegment)
            .map(PathSegment::getBegin)
            .filter(pathSegPos -> isPositionInsideOrOnShape(pathSegment2Check, pathSegPos))
            .findAny()
            .map(createCollisionGridElement(pathSegment2Check, gridElement, newPosition))
            .orElse(null);
   }

   private boolean isPositionInsideOrOnShape(PathSegment pathSegment2Check, Position pathPosition) {
      double distanceFromForemostPosToPathSegment =
            calcDistanceFromPositionToLine(pathPosition, pathSegment2Check.getBegin(), pathSegment2Check.getVector());
      if (isPositionInBetweenBeginAndEndOfSegment(pathPosition, pathSegment2Check)) {
         return hasReachedPathSegment(distanceFromForemostPosToPathSegment);
      }
      return false;
   }

   private boolean hasReachedPathSegment(double distanceFromCenterToPathSegment) {
      return distanceFromCenterToPathSegment - margin < 0;
   }

   /**
    * 
    * Verifies if the center is between the begin and end-Position of the segment: 
    * 
    * @formatter:off
    * Segment           |-------------|
    * Center                  x
    * 
    * Return true
    * 
    *                   |             |
    * Segment           |-------------|
    * Center            |             |  x
    *    or             |             |
    * Center        x   |             |                  
    * 
    * Return false
    * @formatter:on
    * 
    * @param newPosition the new evaluated {@link Position} by the {@link Grid} 
    * @param pathSegment the current {@link PathSegment}
    * @return <code>true</code> if the new {@link Position} is in between the given {@link PathSegment} 
    */
   private static boolean isPositionInBetweenBeginAndEndOfSegment(Position newPosition, PathSegment pathSegment) {
      double pathSegLenght = pathSegment.getBegin().calcDistanceTo(pathSegment.getEnd());
      double center2PathSegBegin = newPosition.calcDistanceTo(pathSegment.getBegin());
      double center2PathSegEndBegin = newPosition.calcDistanceTo(pathSegment.getEnd());
      return center2PathSegBegin <= pathSegLenght && pathSegLenght >= center2PathSegEndBegin;
   }

   private static Intersection createIntersection(PathSegment pathSegment2Check, Position collisionPosition) {
      return IntersectionImpl.of(pathSegment2Check, collisionPosition);
   }
}

package com.myownb3.piranha.core.collision.detection;

import static com.myownb3.piranha.core.grid.gridelement.shape.path.PathSeg2Distance.fillupPathSegment2DistanceMap;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionDetector;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.CollisionGridElementImpl;
import com.myownb3.piranha.core.collision.Intersection;
import com.myownb3.piranha.core.collision.IntersectionImpl;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSeg2Distance;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.MoveableConst;

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
      margin = 1d / MoveableConst.STEP_WITDH;
   }

   protected Shape getOurShapeAtNewPos(Position newPosition, Shape shape) {
      Shape transformedShape = shape.clone();
      transformedShape.transform(newPosition);
      return transformedShape;
   }

   protected Function<List<CollisionGridElement>, CollisionDetectionResult> returnCollisionDetectionResult(
         CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement, Position newPosition) {
      return collisionGridElements -> collisionGridElements.isEmpty() ? new CollisionDetectionResultImpl(newPosition)
            : collisionDetectionHandler.handleCollision(collisionGridElements, movedGridElement, newPosition);
   }

   protected Function<GridElement, Optional<CollisionGridElement>> getNearestIntersectionWithGridElement(GridElement movedGridElement,
         Position newPosition, Shape movedShapeAtNewPos) {
      return gridElement2Check -> gridElement2Check.getPath(movedGridElement)
            .parallelStream()
            .map(pathSeg2Check -> getNearestIntersectionWithPathSegment(pathSeg2Check, movedShapeAtNewPos, gridElement2Check, newPosition))
            .filter(Objects::nonNull)
            .findAny();
   }

   private Function<Position, CollisionGridElement> createCollisionGridElement(PathSegment pathSegment2Check,
         GridElement gridElement, Position newPosition) {
      return movedPathSegmentPos -> {
         // We need to turn the collision-Position into the same direction then the shape is heading
         double angle = movedPathSegmentPos.calcAngleRelativeTo(newPosition);
         Intersection intersection = createIntersection(pathSegment2Check, movedPathSegmentPos.rotate(angle));
         return CollisionGridElementImpl.of(intersection, gridElement);
      };
   }

   private CollisionGridElement getNearestIntersectionWithPathSegment(PathSegment pathSegment2Check, Shape movedShapeAtNewPos,
         GridElement gridElement2Check, Position newPosition) {
      List<PathSeg2Distance> pathSegments2Distances = fillupPathSegment2DistanceMap(pathSegment2Check.getBegin(), movedShapeAtNewPos.getPath());
      return pathSegments2Distances.stream()
            .sorted(Comparator.comparing(PathSeg2Distance::getDistance))
            .map(PathSeg2Distance::getPathSegment)
            .map(PathSegment::getBegin)
            .filter(movedPathPosition -> hasMovedSegmentIntersectionWithOther(pathSegment2Check, movedPathPosition))
            .findAny()
            .map(createCollisionGridElement(pathSegment2Check, gridElement2Check, newPosition))
            .orElse(null);
   }

   private boolean hasMovedSegmentIntersectionWithOther(PathSegment pathSegment2Check, Position movedPathPosition) {
      if (isPositionInBetweenBeginAndEndOfSegment2Check(movedPathPosition, pathSegment2Check)) {
         return hasReachedPathSegment(pathSegment2Check, movedPathPosition);
      }
      return false;
   }

   private boolean hasReachedPathSegment(PathSegment pathSegment2Check, Position movedPathPosition) {
      double distanceFromCenterToPathSegment =
            movedPathPosition.calcDistanceToLine(pathSegment2Check.getBegin(), pathSegment2Check.getVector());
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
    * @param beginPosOfMovedPathSegment
    *        the {@link Position} at the begin of the current {@link PathSegment} of the {@link Shape} which is currently checking for
    *        collision
    * @param pathSegment2Check
    *        the {@link PathSegment} of another {@link Shape} which may have collided with us
    * @return <code>true</code> if the new {@link Position} is in between the given {@link PathSegment}
    */
   private static boolean isPositionInBetweenBeginAndEndOfSegment2Check(Position beginPosOfMovedPathSegment, PathSegment pathSegment2Check) {
      double pathSegLenght = pathSegment2Check.getBegin().calcDistanceTo(pathSegment2Check.getEnd());

      double center2PathSegBegin = beginPosOfMovedPathSegment.calcDistanceTo(pathSegment2Check.getBegin());
      double center2PathSegEndBegin = beginPosOfMovedPathSegment.calcDistanceTo(pathSegment2Check.getEnd());
      return center2PathSegBegin <= pathSegLenght && pathSegLenght >= center2PathSegEndBegin;
   }

   private static Intersection createIntersection(PathSegment pathSegment2Check, Position collisionPosition) {
      return IntersectionImpl.of(pathSegment2Check, collisionPosition);
   }
}

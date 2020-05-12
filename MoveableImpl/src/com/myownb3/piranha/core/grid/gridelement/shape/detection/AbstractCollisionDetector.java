package com.myownb3.piranha.core.grid.gridelement.shape.detection;

import static com.myownb3.piranha.core.grid.gridelement.shape.detection.PathSeg2Distance.fillupPathSegment2DistanceMap;
import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.collision.CollisionDetector;
import com.myownb3.piranha.core.grid.collision.Intersection;
import com.myownb3.piranha.core.grid.collision.IntersectionImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The every implementation of a {@link CollisionDetectionHandler} should extend from this most basic {@link AbstractCollisionDetector}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractCollisionDetector implements CollisionDetector {

   private double margin;

   protected AbstractCollisionDetector() {
      margin = 1d / Moveable.STEP_WITDH;
   }

   protected Function<? super GridElement, CollisionDetectionResult> handleCollision(CollisionDetectionHandler collisionDetectionHandler,
         Position newPosition, GridElement movedGridElement) {
      return otherGridElement -> collisionDetectionHandler.handleCollision(otherGridElement, movedGridElement, newPosition);
   }

   protected Predicate<? super GridElement> isGridElementsInsideOrOnShape(Position newPosition, Shape ourShapeAtNewPos) {
      return gridElement -> {
         Shape shape2Check = gridElement.getShape();
         return shape2Check.getPath()
               .stream()
               .anyMatch(hasIntersectionWithPathSegment(ourShapeAtNewPos));
      };
   }

   private Predicate<? super PathSegment> hasIntersectionWithPathSegment(Shape ourShapeAtNewPos) {
      return pathSegment2Check -> getNearestIntersectionWithPathSegment(pathSegment2Check, ourShapeAtNewPos) != null;
   }

   private Intersection getNearestIntersectionWithPathSegment(PathSegment pathSegment2Check, Shape ourShapeAtNewPos) {
      List<PathSeg2Distance> pathSegments2Distances = fillupPathSegment2DistanceMap(pathSegment2Check.getBegin(), ourShapeAtNewPos.getPath());
      return pathSegments2Distances.stream()
            .sorted(Comparator.comparing(PathSeg2Distance::getDistance))
            .map(PathSeg2Distance::getPathSegment)
            .map(PathSegment::getBegin)
            .filter(pathSegPos -> isPositionInsideOrOnShape(pathSegment2Check, pathSegPos))
            .findAny()
            .map(pathSeg -> createIntersection(pathSegment2Check, pathSeg))
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

package com.myownb3.piranha.core.grid.gridelement.shape.circle.detection;

import java.util.List;
import java.util.function.Predicate;

import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.detection.AbstractCollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link CircleCollisionDetectorImpl} detects collision within a circle
 * 
 * @author Dominic
 *
 */
public class CircleCollisionDetectorImpl extends AbstractCollisionDetector {

   private Circle circle;

   public CircleCollisionDetectorImpl(Circle circle) {
      this.circle = circle;
   }

   @Override
   public void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement, Position oldPosition,
         Position newPosition, List<GridElement> gridElements2Check) {
      gridElements2Check.stream()
            .filter(isGridElementsInsideOrOnTheCircle(newPosition))
            .forEach(handleCollision(collisionDetectionHandler, newPosition, movedGridElement));
   }

   private Predicate<? super GridElement> isGridElementsInsideOrOnTheCircle(Position newPosition) {
      return gridElement -> {
         Shape shape2Check = gridElement.getShape();
         return isCircle(shape2Check) ? hasCircleIntersection((Circle) shape2Check, newPosition)
               : shape2Check.getPath()
                     .stream()
                     .map(PathSegment::getBegin)
                     .anyMatch(posOnShapePath -> isPositionInsideOrOnTheCircle(posOnShapePath, newPosition));
      };
   }

   private boolean hasCircleIntersection(Circle otherCircle, Position newPosition) {
      double distanceFromCenter2Center = otherCircle.getCenter().calcDistanceTo(newPosition);
      return distanceFromCenter2Center <= otherCircle.getRadius() + circle.getRadius();
   }

   private static boolean isCircle(Shape shape) {
      return shape instanceof Circle;
   }

   private boolean isPositionInsideOrOnTheCircle(Position posOnShapePath, Position newCenter) {
      double distanceFromCenterToGridElementPos = newCenter.calcDistanceTo(posOnShapePath);
      return distanceFromCenterToGridElementPos <= circle.getRadius();
   }
}

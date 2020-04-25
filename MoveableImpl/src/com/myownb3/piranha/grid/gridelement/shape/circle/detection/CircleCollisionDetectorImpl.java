package com.myownb3.piranha.grid.gridelement.shape.circle.detection;

import java.util.List;
import java.util.function.Predicate;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.detection.AbstractCollisionDetector;

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
   public void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement gridElement, Position oldPosition,
         Position newPosition, List<Avoidable> allAvoidables) {
      allAvoidables.stream()
            .filter(isAvoidableInsideOrOnTheCircle(newPosition))
            .forEach(handleCollisionWithAvoidable(collisionDetectionHandler, newPosition, gridElement));
   }

   private Predicate<? super Avoidable> isAvoidableInsideOrOnTheCircle(Position newPosition) {
      return avoidable -> avoidable.getShape()
            .getPath()
            .stream()
            .anyMatch(posOnAvoidablePath -> isPositionInsideOrOnTheCircle(posOnAvoidablePath, newPosition));
   }

   private boolean isPositionInsideOrOnTheCircle(Position posOnAvoidablePath, Position newCenter) {
      double distanceFromCenterToAvoidablePos = newCenter.calcDistanceTo(posOnAvoidablePath);
      return distanceFromCenterToAvoidablePos <= circle.getRadius();
   }
}

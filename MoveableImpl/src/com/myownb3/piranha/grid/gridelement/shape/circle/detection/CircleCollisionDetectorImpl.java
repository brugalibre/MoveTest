package com.myownb3.piranha.grid.gridelement.shape.circle.detection;

import java.util.List;
import java.util.function.Consumer;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.circle.Circle;

/**
 * A {@link CircleCollisionDetectorImpl} detects collision within a circle
 * 
 * @author Dominic
 *
 */
public class CircleCollisionDetectorImpl implements CollisionDetector {

   private Circle circle;

   public CircleCollisionDetectorImpl(Circle circle) {
      this.circle = circle;
   }

   @Override
   public void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement gridElement, Position oldPosition,
         Position newPosition, List<Avoidable> allAvoidables) {
      allAvoidables.forEach(checkCollisionWithAvoidable(collisionDetectionHandler, gridElement, newPosition));
   }

   private Consumer<? super Avoidable> checkCollisionWithAvoidable(CollisionDetectionHandler collisionDetectionHandler, GridElement gridElement,
         Position newPosition) {
      return avoidable -> {
         boolean isCollision = isAvoidableInsideOrOnTheCircle(avoidable, newPosition);
         if (isCollision) {
            collisionDetectionHandler.handleCollision(avoidable, gridElement, newPosition);
         }
      };
   }

   private boolean isAvoidableInsideOrOnTheCircle(Avoidable avoidable, Position newPosition) {
      return avoidable.getShape()
            .getPath()
            .stream()
            .anyMatch(posOnAvoidablePath -> isPositionInsideOrOnTheCircle(posOnAvoidablePath, newPosition));
   }

   private boolean isPositionInsideOrOnTheCircle(Position posOnAvoidablePath, Position newCenter) {
      double distanceFromCenterToAvoidablePos = newCenter.calcDistanceTo(posOnAvoidablePath);
      return distanceFromCenterToAvoidablePos <= circle.getRadius();
   }
}

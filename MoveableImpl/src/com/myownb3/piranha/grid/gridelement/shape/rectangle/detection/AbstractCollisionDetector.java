package com.myownb3.piranha.grid.gridelement.shape.rectangle.detection;

import java.util.function.Consumer;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;

public abstract class AbstractCollisionDetector implements CollisionDetector {

   protected Consumer<? super Avoidable> handleCollisionWithAvoidable(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         GridElement gridElement) {
      return avoidable -> collisionDetectionHandler.handleCollision(avoidable, gridElement, newPosition);
   }
}

package com.myownb3.piranha.core.grid.gridelement.shape.detection;

import java.util.function.Consumer;

import com.myownb3.piranha.core.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.detector.collision.CollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.Avoidable;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public abstract class AbstractCollisionDetector implements CollisionDetector {

   protected Consumer<? super Avoidable> handleCollisionWithAvoidable(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         GridElement gridElement) {
      return avoidable -> collisionDetectionHandler.handleCollision(avoidable, gridElement, newPosition);
   }
}

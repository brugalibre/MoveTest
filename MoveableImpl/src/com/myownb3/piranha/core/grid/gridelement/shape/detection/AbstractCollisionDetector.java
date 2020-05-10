package com.myownb3.piranha.core.grid.gridelement.shape.detection;

import java.util.function.Consumer;

import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.collision.CollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public abstract class AbstractCollisionDetector implements CollisionDetector {

   protected Consumer<? super GridElement> handleCollision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         GridElement movedGridElement) {
      return otherGridElement -> collisionDetectionHandler.handleCollision(otherGridElement, movedGridElement, newPosition);
   }
}

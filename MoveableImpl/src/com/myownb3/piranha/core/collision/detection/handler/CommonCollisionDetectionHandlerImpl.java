package com.myownb3.piranha.core.collision.detection.handler;

import static java.util.Collections.singletonList;

import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class CommonCollisionDetectionHandlerImpl implements CollisionDetectionHandler {

   @Override
   public CollisionDetectionResult handleCollision(List<CollisionGridElement> otherGridElements, GridElement movedGridElement, Position newPosition) {
      handleCollisionOnMovedGridElement(otherGridElements, movedGridElement);
      return new CollisionDetectionResultImpl(newPosition);
   }

   private void handleCollisionOnMovedGridElement(List<CollisionGridElement> otherGridElements, GridElement movedGridElement) {
      otherGridElements.stream()
            .map(CollisionGridElement::getGridElement)
            .forEach(gridElement -> gridElement.onCollision(singletonList(movedGridElement)));
   }
}

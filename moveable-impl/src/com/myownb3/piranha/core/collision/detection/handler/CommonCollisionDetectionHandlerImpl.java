package com.myownb3.piranha.core.collision.detection.handler;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class CommonCollisionDetectionHandlerImpl implements CollisionDetectionHandler {

   @Override
   public CollisionDetectionResult handleCollision(List<CollisionGridElement> otherCollisionGridElements, GridElement movedGridElement,
         Position newPosition) {
      handleCollisionOnMovedGridElement(otherCollisionGridElements, movedGridElement);
      return new CollisionDetectionResultImpl(true, movedGridElement.getPosition());
   }

   private void handleCollisionOnMovedGridElement(List<CollisionGridElement> otherCollisionGridElements, GridElement movedGridElement) {
      map2GridElements(otherCollisionGridElements)
            .forEach(gridElement -> gridElement.onCollision(singletonList(movedGridElement)));
      movedGridElement.onCollision(map2GridElements(otherCollisionGridElements));
   }

   private static List<GridElement> map2GridElements(List<CollisionGridElement> otherCollisionGridElements) {
      return otherCollisionGridElements.stream()
            .map(CollisionGridElement::getGridElement)
            .collect(Collectors.toList());
   }
}

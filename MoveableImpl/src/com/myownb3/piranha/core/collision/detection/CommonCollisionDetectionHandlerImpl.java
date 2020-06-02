package com.myownb3.piranha.core.collision.detection;

import java.util.List;
import java.util.stream.Stream;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.CollisionSensitiveGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class CommonCollisionDetectionHandlerImpl implements CollisionDetectionHandler {

   @Override
   public CollisionDetectionResult handleCollision(List<CollisionGridElement> otherGridElements, GridElement movedGridElement, Position newPosition) {
      Stream<GridElement> gridElementStream = getGridElementStream(otherGridElements);
      Stream.concat(gridElementStream, Stream.of(movedGridElement))
            .filter(CollisionSensitiveGridElement.class::isInstance)
            .map(CollisionSensitiveGridElement.class::cast)
            .forEach(CollisionSensitiveGridElement::onCollision);
      return new CollisionDetectionResultImpl(newPosition);
   }

   private static Stream<GridElement> getGridElementStream(List<CollisionGridElement> otherGridElements) {
      return otherGridElements.stream()
            .map(CollisionGridElement::getGridElement);
   }

}

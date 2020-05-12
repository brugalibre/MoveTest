package com.myownb3.piranha.core.grid.collision;

import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class DefaultCollisionDetectionHandlerImpl implements CollisionDetectionHandler {

   @Override
   public CollisionDetectionResult handleCollision(List<CollisionGridElement> otherGridElements, GridElement movedGridElement, Position newPosition) {
      String gridElementPosRepr = getGridElementPosRepresentation(otherGridElements);
      throw new CollisionDetectedException("Collision with GridElement(s) '" + gridElementPosRepr
            + "' and the moved GridElement '" + movedGridElement + "', on Position x='" + newPosition.getX() + "', y='" + newPosition.getY() + "'");
   }

   private String getGridElementPosRepresentation(List<CollisionGridElement> otherGridElements) {
      return otherGridElements.stream()
            .map(CollisionGridElement::getGridElement)
            .map(GridElement::getPosition)
            .map(Position::toString)
            .collect(Collectors.joining("\n"));
   }
}

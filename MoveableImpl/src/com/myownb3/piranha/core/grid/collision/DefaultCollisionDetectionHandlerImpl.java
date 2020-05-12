package com.myownb3.piranha.core.grid.collision;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class DefaultCollisionDetectionHandlerImpl implements CollisionDetectionHandler {

   @Override
   public CollisionDetectionResult handleCollision(GridElement otherGridElement, GridElement movedGridElement, Position newPosition) {
      throw new CollisionDetectedException("Collision with GridElement '" + otherGridElement.getPosition()
            + "' and the moved GridElement '" + movedGridElement + "', on Position x='" + newPosition.getX() + "', y='" + newPosition.getY() + "'");
   }

}

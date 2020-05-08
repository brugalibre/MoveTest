package com.myownb3.piranha.core.detector.collision;

import com.myownb3.piranha.core.grid.gridelement.Avoidable;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class DefaultCollisionDetectionHandlerImpl implements CollisionDetectionHandler {

   @Override
   public void handleCollision(Avoidable avoidable, GridElement gridElement, Position newPosition) {
      throw new CollisionDetectedException("Collision with Avoidable '" + avoidable.getPosition()
            + "' and GridElement '" + gridElement + "', on Position x='" + newPosition.getX() + "', y='" + newPosition.getY() + "'");
   }

}

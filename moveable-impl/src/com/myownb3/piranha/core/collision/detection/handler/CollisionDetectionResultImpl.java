package com.myownb3.piranha.core.collision.detection.handler;

import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.position.Position;

public class CollisionDetectionResultImpl implements CollisionDetectionResult {

   private Position movedPosition;
   private boolean isCollision;

   public CollisionDetectionResultImpl(Position movedPosition) {
      this(false, movedPosition);
   }

   public CollisionDetectionResultImpl(boolean isCollision, Position movedPosition) {
      this.isCollision = isCollision;
      this.movedPosition = movedPosition;
   }

   @Override
   public Position getMovedPosition() {
      return movedPosition;
   }

   @Override
   public boolean isCollision() {
      return isCollision;
   }
}

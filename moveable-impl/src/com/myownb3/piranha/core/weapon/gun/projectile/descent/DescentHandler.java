package com.myownb3.piranha.core.weapon.gun.projectile.descent;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;

/**
 * The {@link DescentHandler} is responsible for handling a descent of a {@link Projectile}
 * 
 * @author Dominic
 *
 */
public class DescentHandler {

   private double targetHeight;
   private double distanceBeforeDescent;
   private Position startPosition;

   public DescentHandler(Position startPos, double distanceBeforeDescent, double targetHeight) {
      this.distanceBeforeDescent = distanceBeforeDescent;
      this.targetHeight = targetHeight;
      this.startPosition = Positions.of(startPos);
   }

   public Position evlPositionForNewHeight(Position currentPosition) {
      if (hasNotReachedTargetHeight(currentPosition)
            && isFarEnoughFromStartToSink(currentPosition, startPosition)) {
         return Positions.of(currentPosition.getDirection(), currentPosition.getX(), currentPosition.getY(), 0);
      }
      return currentPosition;
   }

   private boolean isFarEnoughFromStartToSink(Position currentPosition, Position startPosition) {
      double distanceToStart = currentPosition.calcDistanceTo(startPosition);
      return distanceToStart >= distanceBeforeDescent;
   }

   private boolean hasNotReachedTargetHeight(Position currentPosition) {
      return currentPosition.getZ() > targetHeight;
   }
}

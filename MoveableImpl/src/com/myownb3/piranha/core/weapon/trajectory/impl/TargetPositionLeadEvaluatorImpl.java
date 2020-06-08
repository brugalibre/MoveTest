package com.myownb3.piranha.core.weapon.trajectory.impl;

import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TargetGridElement;

public class TargetPositionLeadEvaluatorImpl implements TargetPositionLeadEvaluator {

   private double projectilVelocity;

   public TargetPositionLeadEvaluatorImpl(double projectilVelocity) {
      this.projectilVelocity = projectilVelocity;
   }

   @Override
   public Position calculateTargetConsideringLead(TargetGridElement targetGridElement, Position turretPos) {
      Position targetPosition = targetGridElement.getCurrentGridElementPosition();
      // Make sure the GridElement has moved. If not -> lets assume it's static and return it's current / initial Position
      if (!targetGridElement.isMoving()) {
         return targetPosition;
      }
      return calculateTargetConsideringLead(turretPos, targetPosition);
   }

   private Position calculateTargetConsideringLead(Position turretPos, Position targetPosition) {
      double distanceFromTurret2Target = calculateDistanceFromTurret2Target(targetPosition, turretPos);
      Position projectStartPos = getProjectileStartPosConsideringVelocity(turretPos);
      int cyclesNeededToReachTarget = calculateCyclesUntilProjectileReachesTarget(targetPosition, projectStartPos, distanceFromTurret2Target);
      return calcEstimatedTargetPosWithinCycles(targetPosition, cyclesNeededToReachTarget);
   }

   private static double calculateDistanceFromTurret2Target(Position acquiredPosition, Position turretPos) {
      return acquiredPosition.calcDistanceTo(turretPos);
   }

   private Position getProjectileStartPosConsideringVelocity(Position turretPos) {
      Direction turretDirection = turretPos.getDirection();
      Direction projectileVelocity =
            Directions.of(turretDirection.getForwardX() * projectilVelocity, turretDirection.getForwardY() * projectilVelocity);
      return Positions.of(projectileVelocity, turretPos.getX(), turretPos.getY());
   }

   /*
    * 2. With the velocity from the projectile and the actual distance to the target, we have to calculate the amount of time it needs the projectile to get there. 
    * The time is calculated in "cycles"
    */
   private int calculateCyclesUntilProjectileReachesTarget(Position acquiredPosition, Position projectStartPos, double distanceFromTurret2Target) {
      Position projectilePosAfterOneCycle = Positions.movePositionForward(projectStartPos);
      double coveredProjectileDistanceInOneCicle = calculateDistanceFromTurret2Target(projectStartPos, projectilePosAfterOneCycle);
      return (int) Math.ceil(distanceFromTurret2Target / coveredProjectileDistanceInOneCicle);
   }

   /*
     * Now we know how long it takes the projectile to reach it's target. With this we can estimate, how far the target comes when
     *  it moves forward with it's current speed for the calculated amount of time
     *  
   *  -> The Position where the target may reach in this time is the actual target we have to aim
     */
   private Position calcEstimatedTargetPosWithinCycles(Position acquiredPosition, int cyclesNeededToReachTarget) {
      return Positions.movePositionForward(acquiredPosition, cyclesNeededToReachTarget);
   }
}

package com.myownb3.piranha.core.weapon.trajectory.impl;

import static com.myownb3.piranha.core.weapon.gun.AbstractGun.createProjectilStartPos;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TargetGridElement;

public class TargetPositionLeadEvaluatorImpl implements TargetPositionLeadEvaluator {

   private ProjectileConfig projectileConfig;

   public TargetPositionLeadEvaluatorImpl(ProjectileConfig projectileConfig) {
      this.projectileConfig = projectileConfig;
   }

   @Override
   public Position calculateTargetConsideringLead(TargetGridElement targetGridElement, Position turretPos) {
      Position targetPosition = targetGridElement.getCurrentGridElementPosition();
      // Make sure the GridElement has moved. If not -> lets assume it's static and return it's current / initial Position
      if (!targetGridElement.isMoving()) {
         return targetPosition;
      }
      Position calculateTargetConsideringLead = calculateTargetConsideringLead(turretPos, targetGridElement);
      return calculateTargetConsideringLead;
   }

   private Position calculateTargetConsideringLead(Position turretPos, TargetGridElement targetGridElement) {
      Position targetPosition = targetGridElement.getCurrentGridElementPosition();
      double calcAngleRelativeTo = targetPosition.calcAngleRelativeTo(targetPosition);
      Position projectStartPos = createProjectilStartPos(turretPos, projectileConfig).rotate(calcAngleRelativeTo);

      double distanceFromTurret2Target = projectStartPos.calcDistanceTo(targetPosition);
      int cyclesNeededToReachTarget = calculateCyclesUntilProjectileReachesTarget(projectStartPos, distanceFromTurret2Target);
      return calcEstimatedTargetPosWithinCycles(targetGridElement, cyclesNeededToReachTarget);
   }

   /*
    * 2. With the velocity from the projectile and the actual distance to the target, we have to calculate the amount of time it needs the projectile to get there. 
    * The time is calculated in "cycles"
    */
   private int calculateCyclesUntilProjectileReachesTarget(Position projectStartPos, double distanceFromTurret2Target) {
      Position projectilePosAfterOneCycle = Positions.movePositionForward(projectStartPos, projectileConfig.getVelocity());
      double coveredProjectileDistanceInOneCicle = projectStartPos.calcDistanceTo(projectilePosAfterOneCycle);
      return (int) Math.ceil(distanceFromTurret2Target / coveredProjectileDistanceInOneCicle);
   }

   /*
     * Now we know how long it takes the projectile to reach it's target. With this we can estimate, how far the target comes when
     *  it moves forward with it's current speed for the calculated amount of time
     *  
   *  -> The Position where the target may reach in this time is the actual target we have to aim
     */
   private Position calcEstimatedTargetPosWithinCycles(TargetGridElement targetGridElement, int cyclesNeededToReachTarget) {
      Position targetPosition = targetGridElement.getCurrentGridElementPosition();
      int targetVelocity = targetGridElement.getTargetVelocity();
      return Positions.movePositionForward(targetPosition, (int) (cyclesNeededToReachTarget / targetVelocity));
   }
}

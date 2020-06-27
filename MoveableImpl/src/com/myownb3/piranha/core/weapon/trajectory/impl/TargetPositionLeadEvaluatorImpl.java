package com.myownb3.piranha.core.weapon.trajectory.impl;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.target.TargetGridElement;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;

public class TargetPositionLeadEvaluatorImpl implements TargetPositionLeadEvaluator {

   private ProjectileConfig projectileConfig;

   public TargetPositionLeadEvaluatorImpl(ProjectileConfig projectileConfig) {
      this.projectileConfig = projectileConfig;
   }

   @Override
   public Position calculateTargetConsideringLead(TargetGridElement targetGridElement, Position sourcePos) {
      Position targetPosition = targetGridElement.getCurrentGridElementPosition();
      // Make sure the GridElement has moved. If not -> lets assume it's static and return it's current / initial Position
      if (!targetGridElement.isMoving()) {
         return targetPosition;
      }
      return computeTargetConsideringLead(sourcePos, targetGridElement);
   }

   /*
    * Lets compute the target lead using the formula dt=-B +- sqrt(B^2 - 4*A*C)
    * Thanks to duhprey (http://www.tosos.com/pages/calculating-a-lead-on-a-target/)
    */
   private Position computeTargetConsideringLead(Position sourcePos, TargetGridElement targetGridElement) {

      // Target-Position & velocity-Vector
      Position targetPosition = targetGridElement.getCurrentGridElementPosition();
      Direction targetPosDirection = Directions.of(targetPosition.getDirection(), targetGridElement.getTargetVelocity());

      // Source-Position & velocity-Vector
      double angleBetweenSourceAndTarget = sourcePos.calcAngleRelativeTo(targetPosition);
      sourcePos = sourcePos.rotate(angleBetweenSourceAndTarget);
      Direction sourcePosDirection = Directions.of(sourcePos.getDirection(), projectileConfig.getVelocity());

      Float64Vector targetVMinusSourceV = targetPosition.getVector().minus(sourcePos.getVector());
      double A = targetPosDirection.getVector().normValue() * targetPosDirection.getVector().normValue()
            - (sourcePosDirection.getVector().normValue() * sourcePosDirection.getVector().normValue());
      double B = 2 * targetVMinusSourceV.times(targetPosDirection.getVector()).doubleValue();
      double C = targetVMinusSourceV.normValue() * targetVMinusSourceV.normValue();

      if (A >= 0) {
         return targetPosition;
      }
      double sqrt = Math.sqrt(B * B - 4 * A * C);
      double dt1 = solveQuadraticFormula(A, B, sqrt, -1);
      double dt = computeDeltaT(A, B, sqrt, dt1);
      Float64Vector targetDirVectorWithTime = targetPosDirection.getVector().times(dt);
      return Positions.of(targetPosition.getX() + targetDirVectorWithTime.getValue(0), targetPosition.getY() + targetDirVectorWithTime.getValue(1),
            sourcePos.getZ());
   }

   @Visible4Testing
   double computeDeltaT(double A, double B, double sqrt, double dt1) {
      return dt1 < 0 ? solveQuadraticFormula(A, B, sqrt, 1) : dt1;
   }

   private static double solveQuadraticFormula(double A, double B, double sqrt, int plusOrMinus) {
      return (-B + plusOrMinus * sqrt) / (2 * A);
   }

}

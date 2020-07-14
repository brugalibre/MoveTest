package com.myownb3.piranha.core.battle.weapon.trajectory.impl;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElement;
import com.myownb3.piranha.core.battle.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;

public class TargetPositionLeadEvaluatorImpl implements TargetPositionLeadEvaluator {

   private int velocity;

   public TargetPositionLeadEvaluatorImpl(int velocity) {
      this.velocity = velocity;
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
      Direction sourcePosDirection = Directions.of(sourcePos.getDirection(), velocity);

      Float64Vector targetVMinusSourceV = targetPosition.getVector().minus(sourcePos.getVector());
      double a = targetPosDirection.getVector().normValue() * targetPosDirection.getVector().normValue()
            - (sourcePosDirection.getVector().normValue() * sourcePosDirection.getVector().normValue());
      double b = 2 * targetVMinusSourceV.times(targetPosDirection.getVector()).doubleValue();
      double c = targetVMinusSourceV.normValue() * targetVMinusSourceV.normValue();

      if (a >= 0) {
         return targetPosition;
      }
      double sqrt = Math.sqrt(b * b - 4 * a * c);
      double dt1 = solveQuadraticFormula(a, b, sqrt, -1);
      double dt = computeDeltaT(a, b, sqrt, dt1);
      Float64Vector targetDirVectorWithTime = targetPosDirection.getVector().times(dt);
      return Positions.of(targetPosition.getX() + targetDirVectorWithTime.getValue(0), targetPosition.getY() + targetDirVectorWithTime.getValue(1),
            sourcePos.getZ());
   }

   @Visible4Testing
   double computeDeltaT(double a, double b, double sqrt, double dt1) {
      return dt1 < 0 ? solveQuadraticFormula(a, b, sqrt, 1) : dt1;
   }

   private static double solveQuadraticFormula(double a, double b, double sqrt, int plusOrMinus) {
      return (-b + plusOrMinus * sqrt) / (2 * a);
   }

}

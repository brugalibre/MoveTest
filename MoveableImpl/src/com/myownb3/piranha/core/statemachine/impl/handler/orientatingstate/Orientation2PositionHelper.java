package com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate;

import static com.myownb3.piranha.util.MathUtil.round;
import static java.lang.Math.abs;
import static java.util.Objects.nonNull;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.util.MathUtil;

/**
 * The {@link Orientation2PositionHelper} contains helper methods which helps a {@link Moveable} to orient it's {@link Position}
 * compared with another {@link Position}
 * 
 * @author Dominic
 *
 */
public class Orientation2PositionHelper {

   private static final int ANGLE_DECIMALS = 1;

   /**
    * Returns <code>true</code> if the angle between the {@link Moveable}s {@link Position} and it's end-Position is zero
    * Otherwise this method will return <code>false</code>
    * 
    * @param moveable
    *        the {@link Moveable}
    * @param endPos
    *        the desired end-{@link Position}
    * @return <code>true</code> if the angle between the {@link Moveable}s {@link Position} and the given end s end-Position is zero.
    *         Otherwise return <code>false</code>
    */
   public boolean isOrientatingNecessary(Moveable moveable, EndPosition endPos) {
      if (has2CheckOrientation(moveable, endPos)) {
         Position moveablePosition = moveable.getPosition();
         double calcedAngle = calcAngle2EndPos(moveablePosition, endPos);
         return abs(calcedAngle) != 0.0d;
      }
      return false;
   }

   /**
    * Calculates the angle from the given {@link Position} to the {@link EndPosition}
    * 
    * @param moveablePosition
    *        the source {@link Position}
    * @param endPos
    *        the {@link EndPosition}
    * @return the angle between those two {@link Position}s
    */
   public double calcAngle2EndPos(Position moveablePosition, EndPosition endPos) {
      return round(moveablePosition.calcAngleRelativeTo(endPos), ANGLE_DECIMALS);
   }

   private boolean has2CheckOrientation(Moveable moveable, EndPosition endPos) {
      return nonNull(endPos) && !endPos.hasReached(moveable);
   }

   /**
    * Calculates the angle which a {@link Position} has to be turned in order to reduce the given angle difference.
    * 
    * @param angleDiff
    *        the given difference between two {@link Position}s
    * @param minimalTurnIncrement
    *        the minimum angle a {@link Position} is rotated in order to minimize the diff
    * @return the effective angle to turn
    */
   public double getAngle2Turn(double angldeDiff, double minimalTurnIncrement) {
      double ajdustedAngleDiff = adjustAngleDiff4TurnWithin180(angldeDiff);
      double angle2Turn;
      int signum = MathUtil.getSignum(ajdustedAngleDiff);
      if (abs(ajdustedAngleDiff) > minimalTurnIncrement) {
         angle2Turn = signum * minimalTurnIncrement;
      } else /*if (abs(angleDiff) > 0)*/ {
         angle2Turn = ajdustedAngleDiff;
      }
      return adjustAngleDiff4TurnWithin180(angle2Turn);
   }

   private static double adjustAngleDiff4TurnWithin180(double angleDiff) {
      if (angleDiff > 180) {
         angleDiff = angleDiff - 360;
      } else if (angleDiff < -180) {
         angleDiff = 360 - abs(angleDiff);
      }
      return angleDiff;
   }
}

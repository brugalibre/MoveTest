package com.myownb3.piranha.core.statemachine.impl.handler.returningstate;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link ReturningStateHandlerAngleHelper} is used as a helper in order to calculate the angles to turn
 * 
 * @author Dominic
 *
 */
public class ReturningStateHandlerAngleHelper {

   /**
    * Calculates the angle for {@link ReturnStates#ANGLE_CORRECTION_PHASE_FROM_ORDONAL}
    * 
    * @param currentAngle
    *        the current difference angle between the {@link Moveable} and the {@link EndPosition}
    * @param initAngle2Turn
    *        the initial angle we'duse to turn the {@link Moveable}
    * @return the angle for {@link ReturnStates#ANGLE_CORRECTION_PHASE_FROM_ORDONAL}
    */
   public double calcAngle2Turn4CorrectionPhase1(double currentAngle, double initAngle2Turn) {
      double actualDiff = 90.0d - currentAngle;
      return Math.min(actualDiff, initAngle2Turn);
   }

   /**
    * Calculates the angle for {@link ReturnStates#ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL}
    * 
    * @param currentAngle
    *        the current difference angle between the {@link Moveable} and the {@link EndPosition}
    * @param initAngle2Turn
    *        the initial angle we'duse to turn the {@link Moveable}
    * @return the angle for {@link ReturnStates#ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL}
    */
   public double calcAngle2Turn4CorrectionPhase2(double currentAngle, double initAngle2Turn) {
      double actualDiff = 0 - currentAngle;
      double effectAngle2Turn = min(abs(initAngle2Turn), abs(actualDiff));
      if (has2InvertAngle(actualDiff, initAngle2Turn)) {
         effectAngle2Turn = -effectAngle2Turn;
      }
      return correctAngle2TurnSignum(actualDiff, effectAngle2Turn);
   }

   /*
    * If the diff-angle is negative -> we have to make a turn with a positive angle (in order to minimize the diff)
    * If the diff-angle is positive, we have to make a turn with a negative angle
    */
   private static double correctAngle2TurnSignum(double actualDiff, double angle2Turn) {
      if (actualDiff <= 0) {
         angle2Turn = -angle2Turn;
      }
      return angle2Turn;
   }

   /*
    * I can't tell why but in some cases it's necessary to invert the angle right before we correct it again
    */
   private static boolean has2InvertAngle(double actualDiff, double initAngle2Turn) {
      return initAngle2Turn > 0 && actualDiff < 0;
   }

}

package com.myownb3.piranha.core.detector.cluster.tripple;

import java.util.function.Function;

import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link StaticSupportiveFlanksDetectingStrategyHandler} handles a specific {@link DetectingStrategy}
 * 
 * @author Dominic
 *
 */
public class StaticSupportiveFlanksDetectingStrategyHandler extends SupportiveFlanksDetectingStrategyHandler {
   @Override
   public double getEvasionAngleRelative2(Position position) {
      return evasionDetectedDetectorOpt.map(evalEvasionAngleWithDetector(position))
            .orElse(0.0);
   }

   private Function<? super IDetector, Double> evalEvasionAngleWithDetector(Position position) {
      return evasionDetectedDetector -> {
         // One of the side detectors has detected something -> let them eval the evasion angle
         double evasionAngleRelative2 = super.getEvasionAngleRelative2(position);
         if (evasionDetectedDetector == centerDetector.getDetector()) {
            return evalEvasionAngle4CenterDetector(position, evasionAngleRelative2);
         }
         return evasionAngleRelative2;
      };
   }

   /*
    *  If the center detector has detected something we must adjust the direction into which we want to turn
    *  Note  a negative evasion angle means a turn to the left whereas a positive angle is a turn to the right
    */
   private double evalEvasionAngle4CenterDetector(Position position, double evalEvasionAngle) {

      boolean hasLeftSideGridElementDetected = leftSideDetector.getDetector().hasGridElementDetectedAtPosition(position);
      boolean hasRightSideGridElementDetected = rightSideDetector.getDetector().hasGridElementDetectedAtPosition(position);

      if (noCorrectionNecessary(evalEvasionAngle, hasLeftSideGridElementDetected, hasRightSideGridElementDetected)) {
         return evalEvasionAngle;
      }
      return -evalEvasionAngle;
   }

   /*
    * When avoiding an evasion with the center detector, We don't need to change our turn angle if
    *    - both side has detecting any GridElement
    *    - we would turn to the left and the left side is clear
    *    - we would turn to the right and the right side is clear
    */
   private boolean noCorrectionNecessary(double evalEvasionAngle, boolean hasLeftSideGridElementDetected, boolean hasRightSideGridElementDetected) {
      return isTurnLeftAndLeftSideIsClear(evalEvasionAngle, hasLeftSideGridElementDetected)
            || isTurnRightAndRightSideIsClear(evalEvasionAngle, hasRightSideGridElementDetected)
            || bothSidesHasDetecting(hasLeftSideGridElementDetected, hasRightSideGridElementDetected);
   }

   private static boolean bothSidesHasDetecting(boolean hasLeftSideGridElementDetected, boolean hasRightSideGridElementDetected) {
      return hasLeftSideGridElementDetected && hasRightSideGridElementDetected;
   }

   private static boolean isTurnLeftAndLeftSideIsClear(double evalEvasionAngle, boolean hasLeftSideGridElementDetected) {
      return isLeftTurn(evalEvasionAngle) && !hasLeftSideGridElementDetected;
   }

   private static boolean isTurnRightAndRightSideIsClear(double evalEvasionAngle, boolean hasRightSideGridElementDetected) {
      return isRightTurn(evalEvasionAngle) && !hasRightSideGridElementDetected;
   }

   private static boolean isRightTurn(double evalEvasionAngle) {
      return evalEvasionAngle > 0;
   }

   private static boolean isLeftTurn(double evalEvasionAngle) {
      return evalEvasionAngle < 0;
   }
}

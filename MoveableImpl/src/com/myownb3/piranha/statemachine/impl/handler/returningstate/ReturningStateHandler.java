package com.myownb3.piranha.statemachine.impl.handler.returningstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;
import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.util.Objects.isNull;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;
import com.myownb3.piranha.util.MathUtil;

public class ReturningStateHandler extends CommonEvasionStateHandlerImpl<ReturningEventStateInput, CommonEvasionStateResult> {

   private double returningAngle;
   private double initReturningAngle;
   private double distanceMargin;
   private double angleMargin;

   @Visible4Testing
   ReturnStates state;
   private double initialDistance;
   private int signum;

   /**
    * Creates a new {@link ReturningStateHandler} with the given
    * {@link EvasionStateMachineConfig}
    * 
    * @param config
    *        the {@link EvasionStateMachineConfig}
    */
   public ReturningStateHandler(EvasionStateMachineConfig config) {
      this(config.getReturningAngleIncMultiplier(), config.getReturningMinDistance(),
            config.getReturningAngleMargin(), config.getEvasionAngleInc());
   }

   private ReturningStateHandler(double angleIncMultiplier, double distanceMargin, double angleMargin,
         double evasionAngleInc) {
      super();
      this.initReturningAngle = evasionAngleInc * angleIncMultiplier;
      this.distanceMargin = distanceMargin;
      this.angleMargin = angleMargin;
      init();
   }

   @Override
   public void init() {
      super.init();
      state = ReturnStates.ENTER_RETURNING;
      this.initialDistance = 0;
      this.signum = 0;
      this.returningAngle = initReturningAngle;
   }

   @Override
   public CommonEvasionStateResult handle(ReturningEventStateInput evenStateInput) {
      EndPosition endPosition = evenStateInput.getEndPosition();
      if (isReturningNotNecessary(endPosition)) {
         init();
         return CommonEvasionStateResult.of(RETURNING, evalNextState(evenStateInput, RETURNING.nextState()), null);
      }
      EvasionStates nextState = handleReturning(evenStateInput.getPositionBeforeEvasion(),
            evenStateInput.getMoveable(), endPosition);
      return evalNextStateAndBuildResult(evenStateInput, RETURNING, nextState);
   }

   private EvasionStates handleReturning(Position positionBeforeEvasion, Moveable moveable, EndPosition endPos) {
      Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
      switch (state) {
         case ENTER_RETURNING:
            handleFirstAngleCorrection(positionBeforeEvasion, moveable, endPosLine);
            return RETURNING;
         case ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL:
            doCorrectionPhase1(moveable, positionBeforeEvasion, endPosLine);
            return evalNextState4StateUntilOrdonal(positionBeforeEvasion, moveable, endPosLine);
         case ANGLE_CORRECTION_PHASE_FROM_ORDONAL:
            doCorrectionPhase2(moveable, positionBeforeEvasion, endPosLine);
            return evalNextState4StateFromOrdonal(positionBeforeEvasion, moveable, endPosLine);
         case RELATIVE_ANGLE_CORRECTION_TO_END_POS:
            EvasionStates nextEvasionState = makeFinalAngleCorrectionIfNecessary(moveable, endPos);
            init();
            return nextEvasionState;
         default:
            throw new IllegalStateException("Unhandled State '" + state + "'");
      }
   }

   private void doCorrectionPhase1(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
      double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
      double actualDiff = 90.0d - currentAngle;
      double angle2Turn = Math.min(actualDiff, getAngle2Turn());
      moveable.makeTurnWithoutPostConditions(angle2Turn);
   }

   private void doCorrectionPhase2(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
      double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
      double actualDiff = 0 - currentAngle;
      double angle2Turn = getAngle2Turn4CorrectionPhase2(actualDiff);
      moveable.makeTurnWithoutPostConditions(-angle2Turn);
   }

   private EvasionStates makeFinalAngleCorrectionIfNecessary(Moveable moveable, EndPosition endPos) {
      double angleRelativeTo = moveable.getPosition().calcAngleRelativeTo(endPos);
      moveable.makeTurnWithoutPostConditions(angleRelativeTo);
      return RETURNING.nextState();
   }

   private EvasionStates evalNextState4StateFromOrdonal(Position positionBeforeEvasion, Moveable moveable,
         Float64Vector endPosLine) {
      if (facesSameDirection(moveable, endPosLine)
            && !isMoveableOnEndPosDirection(endPosLine, positionBeforeEvasion, moveable.getPosition())) {
         state = ReturnStates.ENTER_RETURNING;
         returningAngle = returningAngle / 2;
         return RETURNING;
      }
      return evalNextState(positionBeforeEvasion, moveable, endPosLine);
   }

   private EvasionStates evalNextState4StateUntilOrdonal(Position positionBeforeEvasion, Moveable moveable,
         Float64Vector endPosLine) {
      double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
      double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
            endPosLine);
      if (currentDistance <= (initialDistance / 2) || currentAngle >= 45.0d) {
         state = ReturnStates.ANGLE_CORRECTION_PHASE_FROM_ORDONAL;
         return RETURNING;
      }
      return evalNextState(positionBeforeEvasion, moveable, endPosLine);
   }

   private double getAngle2Turn4CorrectionPhase2(double actualDiff) {
      double initAngle2Turn = getAngle2Turn();
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

   private EvasionStates evalNextState(Position positionBeforeEvasion, Moveable moveable, Float64Vector endPosLine) {
      // If the moveable is on the endPosLine and faces into the right direction ->
      // make one final correction between the moveable position and it's end position and then we are done here
      if (moveableIsBackOnTrack(positionBeforeEvasion, moveable, endPosLine)) {
         state = ReturnStates.RELATIVE_ANGLE_CORRECTION_TO_END_POS;
      }
      return RETURNING;
   }

   private boolean moveableIsBackOnTrack(Position positionBeforeEvasion, Moveable moveable, Float64Vector endPosLine) {
      return isMoveableOnEndPosDirection(endPosLine, positionBeforeEvasion, moveable.getPosition())
            && facesSameDirection(moveable, endPosLine);
   }

   private void handleFirstAngleCorrection(Position positionBeforeEvasion, Moveable moveable,
         Float64Vector endPosLine) {
      state = ReturnStates.ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL;

      Position moveablePos = moveable.getPosition();
      this.initialDistance = calcDistanceFromPositionToLine(moveablePos, positionBeforeEvasion, endPosLine);
      signum = calcSignumWithDistance(moveablePos, positionBeforeEvasion, endPosLine, initReturningAngle);
      if (facesSameDirection(moveable, endPosLine)) {
         return;
      }
      double angle2Turn = getAngle2Turn();
      moveable.makeTurnWithoutPostConditions(angle2Turn);
   }

   private double getAngle2Turn() {
      return signum * returningAngle;
   }

   /*
    * Defines if the moveable faces into the same direction then the
    * 'end-Position'-line
    * 
    * Mathematically this is defined by the angle between the line created by 'the
    * point before evasion' and 'the final end-position' and the line defined by
    * the direction of the moveable
    */
   private boolean facesSameDirection(Moveable moveable, Float64Vector endPosLine) {
      double angle = calcAngle(moveable.getPosition(), endPosLine);
      return angleMargin >= angle && angle >= 0.0d;
   }

   /*
    * Verifies if the Movebale is on the line created by 'the point bevore evasion'
    * and 'the final end-position'
    * 
    */
   private boolean isMoveableOnEndPosDirection(Float64Vector endPosDirectionVector, Position positionBeforeEvasion,
         Position moveablePos) {
      double distance = MathUtil.calcDistanceFromPositionToLine(moveablePos, positionBeforeEvasion,
            endPosDirectionVector);
      return distance <= distanceMargin;
   }

   /*
    * If there is no end-point, then we can't really return anywhere. Thats why
    * we're done here
    */
   private static boolean isReturningNotNecessary(Object endPos) {
      return isNull(endPos);
   }
}

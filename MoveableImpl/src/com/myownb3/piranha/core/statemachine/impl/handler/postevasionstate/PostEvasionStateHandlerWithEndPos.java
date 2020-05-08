package com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate;

import static com.myownb3.piranha.core.statemachine.states.EvasionStates.POST_EVASION;
import static java.util.Objects.requireNonNull;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.handler.postevasion.PostEvasionStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

public class PostEvasionStateHandlerWithEndPos extends
      CommonEvasionStateHandlerImpl<PostEvasionEventStateInput, CommonEvasionStateResult> implements PostEvasionStateHandler {

   private double mingAngle2Turn;
   @Visible4Testing
   PostEvasionStates state;
   private int signum;

   public PostEvasionStateHandlerWithEndPos(double mingAngle2Turn) {
      this.mingAngle2Turn = mingAngle2Turn;
      init();
   }

   @Override
   public void init() {
      super.init();
      this.signum = 0;
      state = PostEvasionStates.ENTERING_POST_EVASION;
   }

   @Override
   public CommonEvasionStateResult handle(PostEvasionEventStateInput evenStateInput) {
      EvasionStates nextState = handlePostEvasion(evenStateInput);
      return evalNextStateAndBuildResult(evenStateInput, POST_EVASION, nextState);
   }

   private EvasionStates handlePostEvasion(PostEvasionEventStateInput evenStateInput) {
      switch (state) {
         case ENTERING_POST_EVASION:
            return handleFirstTimePostEvasion(evenStateInput);
         case POST_EVASION:
            return handlePostEvasionState(evenStateInput);
         default:
            throw new IllegalStateException("Unsupported state ' " + state + "!'");
      }
   }

   private EvasionStates handleFirstTimePostEvasion(PostEvasionEventStateInput evenStateInput) {
      Moveable moveable = evenStateInput.getMoveable();
      EndPosition endPosition = requireNonNull(evenStateInput.getEndPosition(), "We need an end-position here!");
      this.signum = calcSignum(moveable.getPosition(), evenStateInput.getPositionBeforeEvasion(), endPosition);
      state = PostEvasionStates.POST_EVASION;
      return handlePostEvasionState(evenStateInput);
   }

   private EvasionStates handlePostEvasionState(PostEvasionEventStateInput evenStateInput) {
      Position positionBeforeEvasion = evenStateInput.getPositionBeforeEvasion();
      Moveable moveable = evenStateInput.getMoveable();
      Position endPosition = evenStateInput.getEndPosition();
      boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable, endPosition);
      if (isAngleCorrectionNecessary) {
         adjustDirection(positionBeforeEvasion, moveable, evenStateInput.getHelper(), evenStateInput.getGrid(),
               endPosition);
         return POST_EVASION;
      }
      return POST_EVASION.nextState();
   }

   private boolean isAngleCorrectionNecessary(Position positionBeforeEvasion, Moveable moveable, Position endPos) {
      Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
      double angle = calcAngle(moveable.getPosition(), endPosLine);
      return angle != 0.0d;
   }

   private void adjustDirection(Position positionBeforeEvasion, Moveable moveable, DetectableMoveableHelper helper,
         Grid grid, Position endPos) {
      Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
      double angle2Turn = getAngle2Turn(moveable.getPosition(), endPosLine);
      moveable.makeTurnWithoutPostConditions(signum * angle2Turn);
   }

   private int calcSignum(Position moveablePos, Position positionBeforeEvasion, Position endPos) {
      Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
      double angle2Turn = getAngle2Turn(moveablePos, endPosLine);
      return calcSignumWithAngle(moveablePos, positionBeforeEvasion, endPosLine, angle2Turn);
   }

   private double getAngle2Turn(Position moveablePos, Float64Vector endPosLine) {
      double effectAngle2Turn = calcAngle(moveablePos, endPosLine);
      return PostEvasionUtil.getAngle2Turn(effectAngle2Turn, mingAngle2Turn);
   }
}

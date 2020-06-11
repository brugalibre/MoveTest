package com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate;

import static com.myownb3.piranha.core.statemachine.states.EvasionStates.POST_EVASION;
import static java.lang.Math.abs;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.handler.postevasion.PostEvasionStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

public class DefaultPostEvasionStateHandler extends
      CommonEvasionStateHandlerImpl<PostEvasionEventStateInput, CommonEvasionStateResult> implements PostEvasionStateHandler {

   private double postEvasionTurnAngle;

   public DefaultPostEvasionStateHandler(double postEvasionTurnAngle) {
      this.postEvasionTurnAngle = abs(postEvasionTurnAngle);
   }

   @Override
   public CommonEvasionStateResult handle(PostEvasionEventStateInput evenStateInput) {
      EvasionStates nextState = handlePostEvasion(evenStateInput);
      return evalNextStateAndBuildResult(evenStateInput, POST_EVASION, nextState);
   }

   private EvasionStates handlePostEvasion(PostEvasionEventStateInput evenStateInput) {
      Position positionBeforeEvasion = evenStateInput.getPositionBeforeEvasion();
      Moveable moveable = evenStateInput.getMoveable();
      boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
      if (isAngleCorrectionNecessary) {
         adjustDirection(positionBeforeEvasion, moveable, evenStateInput.getHelper());
         return POST_EVASION;
      }
      return POST_EVASION.nextState();
   }

   private boolean isAngleCorrectionNecessary(Position position, Moveable moveable) {
      Position movPos = moveable.getPosition();
      return !movPos.getDirection().equals(position.getDirection());
   }

   private void adjustDirection(Position startPos, Moveable moveable, DetectableMoveableHelper helper) {
      double effectAngle2Turn = getAngle2Turn(moveable.getPosition(), startPos.getDirection().getAngle());
      moveable.makeTurnWithoutPostConditions(effectAngle2Turn);

      checkSurroundingsAndTurnBackIfNecessary(moveable, helper, -effectAngle2Turn / 2);
   }

   /*
    * If the moveable has detected an evasion, revert the turn
    */
   private static void checkSurroundingsAndTurnBackIfNecessary(Moveable moveable, DetectableMoveableHelper helper,
         double angle2Turn) {
      helper.checkSurrounding(moveable);
      if (helper.check4Evasion(moveable)) {
         moveable.makeTurnWithoutPostConditions(angle2Turn);
      }
      helper.checkSurrounding(moveable);
   }

   private double getAngle2Turn(Position moveablePos, double startPosAngle) {
      double angleDiff = startPosAngle - moveablePos.getDirection().getAngle();
      return PostEvasionUtil.getAngle2Turn(angleDiff, postEvasionTurnAngle);
   }
}

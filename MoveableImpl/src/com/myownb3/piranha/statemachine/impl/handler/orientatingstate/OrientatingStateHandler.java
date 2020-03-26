package com.myownb3.piranha.statemachine.impl.handler.orientatingstate;

import static java.lang.Math.abs;

import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.orientatingstate.input.OrientatingStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;
import com.myownb3.piranha.util.MathUtil;

public class OrientatingStateHandler extends CommonStateHandlerImpl<OrientatingStateInput, CommonEventStateResult> {

   private double correctionAngle;
   private Orientation2EndPosHelper helper;

   public OrientatingStateHandler(double correctionAngle) {
      this.correctionAngle = correctionAngle;
      helper = new Orientation2EndPosHelper();
   }

   @Override
   public CommonEventStateResult handle(OrientatingStateInput evenStateInput) {
      EvasionStates nextState = handleOrientatingState(evenStateInput.getMoveable(), evenStateInput.getEndPos());
      return evalNextStateAndBuildResult(evenStateInput, EvasionStates.ORIENTING, nextState);
   }

   private EvasionStates handleOrientatingState(Moveable moveable, EndPosition endPos) {
      if (!helper.isOrientatingNecessary(moveable, endPos)) {
         // Since we are done with orientating, lets go to the next state
         return EvasionStates.ORIENTING.nextState();
      }
      correctAngle(moveable, endPos);
      return evalNextState(moveable, endPos);
   }

   private EvasionStates evalNextState(Moveable moveable, EndPosition endPos) {
      if (!helper.isOrientatingNecessary(moveable, endPos)) {
         return EvasionStates.ORIENTING.nextState();
      }
      return EvasionStates.ORIENTING;
   }

   private void correctAngle(Moveable moveable, Position endPos) {
      Position moveablePosition = moveable.getPosition();
      double diffAngle = getAngle2Turn(endPos, moveablePosition);
      moveable.makeTurnWithoutPostConditions(diffAngle);
   }

   private double getAngle2Turn(Position endPos, Position moveablePosition) {
      double angleRelativeTo = moveablePosition.calcAngleRelativeTo(endPos);
      if (abs(angleRelativeTo) > abs(correctionAngle)) {
         int signum = MathUtil.getSignum(angleRelativeTo);
         return signum * correctionAngle;
      }
      return angleRelativeTo;
   }
}

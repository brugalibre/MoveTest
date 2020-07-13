package com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate.input.OrientatingStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

public class OrientatingStateHandler extends CommonEvasionStateHandlerImpl<OrientatingStateInput, CommonEvasionStateResult> {

   private double correctionAngle;
   private Orientation2PositionHelper helper;

   public OrientatingStateHandler(double correctionAngle) {
      this.correctionAngle = correctionAngle;
      helper = new Orientation2PositionHelper();
   }

   @Override
   public CommonEvasionStateResult handle(OrientatingStateInput evenStateInput) {
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

   private void correctAngle(Moveable moveable, EndPosition endPos) {
      Position moveablePosition = moveable.getPosition();
      double angleDiff = helper.calcAngle2EndPos(moveablePosition, endPos);
      double diffAngle = helper.getAngle2Turn(angleDiff, correctionAngle);
      moveable.makeTurnWithoutPostConditions(diffAngle);
   }

}

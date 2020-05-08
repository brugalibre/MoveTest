package com.myownb3.piranha.core.statemachine.impl.handler.passingstate;

import static com.myownb3.piranha.core.statemachine.states.EvasionStates.PASSING;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.passingstate.input.PassingEventStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

public class PassingStateHandler extends CommonEvasionStateHandlerImpl<PassingEventStateInput, CommonEvasionStateResult> {

   private int passingDistance;

   public PassingStateHandler(int passingDistance) {
      this.passingDistance = passingDistance;
   }

   @Override
   public CommonEvasionStateResult handle(PassingEventStateInput evenStateInput) {
      EvasionStates nextState = handlePassing(evenStateInput);
      return evalNextStateAndBuildResult(evenStateInput, PASSING, nextState);
   }

   private EvasionStates handlePassing(PassingEventStateInput evenStateInput) {
      if (isPassingUnnecessary(evenStateInput)) {
         // Since we are done with passing, lets go to the next state
         return PASSING.nextState();
      }
      return PASSING;
   }

   private boolean isPassingUnnecessary(PassingEventStateInput evenStateInput) {
      Moveable moveable = evenStateInput.getMoveable();
      DetectableMoveableHelper helper = evenStateInput.getHelper();

      Position position = moveable.getPosition();
      boolean isDistanceFarEnough = evenStateInput.getPositionBeforeEvasion()
            .calcDistanceTo(position) > passingDistance;

      return isDistanceFarEnough && !helper.check4Evasion(evenStateInput.getGrid(), moveable);
   }
}

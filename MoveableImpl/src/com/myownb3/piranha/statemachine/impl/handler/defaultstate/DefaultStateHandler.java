package com.myownb3.piranha.statemachine.impl.handler.defaultstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.DEFAULT;
import static com.myownb3.piranha.statemachine.states.EvasionStates.ORIENTING;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.statemachine.impl.handler.defaultstate.input.DefaultStateInput;
import com.myownb3.piranha.statemachine.impl.handler.orientatingstate.Orientation2EndPosHelper;

public class DefaultStateHandler extends CommonEvasionStateHandlerImpl<DefaultStateInput, CommonEvasionStateResult> {

   private Orientation2EndPosHelper orientationHelper;

   public DefaultStateHandler() {
      orientationHelper = new Orientation2EndPosHelper();
   }

   @Override
   public CommonEvasionStateResult handle(DefaultStateInput evenStateInput) {
      return handleDefaultState(evenStateInput.getGrid(), evenStateInput.getMoveable(),
            evenStateInput.getHelper(), evenStateInput.getEndPos());
   }

   private CommonEvasionStateResult handleDefaultState(Grid grid, Moveable moveable, DetectableMoveableHelper detectionHelper, EndPosition endPos) {
      boolean isEvasion = detectionHelper.check4Evasion(grid, moveable);
      if (isEvasion) {
         return CommonEvasionStateResult.of(DEFAULT, DEFAULT.nextState(), Positions.of(moveable.getPosition()));
      } else if (orientationHelper.isOrientatingNecessary(moveable, endPos)) {
         return CommonEvasionStateResult.of(DEFAULT, ORIENTING, null);
      }
      return CommonEvasionStateResult.of(DEFAULT, DEFAULT, null);
   }
}

package com.myownb3.piranha.statemachine.impl.handler.defaultstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.DEFAULT;
import static com.myownb3.piranha.statemachine.states.EvasionStates.ORIENTING;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.defaultstate.input.DefaultStateInput;
import com.myownb3.piranha.statemachine.impl.handler.orientatingstate.Orientation2EndPosHelper;

public class DefaultStateHandler extends CommonStateHandlerImpl<DefaultStateInput, CommonEventStateResult> {

   private Orientation2EndPosHelper orientationHelper;

   public DefaultStateHandler() {
      orientationHelper = new Orientation2EndPosHelper();
   }

   @Override
   public CommonEventStateResult handle(DefaultStateInput evenStateInput) {
      return handleDefaultState(evenStateInput.getGrid(), evenStateInput.getMoveable(),
            evenStateInput.getHelper(), evenStateInput.getEndPos());
   }

   private CommonEventStateResult handleDefaultState(Grid grid, Moveable moveable, DetectableMoveableHelper detectionHelper, EndPosition endPos) {
      boolean isEvasion = detectionHelper.check4Evasion(grid, moveable);
      if (isEvasion) {
         return CommonEventStateResult.of(DEFAULT, DEFAULT.nextState(), Positions.of(moveable.getPosition()));
      } else if (orientationHelper.isOrientatingNecessary(moveable, endPos)) {
         return CommonEventStateResult.of(DEFAULT, ORIENTING, null);
      }
      return CommonEventStateResult.of(DEFAULT, DEFAULT, null);
   }
}

package com.myownb3.piranha.statemachine.impl.handler.defaultstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.DEFAULT;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class DefaultStateHandler extends CommonStateHandlerImpl<CommonEventStateInput, CommonEventStateResult> {

    @Override
    public CommonEventStateResult handle(CommonEventStateInput evenStateInput) {
	Position positionBeforeEvasion = handleDefaultState(evenStateInput.getGrid(), evenStateInput.getMoveable(), evenStateInput.getHelper());
	return buildAndReturnResult(positionBeforeEvasion);
    }

    private Position handleDefaultState(Grid grid, Moveable moveable, DetectableMoveableHelper helper) {
	boolean isEvasion = helper.check4Evasion(grid, moveable);
	if (isEvasion) {
	    return Positions.of(moveable.getPosition());
	}
	return null;
    }

    private CommonEventStateResult buildAndReturnResult(Position positionBeforeEvasion) {
	EvasionStates nextState = positionBeforeEvasion == null ? DEFAULT : DEFAULT.nextState();
	return CommonEventStateResult.of(DEFAULT, nextState, positionBeforeEvasion);
    }
}
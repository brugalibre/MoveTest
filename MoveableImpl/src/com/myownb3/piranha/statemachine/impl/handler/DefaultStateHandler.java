package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.DEFAULT;
import static com.myownb3.piranha.statemachine.states.EvasionStates.EVASION;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.DefaultStateResult;

public class DefaultStateHandler extends CommonStateHandlerImpl<CommonEventStateInput> {

    @Override
    public DefaultStateResult handle(CommonEventStateInput evenStateInput) {
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

    private DefaultStateResult buildAndReturnResult(Position positionBeforeEvasion) {
	return new DefaultStateResult(positionBeforeEvasion, positionBeforeEvasion == null ? DEFAULT : EVASION);
    }
}
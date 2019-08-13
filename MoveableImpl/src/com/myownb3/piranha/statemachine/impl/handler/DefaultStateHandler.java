package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.DEFAULT;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.handler.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.handler.output.DefaultStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class DefaultStateHandler implements EvasionStatesHandler<CommonEventStateInput> {

    private DetectableMoveableHelper helper;

    public DefaultStateHandler(DetectableMoveableHelper helper) {
	this.helper = helper;
    }

    @Override
    public DefaultStateResult handle(CommonEventStateInput evenStateInput) {
	Position positionBeforeEvasion = handleDefaultState(evenStateInput.getGrid(), evenStateInput.getMoveable());
	return buildAndReturnResult(positionBeforeEvasion);
    }

    private Position handleDefaultState(Grid grid, Moveable moveable) {
	boolean isEvasion = helper.check4Evasion(grid, moveable);
	if (isEvasion) {
	    return Positions.of(moveable.getPosition());
	}
	return null;
    }

    private DefaultStateResult buildAndReturnResult(Position positionBeforeEvasion) {
	return new DefaultStateResult(positionBeforeEvasion);
    }

    @Override
    public EvasionStates getNextState() {
	return DEFAULT.nextState();
    }
}
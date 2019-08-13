package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.handler.EvenStateResult;
import com.myownb3.piranha.statemachine.handler.input.PassingEventStateInput;
import com.myownb3.piranha.statemachine.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class PassingStateHandler implements EvasionStatesHandler<PassingEventStateInput> {

    private DetectableMoveableHelper helper;

    public PassingStateHandler(DetectableMoveableHelper helper) {
	this.helper = helper;
    }

    @Override
    public EvenStateResult handle(PassingEventStateInput evenStateInput) {
	handlePassing(evenStateInput);
	return null;
    }

    private void handlePassing(PassingEventStateInput evenStateInput) {

	if (isPassingUnnecessary(evenStateInput.getGrid(), evenStateInput.getMoveable(),
		evenStateInput.getPositionBeforeEvasion(), evenStateInput.getPassingDistance())) {
	    // Since we are done with passing, lets go to the next state
//	    evasionState = RETURNING;
	}
    }

    private boolean isPassingUnnecessary(Grid grid, Moveable moveable, Position positionBeforeEvasion,
	    int passingDistance) {
	Position position = moveable.getPosition();
	boolean isDistanceFarEnough = positionBeforeEvasion.calcDistanceTo(position) > passingDistance;
	return isDistanceFarEnough && !helper.check4Evasion(grid, moveable);
    }

    @Override
    public EvasionStates getNextState() {
	return POST_EVASION.nextState();
    }

}
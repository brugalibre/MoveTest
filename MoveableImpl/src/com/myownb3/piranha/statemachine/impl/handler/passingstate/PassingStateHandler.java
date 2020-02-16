package com.myownb3.piranha.statemachine.impl.handler.passingstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.PASSING;
import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.CommonStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.passingstate.input.PassingEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class PassingStateHandler extends CommonStateHandlerImpl<PassingEventStateInput> {

    private int passingDistance;

    public PassingStateHandler(int passingDistance) {
	this.passingDistance = passingDistance;
    }

    @Override
    public CommonEventStateResult handle(PassingEventStateInput evenStateInput) {
	EvasionStates nextState = handlePassing(evenStateInput);
	return evalNextStateAndBuildResult(evenStateInput, PASSING, nextState);
    }

    private EvasionStates handlePassing(PassingEventStateInput evenStateInput) {
	if (isPassingUnnecessary(evenStateInput)) {
	    // Since we are done with passing, lets go to the next state
	    return RETURNING;
	}
	return PASSING;
    }

    private boolean isPassingUnnecessary(PassingEventStateInput evenStateInput) {
	Moveable moveable = evenStateInput.getMoveable();
	DetectableMoveableHelper helper = evenStateInput.getHelper();

	Position position = moveable.getPosition();
	boolean isDistanceFarEnough = evenStateInput.getPositionBeforeEvasion().calcDistanceTo(position) > passingDistance;

	return isDistanceFarEnough && !helper.check4Evasion(evenStateInput.getGrid(), moveable);
    }
}
package com.myownb3.piranha.statemachine.impl.handler;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.PassingEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class PassingStateHandler implements EvasionStatesHandler<PassingEventStateInput> {

    @Override
    public CommonEventStateResult handle(PassingEventStateInput evenStateInput) {
	return handlePassing(evenStateInput);
    }

    private CommonEventStateResult handlePassing(PassingEventStateInput evenStateInput) {

	if (isPassingUnnecessary(evenStateInput)) {
	    // Since we are done with passing, lets go to the next state
	    return CommonEventStateResult.of(EvasionStates.PASSING.nextState());
	}
	return CommonEventStateResult.of(EvasionStates.PASSING);
    }

    private boolean isPassingUnnecessary(PassingEventStateInput evenStateInput) {
	Moveable moveable = evenStateInput.getMoveable();
	DetectableMoveableHelper helper = evenStateInput.getHelper();

	Position position = moveable.getPosition();
	boolean isDistanceFarEnough = evenStateInput.getPositionBeforeEvasion().calcDistanceTo(position) > evenStateInput.getPassingDistance();

	return isDistanceFarEnough && !helper.check4Evasion(evenStateInput.getGrid(), moveable);
    }
}
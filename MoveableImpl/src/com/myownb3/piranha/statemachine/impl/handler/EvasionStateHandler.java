package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.EVASION;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.EvasionEventStateResult;

public class EvasionStateHandler implements EvasionStatesHandler<EvasionEventStateInput> {

    @Override
    public EvasionEventStateResult handle(EvasionEventStateInput evenStateInput) {
	return handleEvasionState(evenStateInput.getGrid(), evenStateInput.getMoveable(), evenStateInput.getDetector(), evenStateInput.getHelper());
    }

    private EvasionEventStateResult handleEvasionState(Grid grid, Moveable moveable, Detector detector, DetectableMoveableHelper helper) {

	double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
	if (avoidAngle != 0.0d) {
	    moveable.makeTurnWithoutPostConditions(avoidAngle);
	    moveable.moveForwardWithoutPostConditions();
	    helper.checkSurrounding(grid, moveable);
	    return EvasionEventStateResult.of(EVASION);
	}
	return EvasionEventStateResult.of(EVASION.nextState());
    }
}
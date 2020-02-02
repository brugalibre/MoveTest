package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.EVASION;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.EvasionStateResult;

public class EvasionStateHandler extends CommonStateHandlerImpl<EvasionEventStateInput> {

    @Override
    public EvasionStateResult handle(EvasionEventStateInput evenStateInput) {
	return handleEvasionState(evenStateInput.getGrid(), evenStateInput.getMoveable(), evenStateInput.getDetector(), evenStateInput.getHelper());
    }

    private EvasionStateResult handleEvasionState(Grid grid, Moveable moveable, Detector detector, DetectableMoveableHelper helper) {
	if (helper.check4Evasion(grid, moveable)) {
	    double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
	    if (avoidAngle != 0.0d) {
		moveable.makeTurnWithoutPostConditions(avoidAngle);
		helper.checkSurrounding(grid, moveable);
		if (helper.check4Evasion(grid, moveable)) {
		    return EvasionStateResult.of(EVASION, avoidAngle);
		}
	    }
	}
	return EvasionStateResult.of(EVASION.nextState(), 0);
    }

}
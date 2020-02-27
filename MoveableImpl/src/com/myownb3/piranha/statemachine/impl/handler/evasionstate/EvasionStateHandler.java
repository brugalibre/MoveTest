package com.myownb3.piranha.statemachine.impl.handler.evasionstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.EVASION;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.output.EvasionStateResult;

public class EvasionStateHandler extends CommonStateHandlerImpl<EvasionEventStateInput, EvasionStateResult> {

    @Override
    public EvasionStateResult handle(EvasionEventStateInput evenStateInput) {
	return handleEvasionState(evenStateInput.getGrid(), evenStateInput.getMoveable(), evenStateInput.getDetector(),
		evenStateInput.getHelper());
    }

    private EvasionStateResult handleEvasionState(Grid grid, Moveable moveable, Detector detector,
	    DetectableMoveableHelper helper) {
	double avoidAngle = 0;
	if (helper.check4Evasion(grid, moveable)) {
	    avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
	    if (avoidAngle != 0.0d) {
		moveable.makeTurnWithoutPostConditions(avoidAngle);
		helper.checkSurrounding(grid, moveable);
		if (helper.check4Evasion(grid, moveable)) {
		    return EvasionStateResult.of(EVASION, EVASION, avoidAngle);
		} else {
		    // Since we are not evading anymore, turn one more time
		    moveable.makeTurnWithoutPostConditions(avoidAngle);
		}
	    }
	}
	return EvasionStateResult.of(EVASION, EVASION.nextState(), avoidAngle);
    }

}
package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.DEFAULT;

import java.util.Optional;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.moveables.statemachine.impl.MoveableExecutor;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.handler.EvenStateResult;
import com.myownb3.piranha.statemachine.handler.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class EvasionStateHandler implements EvasionStatesHandler<EvasionEventStateInput> {

    @Override
    public EvenStateResult handle(EvasionEventStateInput evenStateInput) {
	handleEvasionState(evenStateInput.getGrid(), evenStateInput.getMoveable(), evenStateInput.getDetector(),
		evenStateInput.getHelper());
	return null;
    }

    private void handleEvasionState(Grid grid, Moveable moveable, Detector detector, DetectableMoveableHelper helper) {

	double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
	if (avoidAngle != 0.0d) {
	    addExecutorIfNecessary(-avoidAngle, () -> moveable.moveMakeTurnAndForward(-avoidAngle));
	    moveable.moveMakeTurnAndForward(avoidAngle);
	    helper.checkSurrounding(grid, moveable);
	}
	// evasionState = POST_EVASION;
    }

    private Optional<MoveableExecutor> addExecutorIfNecessary(double angle2Turn, MoveableExecutor moveableExec) {
	if (angle2Turn != 0.0d) {
	    return Optional.of(moveableExec);
	}
	return Optional.empty();
    }

    @Override
    public EvasionStates getNextState() {
	return DEFAULT.nextState();
    }
}
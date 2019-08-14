package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;

import java.util.LinkedList;
import java.util.List;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableExecutor;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.PostEvasionEventStateResult;

public class PostEvasionStateHandler implements EvasionStatesHandler<PostEvasionEventStateInput> {

    private List<MoveableExecutor> executors;

    public PostEvasionStateHandler() {
	executors = new LinkedList<>();
    }

    @Override
    public PostEvasionEventStateResult handle(PostEvasionEventStateInput evenStateInput) {
	PostEvasionEventStateResult stateResult;
	try {
	    stateResult = handlePostEvasion(evenStateInput.getMoveable(), evenStateInput.getPositionBeforeEvasion());
	} finally {
	    cleanUp();
	}
	return stateResult;
    }

    private PostEvasionEventStateResult handlePostEvasion(Moveable moveable, Position positionBeforeEvasion) {
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable);
	}
	return PostEvasionEventStateResult.of(POST_EVASION.nextState(), new LinkedList<>(executors));
    }

    private boolean isAngleCorrectionNecessary(Position position, Moveable moveable) {

	Position movPos = moveable.getPosition();
	return !movPos.getDirection().equals(position.getDirection());
    }

    private void adjustDirection(Position startPos, Moveable moveable) {

	double effectAngle2Turn = getAngle2Turn(moveable, startPos.getDirection().getAngle());
	addExecutorIfNecessary(-effectAngle2Turn, () -> moveable.moveMakeTurnAndForward(-effectAngle2Turn));
	moveable.moveMakeTurnAndForward(effectAngle2Turn);
    }

    private double getAngle2Turn(Moveable moveable, double calcAbsolutAngle) {
	return (moveable.getPosition().getDirection().getAngle() - calcAbsolutAngle);
    }

    private void addExecutorIfNecessary(double angle2Turn, MoveableExecutor moveableExec) {
	if (angle2Turn != 0.0d) {
	    executors.add(moveableExec);
	}
    }

    /*
     * Clear all collected MoveableExecutors. Since as we leave this handle() Method
     * we are done with all registered executors
     */
    private void cleanUp() {
	executors.clear();
    }
}
package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;

import java.util.Optional;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableExecutor;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.handler.output.EvenStateResult;
import com.myownb3.piranha.statemachine.impl.handler.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class PostEvastionStateHandler implements EvasionStatesHandler<CommonEventStateInput> {

    private Position positionBeforeEvasion;

    @Override
    public EvenStateResult handle(CommonEventStateInput evenStateInput) {
	handlePostEvasion(evenStateInput.getMoveable());
	return null;
    }

    private void handlePostEvasion(Moveable moveable) {
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable);
	}
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

    private Optional<MoveableExecutor> addExecutorIfNecessary(double angle2Turn, MoveableExecutor moveableExec) {
	if (angle2Turn != 0.0d) {
	    return Optional.of(moveableExec);
	}
	return Optional.empty();
    }

    @Override
    public EvasionStates getNextState() {
	return POST_EVASION.nextState();
    }

}
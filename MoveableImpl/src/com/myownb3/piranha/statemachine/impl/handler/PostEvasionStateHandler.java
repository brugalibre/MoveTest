package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.PostEvasionEventStateResult;

public class PostEvasionStateHandler implements EvasionStatesHandler<PostEvasionEventStateInput> {

    private static final int STEP_WIDTH = 5; // Like this the movements are smoother

    @Override
    public PostEvasionEventStateResult handle(PostEvasionEventStateInput evenStateInput) {
	return handlePostEvasion(evenStateInput.getMoveable(), evenStateInput.getPositionBeforeEvasion());
    }

    private PostEvasionEventStateResult handlePostEvasion(Moveable moveable, Position positionBeforeEvasion) {
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable);
	    return PostEvasionEventStateResult.of(POST_EVASION);
	}
	return PostEvasionEventStateResult.of(POST_EVASION.nextState());
    }

    private boolean isAngleCorrectionNecessary(Position position, Moveable moveable) {

	Position movPos = moveable.getPosition();
	return !movPos.getDirection().equals(position.getDirection());
    }

    private void adjustDirection(Position startPos, Moveable moveable) {

	double effectAngle2Turn = getAngle2Turn(moveable, startPos.getDirection().getAngle());
	moveable.makeTurnWithoutPostConditions(effectAngle2Turn);
	moveable.moveForwardWithoutPostConditions();
    }

    private double getAngle2Turn(Moveable moveable, double calcAbsolutAngle) {
	double angleDiff = calcAbsolutAngle - moveable.getPosition().getDirection().getAngle();
	if (angleDiff > STEP_WIDTH) {
	    return angleDiff / STEP_WIDTH;
	}
	return angleDiff;
    }
}
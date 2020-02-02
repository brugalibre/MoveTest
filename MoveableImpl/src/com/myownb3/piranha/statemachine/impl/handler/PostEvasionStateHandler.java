package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.impl.handler.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class PostEvasionStateHandler extends CommonStateHandlerImpl<PostEvasionEventStateInput> {

    private double stepWidth;

    public PostEvasionStateHandler(double stepWidth) {
	this.stepWidth = stepWidth;
    }

    @Override
    public CommonEventStateResult handle(PostEvasionEventStateInput evenStateInput) {
	EvasionStates nextState = handlePostEvasion(evenStateInput.getMoveable(), evenStateInput.getPositionBeforeEvasion());
	return evalNextStateAndBuildResult(evenStateInput, nextState);
    }

    private EvasionStates handlePostEvasion(Moveable moveable, Position positionBeforeEvasion) {
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable);
	    return POST_EVASION;
	}
	return POST_EVASION.nextState();
    }

    private boolean isAngleCorrectionNecessary(Position position, Moveable moveable) {

	Position movPos = moveable.getPosition();
	return !movPos.getDirection().equals(position.getDirection());
    }

    private void adjustDirection(Position startPos, Moveable moveable) {

	double effectAngle2Turn = getAngle2Turn(moveable, startPos.getDirection().getAngle());
	moveable.makeTurnWithoutPostConditions(effectAngle2Turn);
    }

    private double getAngle2Turn(Moveable moveable, double startPosAngle) {
	double angleDiff = startPosAngle - moveable.getPosition().getDirection().getAngle();
	if (angleDiff > stepWidth) {
	    return angleDiff / stepWidth;
	}
	return angleDiff;
    }
}
package com.myownb3.piranha.statemachine.impl.handler.postevasion;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;
import static java.lang.Math.max;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.impl.handler.CommonStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.postevasion.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class PostEvasionStateHandler extends CommonStateHandlerImpl<PostEvasionEventStateInput> {

    private PostEvasionStates state;
    private double stepWidth;
    private Position endPos;
    private int signum;

    public PostEvasionStateHandler(Position endPos, double stepWidth) {
	this.stepWidth = stepWidth;
	this.endPos = endPos;
	init();
    }

    @Override
    public void init() {
        super.init();
        this.signum = 0;
        state = PostEvasionStates.ENTERING_POST_EVASION;
    }
    
    @Override
    public CommonEventStateResult handle(PostEvasionEventStateInput evenStateInput) {
	EvasionStates nextState = handlePostEvasion(evenStateInput.getMoveable(),
		evenStateInput.getPositionBeforeEvasion());
	return evalNextStateAndBuildResult(evenStateInput, POST_EVASION, nextState);
    }

    private EvasionStates handlePostEvasion(Moveable moveable, Position positionBeforeEvasion) {
	switch (state) {
	case ENTERING_POST_EVASION:
	    return handleFirstTimePostEvasion(moveable, positionBeforeEvasion);
	case POST_EVASION:
	    return handlePostEvasionState(moveable, positionBeforeEvasion);
	default:
	    throw new IllegalStateException("Unsupported state ' " + state + "!'");
	}
    }

    private EvasionStates handleFirstTimePostEvasion(Moveable moveable, Position positionBeforeEvasion) {
	this.signum = calcSignum(moveable.getPosition(), positionBeforeEvasion);
	state = PostEvasionStates.POST_EVASION;
	return handlePostEvasionState(moveable, positionBeforeEvasion);
    }

    private EvasionStates handlePostEvasionState(Moveable moveable, Position positionBeforeEvasion) {
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable);
	    return POST_EVASION;
	}
	return POST_EVASION.nextState();
    }

    private boolean isAngleCorrectionNecessary(Position positionBeforeEvasion, Moveable moveable) {
	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
	double angle = calcAngle(moveable.getPosition(), endPosLine);
	return angle != 0.0d;
    }

    private void adjustDirection(Position positionBeforeEvasion, Moveable moveable) {
	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
	double angle2Turn = getAngle2Turn(moveable.getPosition(), endPosLine);
	
	double angleDiff = positionBeforeEvasion.getDirection().getAngle() - moveable.getPosition().getDirection().getAngle();
	if (Math.abs(angleDiff) > stepWidth) {
	    angleDiff = angleDiff / stepWidth;
	}
	
	moveable.makeTurnWithoutPostConditions(signum * angle2Turn);
    }
    
    private int calcSignum(Position moveablePos, Position positionBeforeEvasion) {
	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
	double angle2Turn = getAngle2Turn(moveablePos, endPosLine);
	return calcSignum(moveablePos, positionBeforeEvasion, endPosLine, angle2Turn);
    }

    private double getAngle2Turn(Position moveablePos, Float64Vector endPosLine) {
	double effectAngle2Turn = calcAngle(moveablePos, endPosLine);
	if (Math.abs(effectAngle2Turn) > stepWidth) {
	    effectAngle2Turn = effectAngle2Turn / stepWidth;
	    return max(4, effectAngle2Turn);
	}
	return effectAngle2Turn;
    }
}
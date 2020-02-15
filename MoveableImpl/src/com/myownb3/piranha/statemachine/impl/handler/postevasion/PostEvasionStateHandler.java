package com.myownb3.piranha.statemachine.impl.handler.postevasion;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;
import static java.lang.Math.max;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
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
	EvasionStates nextState = handlePostEvasion(evenStateInput);
	return evalNextStateAndBuildResult(evenStateInput, POST_EVASION, nextState);
    }

    private EvasionStates handlePostEvasion(PostEvasionEventStateInput evenStateInput) {
	switch (state) {
	case ENTERING_POST_EVASION:
	    return handleFirstTimePostEvasion(evenStateInput);
	case POST_EVASION:
	    return handlePostEvasionState(evenStateInput);
	default:
	    throw new IllegalStateException("Unsupported state ' " + state + "!'");
	}
    }

    private EvasionStates handleFirstTimePostEvasion(PostEvasionEventStateInput evenStateInput) {
	Moveable moveable = evenStateInput.getMoveable();
	this.signum = calcSignum(moveable.getPosition(), evenStateInput.getPositionBeforeEvasion());
	state = PostEvasionStates.POST_EVASION;
	return handlePostEvasionState(evenStateInput);
    }

    private EvasionStates handlePostEvasionState(PostEvasionEventStateInput evenStateInput) {
	Position positionBeforeEvasion = evenStateInput.getPositionBeforeEvasion();
	Moveable moveable = evenStateInput.getMoveable();
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable, evenStateInput.getHelper(), evenStateInput.getGrid());
	    return POST_EVASION;
	}
	return POST_EVASION.nextState();
    }

    private boolean isAngleCorrectionNecessary(Position positionBeforeEvasion, Moveable moveable) {
	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
	double angle = calcAngle(moveable.getPosition(), endPosLine);
	return angle != 0.0d;
    }

    private void adjustDirection(Position positionBeforeEvasion, Moveable moveable, DetectableMoveableHelper helper,
	    Grid grid) {
	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
	double angle2Turn = getAngle2Turn(moveable.getPosition(), endPosLine);
	moveable.makeTurnWithoutPostConditions(signum * angle2Turn);

	checkSurroundingsAndTurnBackIfNecessary(moveable, helper, grid, signum * -angle2Turn / 2);
    }

    /*
     * If the moveable has detected an evasion, revert the turn
     */
    private static void checkSurroundingsAndTurnBackIfNecessary(Moveable moveable, DetectableMoveableHelper helper,
	    Grid grid, double angle2Turn) {
	helper.checkSurrounding(grid, moveable);
	if (helper.check4Evasion(grid, moveable)) {
	    moveable.makeTurnWithoutPostConditions(angle2Turn);
	}
	helper.checkSurrounding(grid, moveable);
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
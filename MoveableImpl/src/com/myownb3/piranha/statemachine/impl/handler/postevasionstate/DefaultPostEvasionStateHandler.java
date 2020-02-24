package com.myownb3.piranha.statemachine.impl.handler.postevasionstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.postevasion.PostEvasionStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class DefaultPostEvasionStateHandler extends
	CommonStateHandlerImpl<PostEvasionEventStateInput, CommonEventStateResult> implements PostEvasionStateHandler {

    private double stepWidth;

    public DefaultPostEvasionStateHandler(double stepWidth) {
	this.stepWidth = stepWidth;
    }

    @Override
    public CommonEventStateResult handle(PostEvasionEventStateInput evenStateInput) {
	EvasionStates nextState = handlePostEvasion(evenStateInput);
	return evalNextStateAndBuildResult(evenStateInput, POST_EVASION, nextState);
    }

    private EvasionStates handlePostEvasion(PostEvasionEventStateInput evenStateInput) {
	Position positionBeforeEvasion = evenStateInput.getPositionBeforeEvasion();
	Moveable moveable = evenStateInput.getMoveable();
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable, evenStateInput.getGrid(), evenStateInput.getHelper());
	    return POST_EVASION;
	}
	return POST_EVASION.nextState();
    }

    private boolean isAngleCorrectionNecessary(Position position, Moveable moveable) {
	Position movPos = moveable.getPosition();
	return !movPos.getDirection().equals(position.getDirection());
    }

    private void adjustDirection(Position startPos, Moveable moveable, Grid grid, DetectableMoveableHelper helper) {
	double effectAngle2Turn = getAngle2Turn(moveable.getPosition(), startPos.getDirection().getAngle());
	moveable.makeTurnWithoutPostConditions(effectAngle2Turn);

	checkSurroundingsAndTurnBackIfNecessary(moveable, helper, grid, -effectAngle2Turn / 2);
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

    private double getAngle2Turn(Position moveablePos, double startPosAngle) {
	double angleDiff = startPosAngle - moveablePos.getDirection().getAngle();
	if (Math.abs(angleDiff) > stepWidth) {
	    return angleDiff / stepWidth;
	}
	return angleDiff;
    }
}
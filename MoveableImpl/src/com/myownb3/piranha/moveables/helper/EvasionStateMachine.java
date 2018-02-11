/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import static com.myownb3.piranha.moveables.helper.EvasionStates.PASSING;
import static com.myownb3.piranha.moveables.helper.EvasionStates.POST_ENVASION;
import static com.myownb3.piranha.moveables.helper.EvasionStates.RETURNING;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.grid.direction.util.DirectionUtil;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;

/**
 * An {@link EvasionStateMachine} improves the default helper with functions for
 * evasion
 * 
 * @author Dominic
 *
 */
public class EvasionStateMachine extends EvasionMoveableHelper {

    private Position positionBeforeEvasion;

    private Map<EvasionStates, Integer> recursivCheck;
    private List<MoveableExecutor> reversExecutors;

    public EvasionStateMachine(Detector detector) {
	super(detector);
	positionBeforeEvasion = null;
	reversExecutors = new LinkedList<>();
	recursivCheck = new HashMap<>();
    }

    @Override
    protected void handleDefaultState(Grid grid, Moveable moveable, boolean isEvasion) {
	if (isEvasion) {
	    positionBeforeEvasion = Positions.of(moveable.getPosition());
	}
	super.handleDefaultState(grid, moveable, isEvasion);
    }

    @Override
    protected void handlePostEvasion(Moveable moveable) {
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable);
	}
	// Yes the state stays on 'POST_EVASION' even if the angle was fixed above. This
	// will be fixed during the next turn
	evasionState = isAngleCorrectionNecessary ? POST_ENVASION : PASSING;
    }

    @Override
    protected void handlePassing(Moveable moveable) {

	if (isPassingUnnecessary(moveable)) {
	    // Since we are done with passing, lets go to the next state
	    evasionState = RETURNING;
	}
    }

    private boolean isPassingUnnecessary(Moveable moveable) {
	Position position = moveable.getPosition();
	boolean isSame = DirectionUtil.isSame(position.getDirection(), positionBeforeEvasion.getDirection());

	boolean isDistanceFarEnough = positionBeforeEvasion.calcDistanceTo(position) > 5;
	return isSame && isDistanceFarEnough;
    }

    @Override
    protected void handleReturning() {

	registerForRecursivCall(RETURNING);
	if (!isMethodCallAllowed(RETURNING)) {
	    return;
	}
	reversExecutors.stream()//
		.forEach(MoveableExecutor::execute);
    }

    private boolean isAngleCorrectionNecessary(Position position, Moveable moveable) {
	return !DirectionUtil.isSame(moveable.getPosition().getDirection(), position.getDirection());
    }

    private void adjustDirection(Position startPos, Moveable moveable) {

	double calcAbsolutAngle = startPos.calcAbsolutAngle();
	double effectAngle2Turn = getAngle2Turn(moveable, calcAbsolutAngle);
	addExecutor(moveable, effectAngle2Turn);
	moveable.makeTurn(-effectAngle2Turn);
    }

    private double getAngle2Turn(Moveable moveable, double calcAbsolutAngle) {
	double effectAngle2Turn = moveable.getPosition().getDirection().getAngle() - calcAbsolutAngle;
	double signum = Math.signum(effectAngle2Turn);
	if (Math.abs(effectAngle2Turn) > detector.getAngleInc()) {
	    effectAngle2Turn = detector.getAngleInc() * signum;
	}
	return effectAngle2Turn;
    }

    @Override
    protected void handleEvasionState(Grid grid, Moveable moveable) {

	double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
	if (avoidAngle != 0) {
	    addExecutor(moveable, -avoidAngle);
	}
	super.handleEvasionState(grid, moveable);
	evasionState = POST_ENVASION;
    }

    protected void addExecutor(Moveable moveable, double avoidAngle) {
	reversExecutors.add(() -> {
	    moveable.makeTurn(avoidAngle);
	    moveable.moveForward();
	});
    }

    @FunctionalInterface
    private static interface MoveableExecutor {

	public void execute();
    }

    private void registerForRecursivCall(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	recursivCheck.put(RETURNING, counter == null ? 1 : counter + 1);
    }

    private boolean isMethodCallAllowed(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	return counter != null ? counter.intValue() <= 1 : true;
    }
}

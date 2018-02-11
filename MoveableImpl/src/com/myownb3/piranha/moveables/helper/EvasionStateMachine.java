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

    private int passingDistance;

    public EvasionStateMachine(Detector detector, int passingDistance) {
	super(detector);
	positionBeforeEvasion = null;
	reversExecutors = new LinkedList<>();
	recursivCheck = new HashMap<>();
	this.passingDistance = passingDistance;
    }

    public EvasionStateMachine(Detector detector) {
	this(detector, 4);
    }

    @Override
    protected void handleEvasion4CurrentState(Grid grid, Moveable moveable) {
	switch (evasionState) {
	case DEFAULT:
	    boolean isEvasion = check4Evasion(grid, moveable);
	    handleDefaultState(grid, moveable, isEvasion);
	    break;

	case ENVASION:
	    handleEvasionState(grid, moveable);
	    break;
	case POST_ENVASION:
	    handlePostEvasion(moveable);
	    break;
	case PASSING:
	    handlePassing(moveable);
	    break;
	case RETURNING:
	    handleReturning();
	    break;
	default:
	    throw new IllegalStateException("Unknown state'" + evasionState + "'");
	}
    }

    @Override
    protected void handleDefaultState(Grid grid, Moveable moveable, boolean isEvasion) {
	if (isEvasion) {
	    positionBeforeEvasion = Positions.of(moveable.getPosition());
	}
	super.handleDefaultState(grid, moveable, isEvasion);
    }

    protected void handlePostEvasion(Moveable moveable) {
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable);
	}
	// Yes the state stays on 'POST_EVASION' even if the angle was fixed above. This
	// will be fixed during the next turn
	evasionState = isAngleCorrectionNecessary ? POST_ENVASION : PASSING;
    }

    protected void handlePassing(Moveable moveable) {

	if (isPassingUnnecessary(moveable)) {
	    // Since we are done with passing, lets go to the next state
	    evasionState = RETURNING;
	}
    }

    private boolean isPassingUnnecessary(Moveable moveable) {
	Position position = moveable.getPosition();
	boolean isSame = DirectionUtil.isSame(position.getDirection(), positionBeforeEvasion.getDirection());

	boolean isDistanceFarEnough = positionBeforeEvasion.calcDistanceTo(position) > passingDistance;
	return isSame && isDistanceFarEnough;
    }

    protected void handleReturning() {

	if (!isMethodCallAllowed(RETURNING)) {
	    return;
	}
	registerForRecursivCall(RETURNING);
	reversExecutors.stream()//
		.forEach(MoveableExecutor::execute);
	deregisterForRecursivCall(RETURNING);
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
	return moveable.getPosition().getDirection().getAngle() - calcAbsolutAngle;
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

    private void deregisterForRecursivCall(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	recursivCheck.put(RETURNING, counter - 1);
    }

    private boolean isMethodCallAllowed(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	return counter != null ? counter.intValue() < 1 : true;
    }
}

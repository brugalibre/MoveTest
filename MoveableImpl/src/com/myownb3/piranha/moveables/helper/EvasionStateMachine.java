/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import static com.myownb3.piranha.moveables.helper.EvasionStates.DEFAULT;
import static com.myownb3.piranha.moveables.helper.EvasionStates.ENVASION;
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
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;

/**
 * An {@link EvasionStateMachine} completes the {@link EvasionMoveableHelper}
 * because the {@link EvasionStateMachine} can handle the complete evasion
 * maneuvre evasion
 * 
 * @author Dominic
 *
 */
public class EvasionStateMachine extends DetectableMoveableHelper {

    private EvasionStates evasionState;

    private Position positionBeforeEvasion;
    private int passingDistance;
    private List<MoveableExecutor> reverseExecutors;
    private Map<EvasionStates, Integer> recursivCheck;

    public EvasionStateMachine(Detector detector, int passingDistance) {
	super(detector);
	evasionState = DEFAULT;
	positionBeforeEvasion = null;
	this.passingDistance = passingDistance;
	reverseExecutors = new LinkedList<>();
	createAndInitMap();
    }

    private void createAndInitMap() {
	recursivCheck = new HashMap<>();
	recursivCheck.put(RETURNING, 0);
	recursivCheck.put(POST_ENVASION, 0);
	recursivCheck.put(PASSING, 0);
    }

    public EvasionStateMachine(Detector detector) {
	this(detector, 4);
    }

    @Override
    public void handlePostConditions(Grid grid, Moveable moveable) {
	super.handlePostConditions(grid, moveable);
	switch (evasionState) {
	case DEFAULT:
	    handleDefaultState(grid, moveable);
	    break;
	case ENVASION:
	    System.err.println("ENVASION (" + moveable.getPosition() + "'");
	    handleEvasionState(grid, moveable);
	    break;
	case POST_ENVASION:
	    System.err.println("POST_ENVASION (" + moveable.getPosition() + "'");
	    handlePostEvasion(moveable);
	    break;
	case PASSING:
	    System.err.println("PASSING (" + moveable.getPosition() + "'");
	    handlePassing(grid, moveable);
	    break;
	case RETURNING:
	    System.err.println("RETURNING (" + moveable.getPosition() + "'");
	    handleReturning();
	    break;
	default:
	    throw new IllegalStateException("Unknown state'" + evasionState + "'");
	}
    }

    private void handleDefaultState(Grid grid, Moveable moveable) {
	boolean isEvasion = check4Evasion(grid, moveable);
	if (isEvasion) {
	    evasionState = ENVASION;
	    positionBeforeEvasion = Positions.of(moveable.getPosition());
	    handleEvasionState(grid, moveable);
	}
    }

    private void handleEvasionState(Grid grid, Moveable moveable) {

	double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
	addExecutorIfNecessary(avoidAngle, () -> moveable.makeTurn(-avoidAngle));
	moveable.makeTurn(avoidAngle);
	checkSurrounding(grid, moveable);
	evasionState = POST_ENVASION;
    }

    private void handlePostEvasion(Moveable moveable) {
	boolean isAngleCorrectionNecessary = isAngleCorrectionNecessary(positionBeforeEvasion, moveable);
	if (isAngleCorrectionNecessary) {
	    adjustDirection(positionBeforeEvasion, moveable);
	}
	evasionState = PASSING;
    }

    private void handlePassing(Grid grid, Moveable moveable) {

	if (isPassingUnnecessary(grid, moveable)) {
	    // Since we are done with passing, lets go to the next state
	    evasionState = RETURNING;
	}
    }

    private boolean isPassingUnnecessary(Grid grid, Moveable moveable) {
	Position position = moveable.getPosition();
	boolean isDistanceFarEnough = positionBeforeEvasion.calcDistanceTo(position) > passingDistance;
	return isDistanceFarEnough && !check4Evasion(grid, moveable);
    }

    private void handleReturning() {

	if (!isMethodCallAllowed(RETURNING)) {
	    return;
	}
	handleReturningInternal();
    }

    private void handleReturningInternal() {
	registerForRecursivCall(RETURNING);
	for (int i = reverseExecutors.size(); i > 0; i--) {
	    MoveableExecutor moveableExecutor = reverseExecutors.get(i - 1);
	    moveableExecutor.execute();
	}
	reverseExecutors.clear();
	deregisterForRecursivCall(RETURNING);
	evasionState = EvasionStates.DEFAULT;
    }

    private boolean isAngleCorrectionNecessary(Position position, Moveable moveable) {

	Position movPos = moveable.getPosition();
	return !movPos.getDirection().equals(position.getDirection());
    }

    private void adjustDirection(Position startPos, Moveable moveable) {

	double calcAbsolutAngle = startPos.getDirection().getAngle();
	double effectAngle2Turn = getAngle2Turn(moveable, calcAbsolutAngle);
	addExecutorIfNecessary(effectAngle2Turn, () -> moveable.moveMakeTurnAndForward(effectAngle2Turn));
	moveable.moveMakeTurnAndForward(-effectAngle2Turn);
    }

    private double getAngle2Turn(Moveable moveable, double calcAbsolutAngle) {
	return -(moveable.getPosition().getDirection().getAngle() - calcAbsolutAngle);
    }

    private void addExecutorIfNecessary(double angle2Turn, MoveableExecutor moveableExec) {
	if (angle2Turn != 0.0d) {
	    reverseExecutors.add(moveableExec);
	}
    }

    private void registerForRecursivCall(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	recursivCheck.put(state, counter + 1);
    }

    private void deregisterForRecursivCall(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	recursivCheck.put(state, counter - 1);
    }

    private boolean isMethodCallAllowed(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	return counter.intValue() < 1;
    }

    @FunctionalInterface
    private static interface MoveableExecutor {
	public void execute();
    }
}

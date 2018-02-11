/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import static com.myownb3.piranha.moveables.helper.EvasionStates.NONE;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;

/**
 * An {@link EnvasionMoveableHelper} improves the default helper with functions
 * for evasion
 * 
 * @author Dominic
 *
 */
public class EnvasionMoveableHelper extends MoveableHelper {

    private Detector detector;
    private boolean isEvasionManeuverEnabled;
    private boolean isEvasionManeuverCorrectionEnabled;
    private boolean isEvasion;
    private Position positionBeforeEvasion;
    private EvasionStates evasionState;

    /**
     * 
     */
    public EnvasionMoveableHelper(Detector detector, boolean isEvasionManeuverEnabled,
	    boolean isEvasionManeuverCorrectionEnabled) {
	super();
	this.detector = detector;
	this.isEvasionManeuverEnabled = isEvasionManeuverEnabled;
	this.isEvasionManeuverCorrectionEnabled = isEvasionManeuverCorrectionEnabled;
	isEvasion = false;
	positionBeforeEvasion = null;
	evasionState = NONE;
    }

    public EnvasionMoveableHelper(Detector detector, boolean isEvasionManeuverEnabled) {
	this(detector, isEvasionManeuverEnabled, false);
    }

    @Override
    public void checkPostConditions(Grid grid, Moveable moveable) {

	checkSurrounding(grid, moveable);
	handleEvasionManeuverIfNecessary(grid, moveable);
    }

    private void handleEvasionManeuverIfNecessary(Grid grid, Moveable moveable) {

	List<GridElement> gridElements = grid.getSurroundingGridElements(moveable);

	boolean wasEvasionBefore = isEvasion;
	isEvasion = gridElements.stream()//
		.anyMatch(gridElement -> detector.isEvasion(gridElement));

	if (isEvasion && isEvasionManeuverEnabled) {

	    if (positionBeforeEvasion == null && isEvasionManeuverCorrectionEnabled) {
		positionBeforeEvasion = Positions.of(moveable.getPosition());
	    }
	    handleEvasionManeuver(grid, moveable);
	} else if (positionBeforeEvasion != null) {
	    handleEvasionCorrectionManeuver(moveable, positionBeforeEvasion, wasEvasionBefore, isEvasion);
	}
    }

    private void handleEvasionCorrectionManeuver(Moveable moveable, Position originPosition, boolean wasEvasionBefore,
	    boolean isEvasion) {

	if (isAngleCorrectionNecessary(originPosition, moveable)) {

	    evasionState = EvasionStates.POST_ENVASION;
	    // Since we just evaded another GridElement we need to move forward first,
	    // otherwise this will end in a FlipFlop
	    if (wasEvasionBefore && !isEvasion) {
		moveable.moveForward(2);
	    }
	    if (isAngleCorrectionNecessary(originPosition, moveable)) {
		adjustDirection(originPosition, moveable);
	    }
	} else if (needs2Pass(originPosition, moveable)) {
	    evasionState = EvasionStates.PASSING;
	    moveable.moveForward();
	} else if (isXAxisCorrectionnecessary(originPosition, moveable)) {
	    evasionState = EvasionStates.RETURNING;
	    correctXAxis(originPosition, moveable);

	}
    }

    private boolean needs2Pass(Position originPosition, Moveable moveable) {
	Direction originDirection = originPosition.getDirection();
	boolean hasSameDirection = originDirection.equals(moveable.getPosition().getDirection());
	boolean needs2MoveForward = originPosition.calcDistanceTo(moveable.getPosition()) > 0;
	return hasSameDirection && needs2MoveForward;
    }

    private void correctXAxis(Position originPosition, Moveable moveable) {
	Position moveablePos = moveable.getPosition();
	System.err.println();
    }

    private boolean isXAxisCorrectionnecessary(Position originPosition, Moveable moveable) {
	return originPosition.getX() != moveable.getPosition().getX();
    }

    private boolean isAngleCorrectionNecessary(Position position, Moveable moveable) {
	return !moveable.getPosition().getDirection().equals(position.getDirection());
    }

    private void adjustDirection(Position startPos, Moveable moveable) {

	double calcAbsolutAngle = startPos.calcAbsolutAngle();
	double effectAngle2Turn = getAngle2Turn(moveable, calcAbsolutAngle);
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

    private void checkSurrounding(Grid grid, Moveable moveable) {
	List<GridElement> gridElements = grid.getSurroundingGridElements(moveable);
	gridElements.stream()//
		.forEach(gridElement -> detector.detectObject(gridElement, moveable.getPosition()));
    }

    private void handleEvasionManeuver(Grid grid, Moveable moveable) {

	double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());

	moveable.makeTurn(avoidAngle);
	checkSurrounding(grid, moveable);
    }
}

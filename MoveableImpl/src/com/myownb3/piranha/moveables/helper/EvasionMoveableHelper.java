/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import static com.myownb3.piranha.moveables.helper.EvasionStates.DEFAULT;
import static com.myownb3.piranha.moveables.helper.EvasionStates.ENVASION;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;

/**
 * An {@link EvasionMoveableHelper} improves the default helper with functions
 * for evasion
 * 
 * @author Dominic
 *
 */
public class EvasionMoveableHelper extends DetectableMoveableHelper {

    protected EvasionStates evasionState;

    public EvasionMoveableHelper(Detector detector) {
	super(detector);
	evasionState = DEFAULT;
    }

    @Override
    public void checkPostConditions(Grid grid, Moveable moveable) {

	super.checkPostConditions(grid, moveable);

	handleEvasion4CurrentState(grid, moveable);
    }

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
	    // Fall through
	case PASSING:
	    // Fall through
	    break;
	case RETURNING:
	    // Fall through
	default:
	    // Nothing to do since we are not handling all events
	}
    }

    protected void handleDefaultState(Grid grid, Moveable moveable, boolean isEvasion) {
	if (isEvasion) {
	    evasionState = ENVASION;
	    handleEvasionState(grid, moveable);
	}
    }

    protected void handleEvasionState(Grid grid, Moveable moveable) {

	double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());

	if (avoidAngle != 0) {
	    moveable.makeTurn(avoidAngle);
	}
	checkSurrounding(grid, moveable);
    }

    protected void checkSurrounding(Grid grid, Moveable moveable) {
	List<GridElement> gridElements = grid.getSurroundingGridElements(moveable);
	gridElements.stream()//
		.forEach(gridElement -> detector.detectObject(gridElement, moveable.getPosition()));
    }
}
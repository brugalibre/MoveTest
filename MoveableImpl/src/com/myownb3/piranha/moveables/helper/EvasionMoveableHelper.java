/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import static com.myownb3.piranha.moveables.helper.EvasionStates.DEFAULT;
import static com.myownb3.piranha.moveables.helper.EvasionStates.ENVASION;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;

/**
 * An {@link EvasionMoveableHelper} improves the
 * {@link DetectableMoveableHelper} with additionally evasion functions such as
 * initializing an evasion maneuvre
 * 
 * @author Dominic
 *
 */
public class EvasionMoveableHelper extends DetectableMoveableHelper {

    protected EvasionStates evasionState;
    protected Position positionBeforeEvasion;

    public EvasionMoveableHelper(Detector detector) {
	super(detector);
	evasionState = DEFAULT;
	positionBeforeEvasion = null;
    }

    @Override
    public void handlePostConditions(Grid grid, Moveable moveable) {

	super.handlePostConditions(grid, moveable);

	handleEvasion4CurrentState(grid, moveable);
    }

    protected void handleEvasion4CurrentState(Grid grid, Moveable moveable) {

	switch (evasionState) {
	case DEFAULT:
	    handleDefaultState(grid, moveable);
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

    protected void handleDefaultState(Grid grid, Moveable moveable) {
	boolean isEvasion = check4Evasion(grid, moveable);
	if (isEvasion) {
	    evasionState = ENVASION;
	    positionBeforeEvasion = Positions.of(moveable.getPosition());
	    handleEvasionState(grid, moveable);
	}
    }

    protected void handleEvasionState(Grid grid, Moveable moveable) {

	double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
	moveable.makeTurn(avoidAngle);
	checkSurrounding(grid, moveable);
    }
}
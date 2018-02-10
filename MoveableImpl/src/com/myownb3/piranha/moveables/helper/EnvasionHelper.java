/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;

/**
 * An {@link EnvasionHelper} improves the default helper with functions for
 * evasion
 * 
 * @author Dominic
 *
 */
public class EnvasionHelper extends Helper {

    private Detector detector;
    private boolean isEvasionManeuverEnabled;

    /**
     * 
     */
    public EnvasionHelper(Detector detector, boolean isEvasionManeuverEnabled) {
	super();
	this.detector = detector;
	this.isEvasionManeuverEnabled = isEvasionManeuverEnabled;
    }

    @Override
    public void checkPostConditions(Grid grid, Moveable moveable) {

	checkSurrounding(grid, moveable);
	handleEvasionManeuverIfNecessary(grid, moveable);
    }

    private void handleEvasionManeuverIfNecessary(Grid grid, Moveable moveable) {

	List<GridElement> gridElements = grid.getSurroundingGridElements(moveable);
	boolean isEvasion = gridElements.stream()//
		.anyMatch(gridElement -> detector.isEvasion(gridElement));

	if (isEvasion && isEvasionManeuverEnabled) {
	    handleEvasionManeuver(grid, moveable);
	}
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

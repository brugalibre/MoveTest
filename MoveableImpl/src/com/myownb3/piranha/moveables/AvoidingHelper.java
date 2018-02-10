/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.List;

import com.myownb3.piranha.grid.Detector;
import com.myownb3.piranha.grid.Grid;

/**
 * @author Dominic
 *
 */
public class AvoidingHelper extends Helper {

    private Detector detector;
    private boolean isEvasionManeuverEnabled;

    /**
     * 
     */
    public AvoidingHelper(Detector detector, boolean isEvasionManeuverEnabled) {
	super();
	this.detector = detector;
	this.isEvasionManeuverEnabled = isEvasionManeuverEnabled;
    }

    @Override
    public void checkPostConditions(Moveable moveable, Grid grid) {

	checkSurrounding(moveable, grid);
	handleEvasionManeuverIfNecessary(moveable, grid);
    }

    private void handleEvasionManeuverIfNecessary(Moveable moveable, Grid grid) {

	List<GridElement> gridElements = grid.getSurroundingGridElements(moveable);
	boolean isEvasion = gridElements.stream()//
		.anyMatch(gridElement -> detector.isEvasion(gridElement));

	if (isEvasion && isEvasionManeuverEnabled) {
	    handleEvasionManeuver(moveable, grid);
	}
    }

    private void checkSurrounding(Moveable moveable, Grid grid) {
	List<GridElement> gridElements = grid.getSurroundingGridElements(moveable);
	gridElements.stream()//
		.forEach(gridElement -> detector.detectObject(gridElement, moveable.getPosition()));
    }

    private void handleEvasionManeuver(Moveable moveable, Grid grid) {

	double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
	moveable.makeTurn(avoidAngle);
	checkSurrounding(moveable, grid);
    }
}

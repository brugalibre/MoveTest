/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.List;

import com.myownb3.piranha.grid.Detector;

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
    public void checkPostConditions(AbstractMovable abstractMovable) {

	checkSurrounding(abstractMovable);
	handleEvasionManeuverIfNecessary(abstractMovable);
    }

    private void handleEvasionManeuverIfNecessary(AbstractMovable avoidableMovable) {

	List<GridElement> gridElements = avoidableMovable.grid.getSurroundingGridElements(avoidableMovable);
	boolean isEvasion = gridElements.stream()//
		.anyMatch(gridElement -> detector.isEvasion(gridElement));

	if (isEvasion && isEvasionManeuverEnabled) {
	    handleEvasionManeuver(avoidableMovable);
	}
    }

    private void checkSurrounding(AbstractMovable avoidableMovable) {
	List<GridElement> gridElements = avoidableMovable.grid.getSurroundingGridElements(avoidableMovable);
	gridElements.stream()//
		.forEach(gridElement -> detector.detectObject(gridElement, avoidableMovable.position));
    }

    private void handleEvasionManeuver(AbstractMovable avoidableMovable) {

	double avoidAngle = detector.getEvasionAngleRelative2(avoidableMovable.position);
	avoidableMovable.makeTurn(avoidAngle);
	checkSurrounding(avoidableMovable);
    }
}

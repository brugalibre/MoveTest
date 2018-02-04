/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.List;

import com.myownb3.piranha.grid.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public abstract class AvoidableMovable extends AbstractMovable implements AvoidableMoveable {

    private Detector detector;
    private boolean isEvasionManeuverEnabled;

    /**
     * Creates a new {@link AvoidableMovable}
     * 
     * @param grid
     *            the {@link Grid} this {@link Moveable} is placed on
     * @param position
     *            the initial {@link Position} of this {@link Moveable}
     * @param detector
     *            the {@link Detector}
     * @param isEvasionManeuverEnabled
     *            <code>true</code> if evasion maneuver is enabled
     */
    public AvoidableMovable(Grid grid, Position position, Detector detector, boolean isEvasionManeuverEnabled) {
	super(grid, position);
	this.isEvasionManeuverEnabled = isEvasionManeuverEnabled;
	this.detector = detector;
	checkSurroundingAndHandleEvasionManeuver();
    }

    /**
     * Creates a new {@link AvoidableMovable}. By default the avoiding procedure is
     * disabled
     * 
     * @param grid
     *            the {@link Grid} this {@link Moveable} is placed on
     * @param position
     *            the initial {@link Position} of this {@link Moveable}
     * @param detector
     *            the {@link Detector}
     */
    public AvoidableMovable(Grid grid, Position position, Detector detector) {
	this(grid, position, detector, false);
    }

    @Override
    public void moveForward() {
	super.moveForward();
	checkSurroundingAndHandleEvasionManeuver();
    }

    @Override
    public void moveBackward() {
	super.moveBackward();
	checkSurroundingAndHandleEvasionManeuver();
    }

    @Override
    public void makeTurn(double degree) {
	super.makeTurn(degree);
	checkSurroundingAndHandleEvasionManeuver();
    }

    private void checkSurroundingAndHandleEvasionManeuver() {

	checkSurrounding();
	handleEvasionManeuverIfNecessary();
    }

    private void handleEvasionManeuverIfNecessary() {

	List<GridElement> gridElements = grid.getSurroundingGridElements(this);
	boolean isEvasion = gridElements.stream()//
		.anyMatch(gridElement -> detector.isEvasion(gridElement));

	if (isEvasion && isEvasionManeuverEnabled) {
	    handleEvasionManeuver();
	}
    }

    private void checkSurrounding() {
	List<GridElement> gridElements = grid.getSurroundingGridElements(this);
	gridElements.stream()//
		.forEach(gridElement -> detector.detectObject(gridElement, position));
    }

    private void handleEvasionManeuver() {

	double avoidAngle = detector.getEvasionAngleRelative2(position);
	super.makeTurn(avoidAngle);
	checkSurrounding();
    }
}
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
    private boolean isEvasionProcedureEnabled;

    /**
     * Creates a new {@link AvoidableMovable}
     * 
     * @param grid
     *            the {@link Grid} this {@link Moveable} is placed on
     * @param position
     *            the initial {@link Position} of this {@link Moveable}
     * @param detector
     *            the {@link Detector}
     * @param isEvasionProcedureEnabled
     *            <code>true</code> if avoiding procedure is enabled
     */
    public AvoidableMovable(Grid grid, Position position, Detector detector, boolean isEvasionProcedureEnabled) {
	super(grid, position);
	this.isEvasionProcedureEnabled = isEvasionProcedureEnabled;
	this.detector = detector;
	checkSurroundingAndHandleEvasionManeuve();
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
	checkSurroundingAndHandleEvasionManeuve();
    }

    @Override
    public void moveBackward() {
	super.moveBackward();
	checkSurroundingAndHandleEvasionManeuve();
    }

    @Override
    public void makeTurn(double degree) {
	super.makeTurn(degree);
	checkSurroundingAndHandleEvasionManeuve();
    }

    private void checkSurroundingAndHandleEvasionManeuve() {

	checkSurrounding();
	handleEvasionManeuveIfNecessary();
    }

    private void handleEvasionManeuveIfNecessary() {

	List<GridElement> gridElements = grid.getSurroundingGridElements(this);
	boolean isEvasion = gridElements.stream()//
		.anyMatch(gridElement -> detector.isEvasion(gridElement));

	if (isEvasion && isEvasionProcedureEnabled) {
	    handleEvasionManeuve();
	}
    }

    /**
     * @param gridElements
     */
    private void checkSurrounding() {
	List<GridElement> gridElements = grid.getSurroundingGridElements(this);
	gridElements.stream()//
		.forEach(gridElement -> detector.detectObject(gridElement, position));
    }

    private void handleEvasionManeuve() {

	double avoidAngle = detector.getEvasionAngleRelative2(position);
	super.makeTurn(avoidAngle);
	checkSurrounding();
    }

    // @Override
    // public void continueAvoiding() {
    // // TODO Auto-generated method stub
    // }
}
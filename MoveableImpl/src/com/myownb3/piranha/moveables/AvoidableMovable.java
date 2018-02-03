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
    private boolean isAvoidingProcedureEnabled;

    /**
     * Creates a new {@link AvoidableMovable}
     * 
     * @param grid
     *            the {@link Grid} this {@link Moveable} is placed on
     * @param position
     *            the initial {@link Position} of this {@link Moveable}
     * @param detector
     *            the {@link Detector}
     * @param isAvoidingProcedureEnabled
     *            <code>true</code> if avoiding procedure is enabled
     */
    public AvoidableMovable(Grid grid, Position position, Detector detector, boolean isAvoidingProcedureEnabled) {
	super(grid, position);
	this.detector = detector;
	checkSurrounding();
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
	super(grid, position);
	this.detector = detector;
	checkSurrounding();
    }

    @Override
    public void moveForward() {
	super.moveForward();
	checkSurrounding();
    }

    @Override
    public void moveBackward() {
	super.moveBackward();
	checkSurrounding();
    }

    @Override
    public void makeTurn(double degree) {
	super.makeTurn(degree);
	checkSurrounding();
    }

    private void checkSurrounding() {

	List<GridElement> gridElements = grid.getSurroundingGridElements(this);

	gridElements.stream()//
		.forEach(gridElement -> detector.detectObject(gridElement, position));
	boolean isAvoiding = gridElements.stream()//
		.anyMatch(gridElement -> detector.isAvoiding(gridElement));

	boolean avoidingProcedureEnabled = isAvoidingProcedureEnabled(isAvoiding);
	if (avoidingProcedureEnabled) {
	    handleAvoidingProcedure();
	}
    }

    private boolean isAvoidingProcedureEnabled(boolean isAvoiding) {
	return isAvoiding && isAvoidingProcedureEnabled;
    }

    private void handleAvoidingProcedure() {

	double avoidAngle = detector.getAvoidAngleRelative2(position);
	super.makeTurn(avoidAngle);
    }

    @Override
    public void continueAvoiding() {
	// TODO Auto-generated method stub
    }
}
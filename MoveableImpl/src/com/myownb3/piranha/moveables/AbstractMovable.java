/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.List;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public abstract class AbstractMovable extends AbstractGridElement implements Moveable {

    private Detector detector;

    /**
     * @param position
     */
    public AbstractMovable(Grid grid, Position position) {
	this(grid, position, new DetectorImpl());
    }

    /**
     * @param grid
     * @param position
     * @param detector
     */
    public AbstractMovable(Grid grid, Position position, Detector detector) {
	super(position, grid);
	this.detector = detector;
	checkSurrounding();
    }

    @Override
    public void moveForward() {
	position = grid.moveForward(position);
	checkSurrounding();
    }

    @Override
    public void moveForward(int amount) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveForward();
	}
    }

    @Override
    public void moveBackward() {
	position = grid.moveBackward(position);
	checkSurrounding();
    }

    private void checkSurrounding() {

	List<GridElement> gridElements = ((DefaultGrid) grid).getGridElements();

	gridElements.stream()//
		.filter(gridElement -> !gridElement.equals(this))//
		.forEach(gridElement -> detector.detectObject(gridElement, position));
    }

    @Override
    public void moveBackward(int amount) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveBackward();
	}
    }

    @Override
    public void turnLeft() {
	makeTurn(90);
    }

    @Override
    public void makeTurn(int degree) {
	position.rotate(degree);
	checkSurrounding();
    }

    @Override
    public void turnRight() {
	makeTurn(-90);
    }

    private void verifyAmount(int amount) {
	if (amount <= 0) {
	    throw new IllegalArgumentException("The value 'amount' must not be zero or below!");
	}
    }

    @Override
    public boolean hasObjectDetected(GridElement gridElement) {

	return detector.hasObjectDetected(gridElement);
    }

    @Override
    public boolean isAvoiding(GridElement gridElement) {
	return detector.isAvoiding(gridElement);
    }
}
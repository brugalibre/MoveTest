/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.List;
import java.util.Optional;

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
		.allMatch(gridElement -> detector.hasObjectDetected(gridElement, position));
    }

    @Override
    public void moveBackward(int amount) {
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
    }

    @Override
    public void turnRight() {
	makeTurn(-90);
    }

    @Override
    public boolean hasObjectDetected(GridElement gridElement) {

	return detector.hasObjectDetected(gridElement, position);
    }

    @Override
    public boolean isAvoiding() {
	return detector.isAvoiding();
    }
}
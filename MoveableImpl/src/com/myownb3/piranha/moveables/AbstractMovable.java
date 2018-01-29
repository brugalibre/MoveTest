/**
 * 
 */
package com.myownb3.piranha.moveables;

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
    }

    @Override
    public void moveForward() {
	position = grid.moveForward(position);
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
    public boolean isAvoiding(GridElement gridElement) {
	return detector.isAvoiding(gridElement, position);
    }
}
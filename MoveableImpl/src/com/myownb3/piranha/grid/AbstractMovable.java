/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.AbstractGridElement;
import com.myownb3.piranha.moveables.GridElement;
import com.myownb3.piranha.moveables.Moveable;

/**
 * @author Dominic
 *
 */
public abstract class AbstractMovable extends AbstractGridElement implements Moveable {

    private int detectorReach = 8;
    private int detectorAngle = 45;

    /**
     * @param position
     */
    public AbstractMovable(Grid grid, Position position) {
	super(position, grid);
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
    public boolean hasObjectRecognized(GridElement gridElement) {

	Position gridElemPos = gridElement.getPosition();

	double distance = gridElemPos.calcDistanceTo(position);
	if (distance > detectorReach) {
	    return false;
	}

	double gridElementAngle = gridElemPos.calcAbsolutAngle();
	double ourAngle = position.getDirection().getAngle();

	return ourAngle + (detectorAngle / 2) >= gridElementAngle && gridElementAngle >= ourAngle - (detectorAngle / 2);
    }
}
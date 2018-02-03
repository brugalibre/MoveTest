/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * The {@link AbstractMovable} is responsible for doing the basic move elements
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMovable extends AbstractGridElement implements Moveable {

    /**
     * @param grid
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
	verifyAmount(amount);
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
    public void makeTurn(double degree) {
	position.rotate(degree);
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
}
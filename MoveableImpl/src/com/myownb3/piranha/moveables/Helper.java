/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.AbstractMoveable.Updater;

/**
 * @author Dominic
 *
 */
public class Helper {

    protected Helper next;

    public void moveForward(Grid grid, Moveable movable, Updater updater) {
	Position newPos = grid.moveForward(movable.getPosition());
	updater.update(movable, newPos);
	checkPostConditions(movable, grid);
    }

    public void makeTurn(Grid grid, Moveable moveable, double degree) {
	moveable.getPosition().rotate(degree);
	checkPostConditions(moveable, grid);
    }

    public void moveBackward(Moveable movable, Grid grid, Updater updater) {
	Position newPos = grid.moveBackward(movable.getPosition());
	updater.update(movable, newPos);
	checkPostConditions(movable, grid);
    }

    public void moveBackward(Grid grid, Moveable movable, int amount, Updater updater) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveBackward(movable, grid, updater);
	}
    }

    public void moveForward(Grid grid, Moveable movable, int amount, Updater updater) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveForward(grid, movable, updater);
	}
    }

    void verifyAmount(int amount) {
	if (amount <= 0) {
	    throw new IllegalArgumentException("The value 'amount' must not be zero or below!");
	}
    }

    public void checkPostConditions(Moveable movable, Grid grid) {
	// nothing to do here
    }
}

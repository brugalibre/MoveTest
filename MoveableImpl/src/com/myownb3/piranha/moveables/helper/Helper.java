/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.AbstractMoveable.Updater;
import com.myownb3.piranha.moveables.Moveable;

/**
 * @author Dominic
 *
 */
public class Helper {

    public void moveForward(Grid grid, Moveable movable, Updater updater) {
	Position newPos = grid.moveForward(movable.getPosition());
	updater.update(movable, newPos);
	checkPostConditions(grid, movable);
    }

    public void makeTurn(Grid grid, Moveable moveable, double degree) {
	moveable.getPosition().rotate(degree);
	checkPostConditions(grid, moveable);
    }

    public void moveBackward(Moveable movable, Grid grid, Updater updater) {
	Position newPos = grid.moveBackward(movable.getPosition());
	updater.update(movable, newPos);
	checkPostConditions(grid, movable);
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

    public void checkPostConditions(Grid grid, Moveable movable) {
	// nothing to do here
    }
}

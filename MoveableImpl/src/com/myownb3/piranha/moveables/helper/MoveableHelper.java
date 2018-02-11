/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.Updater;

/**
 * @author Dominic
 *
 */
public class MoveableHelper {

    public void moveForward(Grid grid, Moveable moveable, Updater updater) {
	Position newPos = grid.moveForward(moveable.getPosition());
	updater.update(moveable, newPos);
	checkPostConditions(grid, moveable);
    }

    public void makeTurn(Grid grid, Moveable moveable, double degree) {
	moveable.getPosition().rotate(degree);
	checkPostConditions(grid, moveable);
    }

    public void moveBackward(Moveable moveable, Grid grid, Updater updater) {
	Position newPos = grid.moveBackward(moveable.getPosition());
	updater.update(moveable, newPos);
	checkPostConditions(grid, moveable);
    }

    public void moveBackward(Grid grid, Moveable moveable, int amount, Updater updater) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveBackward(moveable, grid, updater);
	}
    }

    public void moveForward(Grid grid, Moveable moveable, int amount, Updater updater) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveForward(grid, moveable, updater);
	}
    }

    void verifyAmount(int amount) {
	if (amount <= 0) {
	    throw new IllegalArgumentException("The value 'amount' must not be zero or below!");
	}
    }

    public void checkPostConditions(Grid grid, Moveable moveable) {
	// nothing to do here
    }
}

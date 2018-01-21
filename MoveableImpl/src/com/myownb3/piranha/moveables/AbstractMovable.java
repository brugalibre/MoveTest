/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public abstract class AbstractMovable implements Moveable {

    protected Position position;
    protected Grid grid;

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
    public void makeTurn(int dregree) {
	position.makeTurn(dregree);
    }

    @Override
    public void turnRight() {
	makeTurn(-90);
    }

    @Override
    public Position getPosition() {
	return position;
    }
}
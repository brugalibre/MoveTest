/**
 * 
 */
package com.myownb3.piranha;

/**
 * @author Dominic
 *
 */
public abstract class AbstractMovable implements Moveable {

    protected Position position;

    @Override
    public void moveForward() {

	position = position.moveForward();
    }

    @Override
    public void moveForward(int amount) {
	for (int i = 0; i < amount; i++) {
	    moveForward();
	}
    }

    @Override
    public void moveBackward() {
	position = position.moveBackward();
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
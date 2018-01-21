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
	position = position.moveBackwarts();
    }

    @Override
    public void turnLeft() {
	turnDegree(90);
    }

    @Override
    public void turnDegree(int dregree) {
	position.turnDegree(dregree);
    }

    @Override
    public void turnRight() {
	turnDegree(-90);
    }

    @Override
    public Position getPosition() {
	return position;
    }
}
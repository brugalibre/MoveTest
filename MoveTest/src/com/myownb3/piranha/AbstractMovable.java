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
    public void moveBackward() {
	position = position.moveBackwarts();
    }

    @Override
    public void turnLeft() {
	position.turnLeft();
    }

    @Override
    public void turnRight() {
	position.turnRight();
    }

    @Override
    public Position getPosition() {
	return position;
    }
}
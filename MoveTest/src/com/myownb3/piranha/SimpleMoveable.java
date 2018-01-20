/**
 * 
 */
package com.myownb3.piranha;

/**
 * @author Dominic
 *
 */
public class SimpleMoveable {

    private Position position;

    /**
     * @param position
     */
    public SimpleMoveable(Position position) {
	this.position = position;
    }

    /**
     * @return
     */
    public Position getPosition() {
	return position;
    }

    public void moveForward() {
	position = new Position(position.getX(), position.getY() + 1);
    }
}

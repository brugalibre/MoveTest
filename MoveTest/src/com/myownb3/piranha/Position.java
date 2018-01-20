/**
 * 
 */
package com.myownb3.piranha;

/**
 * @author Dominic
 *
 */
public class Position {

    private int y;
    private int x;
    private Direction direction = Direction.N;

    /**
     * @param x
     * @param y
     */
    public Position(int x, int y) {
	this(Direction.N, x, y);
    }

    /**
     * @param direction
     * @param x
     * @param y
     */
    public Position(Direction direction, int x, int y) {
	this.direction = direction;
	this.x = x;
	this.y = y;
    }

    public void turnRight() {
	direction = direction.turnRight();
    }

    public void turnLeft() {
	direction = direction.turnLeft();
    }

    public Position moveBackwarts() {

	double newX = x + direction.getBackwardX();
	double newY = y + direction.getBackwardY();
	return new Position(direction, (int) newX, (int) newY);
    }

    public Position moveForward() {

	double newX = x + direction.getForwardX();
	double newY = y + direction.getForwardY();
	return new Position(direction, (int) newX, (int) newY);
    }

    /**
     * @return the direction
     */
    public final Direction getDirection() {
	return this.direction;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + this.x;
	result = prime * result + this.y;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Position other = (Position) obj;
	if (this.x != other.x)
	    return false;
	if (this.y != other.y)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Direction: '" + direction + "', X-Axis: '" + x + "', Y-Axis: '" + y + "'";
    }
}

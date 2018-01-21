/**
 * 
 */
package com.myownb3.piranha;

import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class Position {

    private double y;
    private double x;
    private Direction direction;

    /**
     * @param x
     * @param y
     */
    public Position(double x, double y) {
	this(Direction.N, x, y);
    }

    public Position(Direction direction, double x, double y) {
	this.direction = direction;
	this.x = MathUtil.roundThreePlaces(x);
	this.y = MathUtil.roundThreePlaces(y);
    }

    /**
     * @param dregree
     */
    public void makeTurn(int dregree) {
	direction = direction.makeTurn(dregree);
    }

    /**
     * Moves the position of this {@link Position} backward by 1 unit
     * 
     * @return the new Position
     */
    public Position moveBackward() {

	double newX = x + direction.getBackwardX();
	double newY = y + direction.getBackwardY();
	return new Position(direction, newX, newY);
    }

    /**
     * Moves the position of this {@link Position} forward by 1 unit
     * 
     * @return the new Position
     */
    public Position moveForward() {

	double newX = x + direction.getForwardX();
	double newY = y + direction.getForwardY();
	return new Position(direction, newX, newY);
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
	double result = 1;
	result = prime * result + this.x;
	result = prime * result + this.y;
	return (int) result;
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

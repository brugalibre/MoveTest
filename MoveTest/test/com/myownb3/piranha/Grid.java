/**
 * 
 */
package com.myownb3.piranha;

/**
 * The {@link Grid} defines the place where a {@link Moveable} can be moved and
 * {@link Position}s can be placed.
 * 
 * A Grid makes sure, that {@link Position} are not misplaced. That means, if
 * any Position is placed out of bounds the Grid places the Position on its
 * reverse location
 * 
 * @author Dominic
 *
 */
public class Grid {

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    /**
     * Creates a default Grid which has a size of 10 to 10
     */
    public Grid() {
	this(10, 10);
    }

    /**
     * 
     * @param maxY
     * @param maxX
     */
    public Grid(int maxY, int maxX) {
	this(maxY, maxX, 0, 0);
    }

    /**
     * 
     * @param maxY
     * @param maxX
     */
    public Grid(int maxY, int maxX, int minX, int minY) {
	this.maxY = maxY;
	this.maxX = maxX;
	this.minY = minY;
	this.minX = minX;
    }

    /**
     * Moves the position of this {@link Position} backward by 1 unit
     * 
     * @param position
     *            the current Position to move backward
     * @return the new moved Position
     */
    public Position moveBackward(Position position) {

	Direction direction = position.getDirection();
	double newX = position.getX() + direction.getBackwardX();
	double newY = position.getY() + direction.getBackwardY();

	newX = checkNewXValue(newX);
	newY = checkNewYValue(newY);
	return new Position(direction, newX, newY);
    }

    /**
     * Moves the position of this {@link Position} forward by 1 unit
     * 
     * @param position
     *            the current Position to move forward
     * @return the new moved Position
     */
    public Position moveForward(Position position) {

	Direction direction = position.getDirection();
	double newX = position.getX() + direction.getForwardX();
	double newY = position.getY() + direction.getForwardY();

	newX = checkNewXValue(newX);
	newY = checkNewYValue(newY);
	return new Position(direction, newX, newY);
    }

    /**
     * Checks weather or not the new value is in bounds. If not, the value is
     * swapped
     * 
     * @param newY
     * @return the new y-Value within grid bounds
     */
    private double checkNewYValue(double newY) {

	if (newY > maxY) {
	    newY = (newY - maxY) + minY;
	} else if (newY < minY) {
	    newY = maxY - (minY - newY);
	}
	return newY;
    }

    /**
     * Checks weather or not the new value is in bounds. If not, the value is
     * swapped
     * 
     * @param newX
     * @return the new x-Value within grid bounds
     */
    private double checkNewXValue(double newX) {

	if (newX > maxX) {
	    newX = (newX - maxX) + minX;
	} else if (newX < minX) {
	    newX = maxX - (minX - newX);
	}

	return newX;
    }

    /**
     * @formatter:off
     * 
     *10 ___________ 
     * 	|__|__|__|__|
     * 	|__|__|__|__|
     * 	|__|__|__|__|
     * 	|__|__|__|__|
     * 5|__|__|__|__|
     *  5	     10 
     * 
     * @formatter:on
     */
}

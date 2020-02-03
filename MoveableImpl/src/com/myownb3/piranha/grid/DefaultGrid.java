/**
 * 
 */
package com.myownb3.piranha.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.exception.GridElementOutOfBoundsException;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;

/**
 * The most simple implementation of a {@link Grid} which simply moves a
 * {@link Position} forward and backward
 * 
 * @author Dominic
 *
 */
public class DefaultGrid implements Grid {

    private List<GridElement> gridElements;
    private boolean checkLowerBoundarys;
    protected int maxX;
    protected int maxY;
    protected int minX;
    protected int minY;

    /**
     * Creates a default Grid which has a size of 10 to 10
     */
    public DefaultGrid() {
	this(10, 10);
    }

    /**
     * Creates a new {@link Grid} with the given maximal, minimal-x and maximal,
     * minimal -y values
     * 
     * @param maxY
     *            the maximal y-axis value
     * @param maxX
     *            the maximal x-axis value
     */
    public DefaultGrid(int maxY, int maxX) {
	this(maxY, maxX, 0, 0);
	this.checkLowerBoundarys = false;
    }

    /**
     * Creates a new {@link Grid} with the given maximal, minimal-x and maximal,
     * minimal -y values
     * 
     * @param maxY
     *            the maximal y-axis value
     * @param maxX
     *            the maximal x-axis value
     * @param minX
     *            the minimal x-axis value
     * @param minY
     *            the minimal y-axis value
     */
    public DefaultGrid(int maxY, int maxX, int minX, int minY) {
	this.maxY = maxY;
	this.maxX = maxX;
	this.minY = minY;
	this.minX = minX;
	this.checkLowerBoundarys = true;
	gridElements = new ArrayList<>();
    }

    /**
     * Moves the position of this {@link Position} backward by 1 unit
     * 
     * @param position
     *            the current Position to move backward
     * @return the new moved Position
     */
    @Override
    public Position moveBackward(Position position) {

	Direction direction = position.getDirection();
	double newX = getNewXValue(position, direction.getBackwardX());
	double newY = getNewYValue(position, direction.getBackwardY());
	checkBounds(newX, newY);
	return Positions.of(direction, newX, newY);
    }

    /**
     * Moves the position of this {@link Position} forward by 1 unit
     * 
     * @param position
     *            the current Position to move forward
     * @return the new moved Position
     */
    @Override
    public Position moveForward(Position position) {

	Direction direction = position.getDirection();
	double newX = getNewXValue(position, direction.getForwardX());
	double newY = getNewYValue(position, direction.getForwardY());
	checkBounds(newX, newY);

	return Positions.of(direction, newX, newY);
    }

    @Override
    public boolean containsElement(GridElement gridElement) {
	return gridElements.contains(gridElement);
    }

    @Override
    public void addElement(GridElement gridElement) {
	checkBounds(gridElement.getPosition());
	gridElements.add(gridElement);
    }

    /**
     * @param position
     * @param forwardY
     *            the amount of forward units for the y-axis
     * @return
     */
    protected double getNewYValue(Position position, double forwardY) {
	return position.getY() + forwardY;
    }

    /**
     * @param position
     * @param forwardX
     *            the amount of forward units for the x-axis
     * @return the new x-value
     */
    protected double getNewXValue(Position position, double forwardX) {
	return position.getX() + forwardX;
    }

    @Override
    public List<GridElement> getSurroundingGridElements(GridElement gridElement) {
	return gridElements.stream()//
		.filter(currenGridEl -> !currenGridEl.equals(gridElement))//
		.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public Dimension getDimension() {
	return new DimensionImpl(minX, minY, maxX - minX, maxY - minY);
    }

    private void checkBounds(Position position) {
	checkBounds(position.getX(), position.getY());
    }

    /**
     * Verifies weather or not the given coordinates are within the bounds
     * 
     * @param newX
     * @param newY
     */
    private void checkBounds(double newX, double newY) {
	if (outOfUpperBounds(newY, newX) || outOfLowerBounds(newY, newX)) {
	    throw new GridElementOutOfBoundsException(
		    "The bounds '" + newX + "', '" + newY + "' are out of bounds for this Grid '\n" + this);
	}
    }

    private boolean outOfLowerBounds(double newY, double newX) {
	return checkLowerBoundarys && (newY < minY || newX < minX);
    }

    private boolean outOfUpperBounds(double newY, double newX) {
	return newY > maxY || newX > maxX;
    }

    @Override
    public String toString() {

	return "Max x:'" + maxX + ", Min x:'" + minX + "; Max y:'" + maxY + ", Min y:'" + minY;
    }
}
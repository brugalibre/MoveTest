/**
 * 
 */
package com.myownb3.piranha.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.moveables.Direction;
import com.myownb3.piranha.moveables.GridElement;

/**
 * The most simple implementation of a {@link Grid} which simply moves a
 * {@link Position} forward and backward
 * 
 * @author Dominic
 *
 */
public class DefaultGrid implements Grid {

    private List<GridElement> gridElements;
    protected int maxX;
    protected int maxY;
    protected int minX;
    protected int minY;

    /**
     * Creates a default Grid which has a size of 10 to 10
     */
    public DefaultGrid() {
	this(10, 10);
	gridElements = new ArrayList<>();
    }

    /**
     * 
     * @param maxY
     * @param maxX
     */
    public DefaultGrid(int maxY, int maxX) {
	this(maxY, maxX, 0, 0);
    }

    /**
     * 
     * @param maxY
     * @param maxX
     */
    public DefaultGrid(int maxY, int maxX, int minX, int minY) {
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
    @Override
    public Position moveBackward(Position position) {

	Direction direction = position.getDirection();
	double newX = getNewXValue(position, direction.getBackwardX());
	double newY = getNewYValue(position, direction.getBackwardY());
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

	return Positions.of(direction, newX, newY);
    }

    @Override
    public boolean containsElement(GridElement gridElement) {
	return gridElements.contains(gridElement);
    }

    @Override
    public void addElement(GridElement gridElement) {
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
}
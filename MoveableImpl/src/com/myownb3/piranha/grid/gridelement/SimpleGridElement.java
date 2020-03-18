/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.shape.PointShape;
import com.myownb3.piranha.grid.gridelement.shape.Shape;

/**
 * @author Dominic
 *
 */
public class SimpleGridElement implements GridElement {

    protected Position position;
    protected Grid grid;
    protected Shape shape;

    /**
     * Creates a new {@link SimpleGridElement} with the given {@link Grid} and start
     * {@link Position} 
     * 
     * @param grid
     *            the Grid on which this {@link SimpleGridElement} is placed
     * @param position
     *            the start {@link Position}
     */
    public SimpleGridElement(Grid grid, Position position) {
	this(grid, position, new PointShape(position));
    }

    /**
     * Creates a new {@link SimpleGridElement} with the given {@link Grid}, start
     * {@link Position} and {@link Shape}
     * 
     * @param grid
     *            the Grid on which this {@link SimpleGridElement} is placed
     * @param position
     *            the start {@link Position}
     * @param shape
     *            the {@link Shape}
     */
    public SimpleGridElement(Grid grid, Position position, Shape shape) {
	super();
	this.position = position;
	this.grid = grid;
	this.shape = shape;
	grid.addElement(this);
    }

    @Override
    public Position getPosition() {
	return position;
    }

    @Override
    public Grid getGrid() {
	return grid;
    }
    
    @Override
    public Shape getShape() {
	return shape;
    }
    
    @Override
    public String toString() {
	return "Position: " + position + "\n" + grid;
    }
}
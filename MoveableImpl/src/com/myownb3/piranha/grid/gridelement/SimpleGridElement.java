/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;

/**
 * @author Dominic
 *
 */
public class SimpleGridElement implements GridElement {

    protected Position position;
    protected Grid grid;

    public SimpleGridElement(Grid grid, Position position) {
	super();
	this.position = position;
	this.grid = grid;
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
    public String toString() {
	return "Position: " + position + "\n" + grid;
    }
}
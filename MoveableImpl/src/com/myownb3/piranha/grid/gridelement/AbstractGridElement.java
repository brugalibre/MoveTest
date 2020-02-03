/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;

/**
 * @author Dominic
 *
 */
public class AbstractGridElement implements GridElement {

    protected Position position;
    public Grid grid;

    public AbstractGridElement(Grid grid, Position position) {
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
    public String toString() {
        return "Position: " + position + "\n" + grid;
    }
}
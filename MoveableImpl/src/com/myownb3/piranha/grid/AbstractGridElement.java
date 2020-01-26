/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public class AbstractGridElement implements GridElement {

    protected Position position;
    protected Grid grid;

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
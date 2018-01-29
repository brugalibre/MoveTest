/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public class AbstractGridElement implements GridElement {

    protected Position position;
    protected Grid grid;

    public AbstractGridElement(Position position, Grid grid) {
	super();
	this.position = position;
	this.grid = grid;
	grid.addElement(this);
    }

    @Override
    public Position getPosition() {
	return position;
    }
}
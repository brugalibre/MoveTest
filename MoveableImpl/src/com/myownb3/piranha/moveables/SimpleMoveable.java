/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.AbstractMovable;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.PositionImpl;

/**
 * @author Dominic
 *
 */
public class SimpleMoveable extends AbstractMovable {

    /**
     * @param position
     */
    public SimpleMoveable() {
	this(new DefaultGrid(), new PositionImpl(0, 0));
    }

    /**
     * @param position
     */
    public SimpleMoveable(Grid grid, Position position) {
	super(grid, position);
    }
}

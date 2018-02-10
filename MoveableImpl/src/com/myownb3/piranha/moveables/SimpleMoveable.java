/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;

/**
 * @author Dominic
 *
 */
public class SimpleMoveable extends AbstractMoveable {

    /**
     * @param position
     */
    public SimpleMoveable() {
	this(new DefaultGrid(), Positions.of(0, 0));
    }

    /**
     * @param position
     */
    public SimpleMoveable(Grid grid, Position position) {
	super(grid, position);
    }

    /**
     * @param grid
     * @param of
     * @param detector
     */
    public SimpleMoveable(Grid grid, Position position, Detector detector) {
	this(grid, position, detector, false);
    }

    /**
     * @param grid
     * @param of
     * @param detector
     */
    public SimpleMoveable(Grid grid, Position position, Detector detector, boolean isEvasionEnabled) {
	super(grid, position, new AvoidingHelper(detector, isEvasionEnabled));
    }
}

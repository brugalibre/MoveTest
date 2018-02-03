/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public class SimpleAvoidableMoveable extends AvoidableMovable {

    /**
     * @param grid
     * @param of
     * @param detector
     */
    public SimpleAvoidableMoveable(Grid grid, Position position, Detector detector) {
	super(grid, position, detector);
    }
}

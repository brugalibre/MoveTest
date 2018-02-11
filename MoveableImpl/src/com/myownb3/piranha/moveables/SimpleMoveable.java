/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.moveables.detector.Detector;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.moveables.helper.EvasionMoveableHelper;
import com.myownb3.piranha.moveables.helper.EvasionStateMachine;
import com.myownb3.piranha.moveables.helper.MoveableHelper;

/**
 * @author Dominic
 *
 */
public class SimpleMoveable extends AbstractMoveable {

    /**
     * Creates a new {@link SimpleMoveable} with a {@link DefaultGrid}, a default
     * {@link MoveableHelper} and the default origin {@link Position} at x=0 and y=0
     * 
     */
    public SimpleMoveable() {
	this(new DefaultGrid(), Positions.of(0, 0));
    }

    /**
     * Creates a new {@link SimpleMoveable} with the given {@link Grid} a default
     * {@link MoveableHelper} and the given origin {@link Position}
     * 
     * @param position
     *            the origin position
     */
    public SimpleMoveable(Grid grid, Position position) {
	super(grid, position);
    }

    /**
     * Creates a new {@link SimpleMoveable} with the given {@link Grid},a
     * {@link DetectableMoveableHelper} which is only able to detect other
     * {@link GridElement} and to decide weather or not this {@link GridElement} is
     * evaded
     * 
     * @param grid
     *            the Grid
     * @param position
     *            the origin Position
     * @param detector
     *            the {@link Detector}
     */
    public SimpleMoveable(Grid grid, Position position, Detector detector) {
	super(grid, position, new DetectableMoveableHelper(detector));
    }

    /**
     * * Creates a new {@link SimpleMoveable} with the given {@link Grid},a
     * {@link EnvasionMoveableHelper} and the given origin {@link Position}
     * 
     * @param grid
     *            the Grid
     * @param position
     *            the origin Position
     * @param detector
     *            the {@link Detector}
     * @param isEvasionEnabled
     *            <code>true</code> if the Detector is evasioning of
     *            <code>false</code> if not
     */
    public SimpleMoveable(Grid grid, Position position, Detector detector, boolean isEvasionEnabled) {
	super(grid, position, new EvasionMoveableHelper(detector));
    }

    /**
     * * Creates a new {@link SimpleMoveable} with the given {@link Grid},a
     * {@link EnvasionMoveableHelper} and the given origin {@link Position}
     * 
     * @param grid
     *            the Grid
     * @param position
     *            the origin Position
     * @param detector
     *            the {@link Detector}
     * @param isEvasionEnabled
     *            <code>true</code> if the Detector is evasioning of
     *            <code>false</code> if not
     * @param isEvasionManeuvreCorrectionEnabled
     */
    public SimpleMoveable(Grid grid, Position position, Detector detector, boolean isEvasionEnabled,
	    boolean isEvasionManeuvreCorrectionEnabled) {
	super(grid, position, new EvasionStateMachine(detector));
    }
}

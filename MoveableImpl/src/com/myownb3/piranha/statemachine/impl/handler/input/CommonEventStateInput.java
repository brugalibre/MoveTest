package com.myownb3.piranha.statemachine.impl.handler.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.input.EventStateInput;

public class CommonEventStateInput implements EventStateInput {

    private Grid grid;
    private DetectableMoveableHelper helper;
    private Moveable moveable;

    protected CommonEventStateInput(Grid grid, Moveable moveable, DetectableMoveableHelper helper) {
	this.grid = requireNonNull(grid);
	this.moveable = requireNonNull(moveable);
	this.helper = requireNonNull(helper);
    }

    public DetectableMoveableHelper getHelper() {
	return helper;
    }

    public Grid getGrid() {
	return grid;
    }

    public Moveable getMoveable() {
	return moveable;
    }

    /**
     * Creates a new {@link CommonEventStateInput}
     * 
     * @param grid
     *            the {@link Grid} on which the moveable moves
     * 
     * @param moveable
     *            the {@link Moveable}
     * @param helper
     *            the {@link DetectableMoveableHelper}
     * @return a new {@link CommonEventStateInput}
     */
    public static CommonEventStateInput of(Grid grid, Moveable moveable, DetectableMoveableHelper helper) {
	return new CommonEventStateInput(grid, moveable, helper);
    }
}
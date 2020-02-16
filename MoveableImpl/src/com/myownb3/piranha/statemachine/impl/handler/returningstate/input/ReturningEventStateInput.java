package com.myownb3.piranha.statemachine.impl.handler.returningstate.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.input.CommonEventStateInput;

public class ReturningEventStateInput extends CommonEventStateInput {

    private Position positionBeforeEvasion;

    private ReturningEventStateInput(DetectableMoveableHelper helper, Grid grid, Position positionBeforeEvasion, Moveable moveable) {
	super(grid, moveable, helper);
	this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
    }

    /**
     * Creates a new {@link ReturningEventStateInput}
     * 
     * @param helper
     *            the {@link DetectableMoveableHelper}
     * @param grid
     *            the {@link Grid}
     * @param moveable
     *            the {@link Moveable}
     * @param positionBeforeEvasion
     *            the {@link Position} when the {@link Detector} detected an evasion
     * @return a new {@link ReturningEventStateInput}
     */
    public static ReturningEventStateInput of(DetectableMoveableHelper helper, Grid grid, Moveable moveable, Position positionBeforeEvasion) {
	return new ReturningEventStateInput(helper, grid, positionBeforeEvasion, moveable);
    }

    public final Position getPositionBeforeEvasion() {
	return this.positionBeforeEvasion;
    }
}

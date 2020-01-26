package com.myownb3.piranha.statemachine.impl.handler.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;

public class PassingEventStateInput extends CommonEventStateInput {

    private Position positionBeforeEvasion;
    private int passingDistance;

    private PassingEventStateInput(Grid grid, Moveable moveable, Position positionBeforeEvasion, DetectableMoveableHelper helper, int passingDistance) {
	super(grid, moveable, helper);
	this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
	this.passingDistance = passingDistance;
    }

    public static PassingEventStateInput of(Grid grid, Moveable moveable, Position positionBeforeEvasion, DetectableMoveableHelper helper, int passingDistance) {
	return new PassingEventStateInput(grid, moveable, positionBeforeEvasion, helper, passingDistance);
    }

    public Position getPositionBeforeEvasion() {
	return positionBeforeEvasion;
    }

    /**
     * @return the passing distance
     */
    public double getPassingDistance() {
	return passingDistance;
    }
}

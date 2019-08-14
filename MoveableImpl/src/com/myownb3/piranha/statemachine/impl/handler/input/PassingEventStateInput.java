package com.myownb3.piranha.statemachine.impl.handler.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;

public class PassingEventStateInput extends CommonEventStateInput {

    private Position positionBeforeEvasion;

    private PassingEventStateInput(Grid grid, Moveable moveable, Position positionBeforeEvasion, DetectableMoveableHelper helper) {
	super(grid, moveable, helper);
	this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
    }

    public static PassingEventStateInput of(Grid grid, Moveable moveable, Position positionBeforeEvasion, DetectableMoveableHelper helper) {
	return new PassingEventStateInput(grid, moveable, positionBeforeEvasion, helper);
    }

    public Position getPositionBeforeEvasion() {
	return positionBeforeEvasion;
    }
}

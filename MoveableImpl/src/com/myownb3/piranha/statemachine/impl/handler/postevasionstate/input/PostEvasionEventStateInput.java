package com.myownb3.piranha.statemachine.impl.handler.postevasionstate.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.input.CommonEventStateInput;

public class PostEvasionEventStateInput extends CommonEventStateInput {

    private Position positionBeforeEvasion;

    private PostEvasionEventStateInput(Grid grid, DetectableMoveableHelper helper, Moveable moveable, Position positionBeforeEvasion) {
	super(grid, moveable, helper);
	this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
    }

    public static PostEvasionEventStateInput of(DetectableMoveableHelper helper, Grid grid, Moveable moveable, Position positionBeforeEvasion) {
	return new PostEvasionEventStateInput(grid, helper, moveable, positionBeforeEvasion);
    }

    public final Position getPositionBeforeEvasion() {
	return this.positionBeforeEvasion;
    }
}

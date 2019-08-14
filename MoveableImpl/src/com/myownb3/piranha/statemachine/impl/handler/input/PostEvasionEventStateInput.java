package com.myownb3.piranha.statemachine.impl.handler.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.handler.input.EventStateInput;

public class PostEvasionEventStateInput implements EventStateInput {

    private Moveable moveable;
    private Position positionBeforeEvasion;

    private PostEvasionEventStateInput(Position positionBeforeEvasion, Moveable moveable) {
	this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
	this.moveable = requireNonNull(moveable);
    }

    public static PostEvasionEventStateInput of(Position positionBeforeEvasion, Moveable moveable) {
	return new PostEvasionEventStateInput(positionBeforeEvasion, moveable);
    }

    public final Moveable getMoveable() {
	return this.moveable;
    }

    public final Position getPositionBeforeEvasion() {
	return this.positionBeforeEvasion;
    }
}

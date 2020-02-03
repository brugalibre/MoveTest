package com.myownb3.piranha.statemachine.impl.handler.output;

import java.util.Optional;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class DefaultStateResult extends CommonEventStateResult {

    private Position positionBeforeEvasion;

    public DefaultStateResult(Position positionBeforeEvasion,EvasionStates nextState) {
	super(nextState);
	this.positionBeforeEvasion = positionBeforeEvasion;
    }

    public Optional<Position> getPositionBeforeEvasion() {
	return Optional.ofNullable(positionBeforeEvasion);
    }
}

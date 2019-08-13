package com.myownb3.piranha.moveables.statemachine.impl.handler.result;

import com.myownb3.piranha.grid.Position;

public class DefaultStateResult extends EmptyEvenStateResult {

    private Position positionBeforeEvasion;

    public DefaultStateResult(Position positionBeforeEvasion) {
	this.positionBeforeEvasion = positionBeforeEvasion;
    }

    public Position getPositionBeforeEvasion() {
	return positionBeforeEvasion;
    }

}

package com.myownb3.piranha.moveables.statemachine.impl.handler.input;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.statemachine.impl.DetectableMoveableHelper;

public class PassingEventStateInput extends CommonEventStateInput {

    private Position positionBeforeEvasion;
    private int passingDistance;

    public PassingEventStateInput(Grid grid, Moveable moveable, Position positionBeforeEvasion, int passingDistance,
	    DetectableMoveableHelper helper) {
	super(grid, moveable, helper);
	this.positionBeforeEvasion = positionBeforeEvasion;
	this.passingDistance = passingDistance;
    }

    public Position getPositionBeforeEvasion() {
	return positionBeforeEvasion;
    }

    public int getPassingDistance() {
	return passingDistance;
    }

}

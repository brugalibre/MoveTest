package com.myownb3.piranha.statemachine.handler.input;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.EvenStateInput;

public class CommonEventStateInput implements EvenStateInput {

    private Grid grid;
    private DetectableMoveableHelper helper;
    private Moveable moveable;

    public CommonEventStateInput(Grid grid, Moveable moveable, DetectableMoveableHelper helper) {
	this.grid = grid;
	this.moveable = moveable;
	this.helper = helper;
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

}

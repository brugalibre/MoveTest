package com.myownb3.piranha.statemachine.handler.input;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.moveables.statemachine.impl.MoveableExecutor;

public class ReturningEventStateInput extends CommonEventStateInput {

    private List<MoveableExecutor> executors;

    public ReturningEventStateInput(Grid grid, Moveable moveable, DetectableMoveableHelper helper) {
	super(grid, moveable, helper);
    }

    public List<MoveableExecutor> getExecutors() {
	return executors;
    }
}

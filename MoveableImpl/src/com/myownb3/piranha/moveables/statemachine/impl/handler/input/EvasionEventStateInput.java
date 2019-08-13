package com.myownb3.piranha.moveables.statemachine.impl.handler.input;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;
import com.myownb3.piranha.moveables.statemachine.impl.DetectableMoveableHelper;

public class EvasionEventStateInput extends CommonEventStateInput {

    private Position positionBeforeEvasion;
    private Detector detector;
    private int passingDistance;

    public EvasionEventStateInput(Grid grid, Moveable moveable, Detector detector, DetectableMoveableHelper helper) {
	super(grid, moveable, helper);
	this.detector = detector;
    }

    public Detector getDetector() {
	return detector;
    }

    public Position getPositionBeforeEvasion() {
	return positionBeforeEvasion;
    }

    public int getPassingDistance() {
	return passingDistance;
    }

}

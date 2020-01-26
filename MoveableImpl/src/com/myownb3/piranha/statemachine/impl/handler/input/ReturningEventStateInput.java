package com.myownb3.piranha.statemachine.impl.handler.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.handler.input.EventStateInput;

public class ReturningEventStateInput implements EventStateInput {

    private Position positionBeforeEvasion;
    private Moveable moveable;
    private Detector detector;

    private ReturningEventStateInput(Detector detector, Position positionBeforeEvasion, Moveable moveable) {
	this.detector = requireNonNull(detector);
	this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
	this.moveable = requireNonNull(moveable);
    }

    /**
     * Creates a new {@link ReturningEventStateInput}
     * 
     * @param detector
     *            the Detector
     * @param positionBeforeEvasion
     *            the {@link Position} when the {@link Detector} detected an evasion
     * @param moveable
     *            the {@link Moveable}
     * @return a new {@link ReturningEventStateInput}
     */
    public static ReturningEventStateInput of(Detector detector, Position positionBeforeEvasion, Moveable moveable) {
	return new ReturningEventStateInput(detector, positionBeforeEvasion, moveable);
    }

    public final Detector getDetector() {
	return this.detector;
    }

    public final Position getPositionBeforeEvasion() {
	return this.positionBeforeEvasion;
    }

    public final Moveable getMoveable() {
	return this.moveable;
    }
}

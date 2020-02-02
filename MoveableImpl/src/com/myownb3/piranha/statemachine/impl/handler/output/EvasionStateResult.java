package com.myownb3.piranha.statemachine.impl.handler.output;

import com.myownb3.piranha.statemachine.states.EvasionStates;

public class EvasionStateResult extends CommonEventStateResult {

    private double avoidAngle;

    private EvasionStateResult(EvasionStates resultState, double avoidAngle) {
	super(resultState);
	this.avoidAngle = avoidAngle;
    }

    public static EvasionStateResult of(EvasionStates resultState, double avoidAngle) {
	return new EvasionStateResult(resultState, avoidAngle);
    }

    public final double getAvoidAngle() {
	return this.avoidAngle;
    }
}

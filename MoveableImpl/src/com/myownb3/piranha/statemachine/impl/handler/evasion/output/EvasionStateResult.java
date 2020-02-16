package com.myownb3.piranha.statemachine.impl.handler.evasion.output;

import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class EvasionStateResult extends CommonEventStateResult {

    private double avoidAngle;

    private EvasionStateResult(EvasionStates prevState, EvasionStates resultState, double avoidAngle) {
	super(prevState, resultState, null);
	this.avoidAngle = avoidAngle;
    }

    public static EvasionStateResult of(EvasionStates prevState, EvasionStates resultState, double avoidAngle) {
	return new EvasionStateResult(prevState, resultState, avoidAngle);
    }

    public final double getAvoidAngle() {
	return this.avoidAngle;
    }
}

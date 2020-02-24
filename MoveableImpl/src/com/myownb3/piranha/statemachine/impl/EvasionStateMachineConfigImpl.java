/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;

/**
 * The {@link EvasionStateMachineConfigImpl} is the default implementation for
 * the {@link EvasionStateMachineConfig}
 */
public class EvasionStateMachineConfigImpl implements EvasionStateMachineConfig {

    // Attributes of the different EvasionStateHandlerse
    private int postEvasionAngleAdjustStepWidth;
    private double returningMinDistance;
    private double returningAngleIncMultiplier;
    private int passingDistance;

    // Attributes for Detector
    private int detectorReach;
    private int detectorAngle;
    private int evasionAngle;
    private double evasionAngleInc;

    private double returningAngleMargin;
    private int returingMovingForwardIncrement;

    public EvasionStateMachineConfigImpl(double angleIncMultiplier, double minDistance, double angleMargin,
	    int detectorReach, int detectorAngle, int evasionAngle, double evasionAngleInc) {
	this(angleIncMultiplier, minDistance, angleMargin, detectorReach, 2 * detectorReach / 3, detectorAngle,
		evasionAngle, evasionAngleInc, 0);
    }
    
    public EvasionStateMachineConfigImpl(double angleIncMultiplier, double minDistance, double angleMargin,
	    int detectorReach, int detectorAngle, int evasionAngle, double evasionAngleInc, int returingMovingForwardIncrement) {
	this(angleIncMultiplier, minDistance, angleMargin, detectorReach, 2 * detectorReach / 3, detectorAngle,
		evasionAngle, evasionAngleInc, returingMovingForwardIncrement);
    }

    private EvasionStateMachineConfigImpl(double angleIncMultiplier, double minDistance, double angleMargin,
	    int detectorReach, int passingDistance, int detectorAngle, int evasionAngle, double evasionAngleInc, int returingMovingForwardIncrement) {
	this.postEvasionAngleAdjustStepWidth = 10; // Like this the movements are smoother
	this.returningAngleIncMultiplier = angleIncMultiplier;
	this.returningMinDistance = minDistance;
	this.returningAngleMargin = angleMargin;

	this.passingDistance = 2 * detectorReach / 3;
	this.evasionAngleInc = evasionAngleInc;
	this.detectorReach = detectorReach;
	this.detectorAngle = detectorAngle;
	this.evasionAngle = evasionAngle;
	this.returingMovingForwardIncrement = returingMovingForwardIncrement;
    }

    @Override
    public int getPostEvasionAngleAdjustStepWidth() {
	return this.postEvasionAngleAdjustStepWidth;
    }

    @Override
    public final double getReturningMinDistance() {
	return this.returningMinDistance;
    }

    @Override
    public double getReturningAngleMargin() {
	return returningAngleMargin;
    }

    @Override
    public final double getReturningAngleIncMultiplier() {
	return this.returningAngleIncMultiplier;
    }
    
    @Override
    public int getReturingMovingForwardIncrement() {
        return returingMovingForwardIncrement;
    }

    @Override
    public double getEvasionAngleInc() {
	return evasionAngleInc;
    }

    @Override
    public final int getDetectorReach() {
	return this.detectorReach;
    }

    @Override
    public final int getPassingDistance() {
	return this.passingDistance;
    }

    @Override
    public final int getDetectorAngle() {
	return this.detectorAngle;
    }

    @Override
    public final int getEvasionAngle() {
	return this.evasionAngle;
    }
}

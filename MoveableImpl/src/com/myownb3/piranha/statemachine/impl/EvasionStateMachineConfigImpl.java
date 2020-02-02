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
    private int evasionAngleRelative2Increment;
    private double returningMinDistance;
    private int returningAngleIncMultiplier;
    private int passingDistance;

    // Attributes for Detector
    private int detectorReach;
    private int detectorAngle;
    private int evasionAngle;
    private double evasionAngleInc;
    private double returningAngleMargin;

    public EvasionStateMachineConfigImpl(int angleIncMultiplier, double minDistance, double angleMargin, int detectorReach, int detectorAngle, int evasionAngle, double evasionAngleInc) {
	this.postEvasionAngleAdjustStepWidth = 10; // Like this the movements are smoother
	this.returningAngleIncMultiplier = angleIncMultiplier;
	this.returningMinDistance = minDistance;
	this.returningAngleMargin = angleMargin;

	this.passingDistance = 2 * detectorReach / 3;
	this.evasionAngleInc = evasionAngleInc;
	this.detectorReach = detectorReach;
	this.detectorAngle = detectorAngle;
	this.evasionAngle = evasionAngle;
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
    public final int getReturningAngleIncMultiplier() {
	return this.returningAngleIncMultiplier;
    }

    @Override
    public final int getEvasionAngleRelative2Increment() {
	return this.evasionAngleRelative2Increment;
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

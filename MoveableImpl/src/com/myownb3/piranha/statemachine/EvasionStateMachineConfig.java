/**
 * 
 */
package com.myownb3.piranha.statemachine;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * 
 * The {@link EvasionStateMachineConfig} contain the configuration for the
 * {@link EvasionStateMachine} itself as well as for all it's
 * {@link EvasionStatesHandler} E.g. this contains the angle-increment used in
 * order to turn a moveable as soon as there is an evasion or the minimal
 * distance when returning to it's previous direction
 * 
 * @author Dominic
 *
 */
public interface EvasionStateMachineConfig {

    /**
     * Returns the angle increment a {@link Moveable} turns whenever there is an
     * evasion. Used in State {@link EvasionStates#EVASION}
     * 
     * @return the angle increment a {@link Moveable} turns whenever there is an
     *         evasion
     */
    double getEvasionAngleInc();

    /**
     * @return the angle increment a {@link Moveable} turns in order to avoid an
     *         evasion in State {@link EvasionStates#EVASION}
     */
    int getEvasionAngleRelative2Increment();

    /**
     * @returns the Step-width for the angle in State
     *          {@link EvasionStates#POST_EVASION}
     */
    int getPostEvasionAngleAdjustStepWidth();

    /**
     * @return the value which is used to multiply the angle to turn a
     *         {@link Moveable} in order to return to it's previous direction in
     *         State {@link EvasionStates#RETURNING}
     */
    int getReturningAngleIncMultiplier();

    /**
     * @return the minimal distance a {@link Moveable} has to approximate to it's
     *         previous direction in State {@link EvasionStates#RETURNING}
     */
    double getReturningMinDistance();

    /**
     * @return the angle to detect an evasion with a {@link GridElement}
     */
    int getEvasionAngle();

    /**
     * @return the angle to detect {@link GridElement} of the detector
     */
    int getDetectorAngle();

    /**
     * @return the reach of the {@link Detector}
     */
    int getDetectorReach();

    /**
     * @return the distance a {@link Moveable} passes along an evaded
     *         {@link GridElement} in state {@link EvasionStates#PASSING}
     */
    int getPassingDistance();

    /**
     * @return the margin which is tolerated between the actual angle and 0 degrees
     */
    double getReturningAngleMargin();
}

/**
 * 
 */
package com.myownb3.piranha.core.statemachine;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

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
    * Returns the angle for the initial angle correction in order to get the {@link Moveable} on track
    * 
    * @return
    */
   double getOrientationAngle();

   /**
    * Returns the angle increment a {@link Moveable} turns whenever there is an
    * evasion. Used in State {@link EvasionStates#EVASION}
    * 
    * @return the angle increment a {@link Moveable} turns whenever there is an
    *         evasion
    */
   double getEvasionAngleInc();

   /**
    * @returns the angle to turn when entering the State
    *          {@link EvasionStates#POST_EVASION}
    */
   int getPostEvasionReturnAngle();

   /**
    * @return the value which is used to multiply the angle to turn a
    *         {@link Moveable} in order to return to it's previous direction in
    *         State {@link EvasionStates#RETURNING}
    */
   double getReturningAngleIncMultiplier();

   /**
    * @return the minimal distance a {@link Moveable} has to approximate to it's
    *         previous direction in State {@link EvasionStates#RETURNING}
    */
   double getReturningMinDistance();

   /**
    * @return the angle to detect an evasion with a {@link GridElement}
    */
   double getEvasionAngle();

   /**
    * @return the angle to detect {@link GridElement} of the detector
    */
   double getDetectorAngle();

   /**
    * @return the reach of the {@link Detector}
    */
   int getDetectorReach();


   /**
    * @return the reach of the {@link Detector} within it can detect a collision
    */
   int getEvasionDistance();

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

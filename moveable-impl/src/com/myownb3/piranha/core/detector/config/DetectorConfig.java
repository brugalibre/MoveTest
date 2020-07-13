/**
 * 
 */
package com.myownb3.piranha.core.detector.config;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

/**
 * 
 * The {@link DetectorConfig} contain the configuration for a {@link TrippleDetectorCluster} or a {@link Detector}
 * 
 * @author Dominic
 *
 */
public interface DetectorConfig {

   /**
    * Returns the angle increment a {@link Moveable} turns whenever there is an
    * evasion. Used in State {@link EvasionStates#EVASION}
    * 
    * @return the angle increment a {@link Moveable} turns whenever there is an
    *         evasion
    */
   double getEvasionAngleInc();

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
}

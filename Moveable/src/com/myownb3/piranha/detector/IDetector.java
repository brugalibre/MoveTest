/**
 * 
 */
package com.myownb3.piranha.detector;

/**
 * Defines a {@link Detector} with a getter access to some of it's values
 * 
 * @author Dominic
 *
 */
public interface IDetector extends Detector {

   /**
    * @return the evasion angle
    */
   double getEvasionAngle();

   /**
    * 
    * @return the detection angle
    */
   double getDetectorAngle();
}

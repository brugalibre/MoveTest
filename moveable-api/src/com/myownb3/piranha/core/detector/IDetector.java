/**
 * 
 */
package com.myownb3.piranha.core.detector;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

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

   /**
    * Returns <code>true</code> if the this {@link Detector} is currently
    * detecting any {@link GridElement} within it's detecting radius or <code>false</code> if not
    * 
    * @param position
    * 
    * @return <code>true</code> if the this {@link Detector} is currently
    *         detecting any {@link GridElement} within it's detecting radius or <code>false</code> if not
    */
   boolean hasGridElementDetectedAtPosition(Position position);
}

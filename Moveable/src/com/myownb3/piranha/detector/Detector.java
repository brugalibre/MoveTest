/**
 * 
 */
package com.myownb3.piranha.detector;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;

/**
 * A {@link Detector} is used to detect other {@link Avoidable} on a
 * {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface Detector {

   /**
    * Evaluates if the given {@link Avoidable} has been detected by this Detector
    * We use also the given {@link Position} of the {@link Avoidable} to check weather or not this avoidable is detected
    * 
    * @param avoidable
    *        the {@link Avoidable} to detect
    * @param avoidablePos
    *        the {@link Position} of the Avoidble which is used for the verification if there is a detection
    * @param position
    *        the current Position from which the {@link Detector} trys to
    *        detect
    * @return <code>true</code> if the object was detected, <code>false</code> if
    *         not
    */
   boolean detectObject(Avoidable avoidable, Position avoidablePos, Position position);

   /**
    * 
    * @return <code>true</code> if this {@link Moveable} is currently evasion the
    *         given {@link Avoidable}
    */
   boolean isEvasion(Avoidable avoidable);

   /**
    * Returns <code>true</code> if the given {@link Avoidable} is currently
    * detected or <code>false</code> if not
    * 
    * @param avoidable
    * @return <code>true</code> if the given {@link Avoidable} is currently
    *         detected or <code>false</code> if not
    */
   boolean hasObjectDetected(Avoidable avoidable);

   /**
    * Returns the angle increment for which a {@link Moveable} can make a turn in
    * order to avoid a {@link Avoidable} which is is on a collision path. This
    * method will return <code>0</code> if this {@link Detector} is currently not
    * evasion any {@link Avoidable}
    * 
    * @param position
    *        the origin Position
    * @returns the evasion angle increment
    */
   double getEvasionAngleRelative2(Position position);
}

/**
 * 
 */
package com.myownb3.piranha.detector;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
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
    * Evaluates if the given {@link GridElement} has been detected by this Detector
    * We use also the given {@link Position} of the {@link Avoidable} to check weather or not this avoidable is detected
    * 
    * @param gridElement
    *        the {@link GridElement} to detect
    * @param gridElementPos
    *        the {@link Position} of the gridElement which is used for the verification if there is a detection
    * @param position
    *        the current Position from which the {@link Detector} tries to
    *        detect
    */
   void detectObject(GridElement gridElement, Position gridElementPos, Position position);

   /**
    * Evaluates if the given {@link GridElement} has been detected by this Detector
    * We use also the given {@link Position} of the {@link Avoidable} to check weather or not this avoidable is detected
    * 
    * @param gridElement
    *        the {@link GridElement} to detect
    * @param gridElementPath
    *        the Path of the gridElement which is used for the verification if there is a detection
    * @param position
    *        the current Position from which the {@link Detector} tries to
    *        detect
    */
   void detectObjectAlongPath(GridElement gridElement, List<Position> gridElementPath, Position position);

   /**
    * 
    * @return <code>true</code> if this {@link Moveable} is currently evasion the
    *         given {@link GridElement}
    */
   boolean isEvasion(GridElement gridElement);

   /**
    * Returns <code>true</code> if the given {@link GridElement} is currently
    * detected or <code>false</code> if not
    * 
    * @param gridElement
    * @return <code>true</code> if the given {@link GridElement} is currently
    *         detected or <code>false</code> if not
    */
   boolean hasObjectDetected(GridElement gridElement);

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

   /**
    * Returns the range of this detector
    * 
    * @return the range of this detector
    */
   int getDetectorRange();

   /**
    * @return the range within this detector avoid a {@link GridElement}
    */
   int getEvasionRange();
}

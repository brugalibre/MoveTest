package com.myownb3.piranha.detector.cluster;

import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.init.Initializable;
import com.myownb3.piranha.moveables.Moveable;

/**
 * The {@link DetectingStrategyHandler} handles a specific {@link DetectingStrategy}
 * 
 * @author Dominic
 *
 */
public interface DetectingStrategyHandler extends Initializable {

   /**
    * Evaluates if the given {@link GridElement} has been detected by this Detector
    * We use also the given {@link Position} of the {@link Avoidable} to check weather or not this avoidable is detected
    * 
    * @param gridElement
    *        the {@link GridElement} to detect
    * @param gridElementPath
    *        the Path of the gridElement which is used for the verification if there is a detection
    * @param detectorPosition
    *        the current Position from which the {@link Detector} tries to
    *        detect
    */
   void detectObjectAlongPath(GridElement gridElement, List<Position> gridElementPath, Position detectorPosition);

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
    * Returns the evasion distance for the Detector which has detected the evasion in the first place or <code>null</code> if there is
    * currently no detecting {@link Detector}
    * {@link Detector}s
    * 
    * @return the distance for which the returning of the {@link EvasionStates}
    */
   Integer getEvasionDistance4DetectingDetector();
}

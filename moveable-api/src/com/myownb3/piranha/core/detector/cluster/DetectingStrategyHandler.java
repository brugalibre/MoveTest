package com.myownb3.piranha.core.detector.cluster;

import java.util.List;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link DetectingStrategyHandler} handles a specific {@link DetectingStrategy}
 * 
 * @author Dominic
 *
 */
public interface DetectingStrategyHandler {

   /**
    * Evaluates if the given {@link GridElement} has been detected by this Detector
    * We use also the given {@link Position}s of the {@link GridElement} (according to the path of it's {@link Shape}) to check weather or
    * not this GridElement is detected
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
    * order to avoid a {@link GridElement} which is is on a collision path. This
    * method will return <code>0.0</code> if this {@link Detector} is currently not
    * evasion any {@link GridElement}
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

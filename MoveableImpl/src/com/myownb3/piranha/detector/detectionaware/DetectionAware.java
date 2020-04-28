package com.myownb3.piranha.detector.detectionaware;

import java.util.List;
import java.util.Optional;

import com.myownb3.piranha.detector.DetectionResult;
import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.moveables.Moveable;

/**
 * Defines an interface fore instance which are aware of detected {@link GridElement}
 * 
 * @author Dominic
 *
 */
public interface DetectionAware {

   /**
    * Removes all recorded entries about detection and evasion for the given {@link GridElement}
    * 
    * @param gridElement
    *        the {@link GridElement}
    */
   void clearGridElement(GridElement gridElement);

   /**
    * Depending on the {@link DetectionResult}s this {@link GridElement} is added internaly and will be considered as 'detected' or even
    * 'isEvasion'
    * 
    * @param gridElement
    *        the {@link GridElement}
    * @param detectionResults
    *        the result from the {@link Detector} detecting
    */
   void checkGridElement4Detection(GridElement gridElement, List<DetectionResult> detectionResults);

   /**
    * 
    * @return <code>true</code> if the {@link Moveable} is currently evasion the
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
    * Returns the detected {@link Avoidable} which is placed the neares to the given {@link Position}
    * 
    * @param position
    *        the given {@link Position}
    * @return the detected {@link Avoidable} which is placed the neares to the given {@link Position}
    */
   Optional<Avoidable> getNearestEvasionAvoidable(Position position);
}

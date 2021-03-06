package com.myownb3.piranha.core.detector.detectionaware;

import java.util.List;
import java.util.Optional;

import com.myownb3.piranha.core.detector.DetectionResult;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link DetectionAware} defines an interface for objects, which contains detected {@link GridElement}s.
 * It's like a knowledge base for detected {@link GridElement} - often used by a {@link Detector}
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
    * Depending on the {@link DetectionResult}s this {@link GridElement} is added internally and will be considered as 'detected' or even
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
    * Returns the detected {@link GridElement} which is placed the nearest to the given {@link Position}
    * 
    * @param position
    *        the given {@link Position}
    * @return the detected {@link GridElement} which is placed the nearest to the given {@link Position}
    */
   Optional<GridElement> getNearestEvasionGridElement(Position position);

   /**
    * Returns the detected {@link GridElement} which is placed the nearest to the given {@link Position}
    * 
    * @param position
    *        the given {@link Position}
    * @return the detected {@link GridElement} which is placed the nearest to the given {@link Position}
    */
   Optional<GridElement> getNearestDetectedGridElement(Position position);

   /**
    * 
    * @param gridElement
    *        the {@link GridElement}
    * @return all detected {@link Position}s for the given {@link GridElement}
    */
   List<Position> getDetectedPositions4GridElement(GridElement gridElement);
}

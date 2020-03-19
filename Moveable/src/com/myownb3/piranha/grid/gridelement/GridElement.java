/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.shape.Shape;

/**
 * A {@link GridElement} is a most simple element which can be placed on a
 * {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface GridElement {

   /**
    * Return the current {@link Position} of this {@link GridElement}
    * 
    * @return the current {@link Position} of this {@link GridElement}
    */
   Position getPosition();

   /**
    * Returns the {@link Position} of this {@link GridElement} which faces the same
    * direction than it's center {@link Position} but is placed on it's
    * {@link Shape}
    * 
    * @return the {@link Position} of this {@link GridElement}
    */
   Position getFurthermostFrontPosition();

   /**
    * Returns the {@link Position} of this {@link GridElement} which faces the oposit
    * direction than it's center {@link Position} but is placed on it's
    * {@link Shape}
    * 
    * @return the {@link Position} of this {@link GridElement}
    */
   Position getFurthermostBackPosition();

   /**
    * @return the Grid of this {@link GridElement}
    */
   Grid getGrid();

   /**
    * Returns the Shape of this {@link GridElement}
    * 
    * @return the Shape of this {@link GridElement}
    */
   Shape getShape();

   /**
    * Checks if this {@link GridElement} has detected the given {@link Avoidable}
    * 
    * @param avoidable
    *        the {@link Avoidable} to check
    * @param detector
    *        the {@link Detector} used for the actual detection
    */
   void hasAvoidableDetected(Avoidable avoidable, Detector detector);

   /**
    * Checks if this {@link GridElement} is detected by the given {@link Detector} and from the given Position
    * 
    * @param detectionPos
    *        the {@link Position} from which we check if we are detected
    * @param detector
    *        the {@link Detector} which does the actual detecting
    */
   void isDetectedBy(Position detectionPos, Detector detector);

   /**
    * Checks for every given {@link Avoidable} if there is a collision when this {@link GridElement} is moving
    * from it's current Position to the new Position
    * 
    * @param collisionDetector
    *        the {@link CollisionDetectionHandler} which does the actual collision detecting
    * 
    * @param newPosition
    *        the new Position after the movement
    * @param allAvoidables
    *        all {@link Avoidable} on the Grid
    */
   void check4Collision(CollisionDetector collisionDetector, Position newPosition, List<Avoidable> allAvoidables);
}

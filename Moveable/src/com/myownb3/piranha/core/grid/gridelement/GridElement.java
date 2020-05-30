/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import java.util.List;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

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
   Position getForemostPosition();

   /**
    * Returns the {@link Position} of this {@link GridElement} which faces the oposit
    * direction than it's center {@link Position} but is placed on it's
    * {@link Shape}
    * 
    * @return the {@link Position} of this {@link GridElement}
    */
   Position getRearmostPosition();

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
    * @return the radius within this {@link Shape} could be placed
    */
   double getDimensionRadius();

   /**
    * Checks if this {@link GridElement} has detected the given {@link GridElement}
    * 
    * @param gridElement
    *        the {@link GridElement} to check
    * @param detector
    *        the {@link Detector} used for the actual detection
    */
   void hasGridElementDetected(GridElement gridElement, Detector detector);

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
    * Checks for every given {@link GridElement} if there is a collision when this {@link GridElement} is moving
    * from it's current Position to the new Position
    * 
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles the collision if one occurred
    * @param newPosition
    *        the new Position after the movement
    * @param gridElements2Check
    *        the {@link GridElement}s to check
    */
   void check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition, List<GridElement> gridElements2Check);

   /**
    * Returns <code>true</code> if this {@link GridElement} is gridElement or <code>false</code> if not.
    * A {@link GridElement} which is avoidable can be avoided by a {@link Moveable} and can
    * 
    * @return <code>true</code> if this {@link GridElement} is gridElement or <code>false</code> if not
    * 
    */
   default boolean isAvoidable() {
      return false;
   }
}

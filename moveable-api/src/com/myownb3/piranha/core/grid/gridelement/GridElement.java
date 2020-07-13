/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectedException;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link GridElement} is a most simple element which can be placed on a
 * {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface GridElement extends CollisionSensitive {

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
    * Returns the {@link Position} of this {@link GridElement} which faces the opposite
    * direction than it's center {@link Position} but is placed on it's
    * {@link Shape}
    * 
    * @return the {@link Position} of this {@link GridElement}
    */
   Position getRearmostPosition();

   /**
    * Returns the Shape of this {@link GridElement}
    * 
    * @return the Shape of this {@link GridElement}
    */
   Shape getShape();

   /**
    * Returns the path as a list of {@link PathSegment} of this {@link GridElement}.
    * If this {@link GridElement} is placed higher then the other {@link GridElement} an empty list is returned
    * 
    * @param gridElement
    *        the {@link GridElement} which want the {@link PathSegment}s
    * @return a {@link List} of {@link PathSegment}
    */
   List<PathSegment> getPath(GridElement gridElement);

   /**
    * 
    * @return the {@link DimensionInfo} for this {@link GridElement}
    */
   DimensionInfo getDimensionInfo();

   /**
    * Checks if this {@link GridElement} is detected by the given {@link Detector} and from the given Position
    * 
    * @param detectionPos
    *        the {@link Position} from which we check if we are detected
    * @param detector
    *        the {@link Detector} which does the actual detecting
    * 
    * @return <code>true</code> if the {@link GridElement} at the specific {@link Position} was detected, <code>false</code> if not
    */
   boolean isDetectedBy(Position detectionPos, Detector detector);

   /**
    * Checks for every given {@link GridElement} if there is a collision when this {@link GridElement} is moving
    * from it's current Position to the new Position.
    * The returned {@link CollisionDetectionResult} contains further information about any detected collision. Depending on the
    * {@link CollisionDetectionHandler} it may be thrown a {@link CollisionDetectedException}
    * 
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles the collision if one occurred
    * @param newPosition
    *        the new Position after the movement
    * @param gridElements2Check
    *        the {@link GridElement}s to check
    * @return the result of the collision detection
    */
   CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check);

   /**
    * Returns <code>true</code> if this {@link GridElement} can be avoided or <code>false</code> if not.
    * A {@link GridElement} which is avoidable can be detected by a {@link Detector} in order to avoid a collision. Which means:
    * {@link GridElement} which are not avoidable are de-facto invisible to other {@link GridElement}s
    * 
    * @return <code>true</code> if this {@link GridElement} is gridElement or <code>false</code> if not
    * 
    */
   default boolean isAvoidable() {
      return true;
   }

   /**
    * @return the velocity of this {@link GridElement}
    */
   int getVelocity();

   /**
    * @return 0.0 because by default a {@link GridElement} can't move forward
    */
   default double getSmallestStepWith() {
      return 0.0;
   }
}

/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape;

import java.util.List;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.gridelement.Avoidable;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Position;

/**
 * The {@link Shape} defines the Shape of any {@link GridElement}. A Shape is usually defined by one or more {@link Position}s and is
 * therefore depending on the {@link Position} of it's {@link GridElement}.
 * 
 * @author Dominic
 *
 */
public interface Shape {

   /**
    * Returns the path of this shape described by a line a pen would follow to draw
    * the shape.
    * 
    * @return the path of this shape described by a line a pen would follow to draw
    *         the shape.
    */
   List<Position> getPath();

   /**
    * Checks for every given {@link Avoidable} if there is a collision when the {@link GridElement} of this {@link Shape} is moving
    * from it's current Position to the new Position
    * 
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles the collision if one occurred
    * @param newPosition
    *        the new Position after the movement
    * @param allAvoidables
    *        all {@link Avoidable} on the Grid
    */
   void check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition, List<Avoidable> allAvoidables);

   /**
    * Evaluates if the {@link GridElement} of this {@link Shape} at the given {@link Position} is in reach to this Shape or it's path to be
    * detected by the given Detector
    * 
    * @param detectorPosition
    *        the current Position from which the {@link Detector} try to detect
    * @param detector
    *        the Detector which does the actual detecting
    * 
    * @return <code>true</code> if the avoidable at the specific {@link Position} was detected, <code>false</code> if not
    */
   boolean detectObject(Position detectorPosition, Detector detector);

   /**
    * Verifies if the detected {@link Position}s are within the upper or lower bounds
    * 
    * @param detectedPositions
    *        the detected {@link Position}s
    * @param detectorPos
    *        the Position of the {@link Detector}
    * @return <code>true</code> if the detected {@link Position}s are within the upper bounds or <code>false</code> if not
    */
   boolean isWithinUpperBounds(List<Position> detectedPositions, Position detectorPos);

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
    * @return the radius within this {@link Shape} could be placed
    */
   double getDimensionRadius();

   /**
    * Transform this shape according the new {@link Position}
    * 
    * @param position
    */
   void transform(Position position);
}

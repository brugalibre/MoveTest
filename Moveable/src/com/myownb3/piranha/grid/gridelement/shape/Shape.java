/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;

/**
 * The {@link Shape} defines the Shape of any {@link GridElement}. A Shape is
 * usualy defined by one or more {@link Position}s and is therefore depending on
 * the {@link Position} of it's {@link GridElement}
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

   /**
    * Evaluates if the given {@link Avoidable} at the given {@link Position} is in reach to this Shape or it's path to be detected by the
    * given Detector
    * 
    * @param avoidable
    *        the {@link Avoidable} to detect
    * @param detectorPosition
    *        the current Position from which the {@link Detector} trys to
    *        detect
    * @param detector
    *        the Detector which does the actual detecting
    * @return <code>true</code> if the avoidable at the specific {@link Position} was detected, <code>false</code> if
    *         not
    */
   boolean detectObject(Avoidable avoidable, Position detectorPosition, Detector detector);

   /**
    * Returns the {@link Position} placed on the path which is equivalent to the
    * given Position. That means the position to return faces the same direction.
    * 
    * @param position
    *        the given {@link Position}
    * @return the {@link Position} placed on the path which is equivalent to the
    *         given Position
    */
   Position getPositionOnPathFor(Position position);

   /**
    * Transform this shape according the new {@link Position}
    * 
    * @param position
    */
   void transform(Position position);
}

/**
 * 
 */
package com.myownb3.piranha.detector.collision;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;

/**
 * * The {@link CollisionDetectionHandler} handles any occurrence of a collision
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface CollisionDetectionHandler {

   /**
    * Handles a specific collision between the given {@link Avoidable} at the given
    * {@link Position}
    * 
    * @param avoidable
    *        the {@link Avoidable} with which another
    *        {@link GridElement} was collided
    * @param newPosition
    *        the Position at which the collision occurred
    */
   void handleCollision(Avoidable avoidable, Position newPosition);
}

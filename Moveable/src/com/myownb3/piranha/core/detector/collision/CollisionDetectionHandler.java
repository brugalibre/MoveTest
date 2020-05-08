/**
 * 
 */
package com.myownb3.piranha.core.detector.collision;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.Avoidable;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

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
    * Handles a specific collision between the given {@link Avoidable} and the given {@link GridElement} at the given
    * {@link Position}
    * 
    * @param avoidable
    *        the {@link Avoidable} with which another
    *        {@link GridElement} was collided
    * @param gridElement
    *        the {@link GridElement} which caused the collision with the given {@link Avoidable}
    * @param newPosition
    *        the Position at which the collision occurred
    */
   void handleCollision(Avoidable avoidable, GridElement gridElement, Position newPosition);
}

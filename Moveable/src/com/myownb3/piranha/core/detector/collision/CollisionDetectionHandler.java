/**
 * 
 */
package com.myownb3.piranha.core.detector.collision;

import com.myownb3.piranha.core.grid.Grid;
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
    * Handles a specific collision between the given {@link GridElement},the other given {@link GridElement} at the given
    * {@link Position}
    * 
    * @param otherGridElement
    *        the other {@link GridElement}
    *        {@link GridElement} was collided
    * @param movedGridElement
    *        the {@link GridElement} which caused the collision with the other {@link GridElement}
    * @param newPosition
    *        the Position at which the collision occurred
    */
   void handleCollision(GridElement otherGridElement, GridElement movedGridElement, Position newPosition);
}

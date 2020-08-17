/**
 * 
 */
package com.myownb3.piranha.core.collision;

import java.util.List;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link CollisionDetectionHandler} handles any occurrence of a collision
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface CollisionDetectionHandler {

   /**
    * Handles a specific collision between the given {@link CollisionGridElement} and the other {@link GridElement}s at the given
    * {@link Position}
    * 
    * @param otherGridElements
    *        the other {@link GridElement}s the moved {@link GridElement} was collided with. Note that this list is never <code>null</code>
    *        nor empty
    * @param movedGridElement
    *        the {@link GridElement} which caused the collision with the other {@link GridElement}
    * @param newPosition
    *        the Position at which the collision occurred
    * @return a {@link CollisionDetectionResult} which contains the results of the collision handling
    */
   CollisionDetectionResult handleCollision(List<CollisionGridElement> otherGridElements, GridElement movedGridElement,
         Position newPosition);
}

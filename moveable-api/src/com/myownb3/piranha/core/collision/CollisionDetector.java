package com.myownb3.piranha.core.collision;

import java.util.List;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link CollisionDetector} helps to detect collisions on the {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface CollisionDetector {

   /**
    * Checks for the given surrounding {@link GridElement}s if there is a collision for the given moved {@link GridElement} when moving
    * from the old to the new Position
    * 
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles the collision if one occurred
    * @param movedGridElement
    *        the {@link GridElement} which was moved and is now checked for collision
    * @param oldPosition
    *        the Position before the movement
    * @param newPosition
    *        the new Position after the movement
    * @param gridElements2Check
    *        all {@link GridElement}s which are in reach to collide and which are also 'avoidable'
    * @return a result about the detection which was made by this {@link CollisionDetector}
    */
   CollisionDetectionResult checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement, Position oldPosition,
         Position newPosition, List<GridElement> gridElements2Check);

}

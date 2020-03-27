package com.myownb3.piranha.detector.collision;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;

/**
 * The {@link CollisionDetector} helps to detect collisions on the {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface CollisionDetector {

   /**
    * Checks for every given {@link Avoidable} if there is a collision when moving
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
    * @param allAvoidables
    *        all {@link Avoidable} on the Grid
    */
   void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement, Position oldPosition, Position newPosition,
         List<Avoidable> allAvoidables);

}

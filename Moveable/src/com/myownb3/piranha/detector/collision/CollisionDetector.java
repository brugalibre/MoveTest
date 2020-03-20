package com.myownb3.piranha.detector.collision;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Position;

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
    * @param oldPosition
    *        the Position before the movement
    * @param newPosition
    *        the new Position after the movement
    * @param allAvoidables
    *        all {@link Avoidable} on the Grid
    */
   void checkCollision(Position oldPosition, Position newPosition, List<Avoidable> allAvoidables);

}
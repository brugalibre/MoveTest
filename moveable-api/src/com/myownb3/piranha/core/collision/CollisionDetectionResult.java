package com.myownb3.piranha.core.collision;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link CollisionDetectionResult} contains the result of a collision, detected by a {@link CollisionDetectionHandler}
 * 
 * @author Dominic
 *
 */
public interface CollisionDetectionResult {

   /**
    * @return the by the {@link Grid} moved {@link Position}
    */
   Position getMovedPosition();


   /**
    * @return <code>true</code> if there was a collision or <code>false</code> if not
    */
   boolean isCollision();
}

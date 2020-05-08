package com.myownb3.piranha.core.moveables;

import com.myownb3.piranha.core.grid.gridelement.position.Position;

public interface MoveResult {

   /**
    * @return the distance to the end-position
    */
   double getEndPosDistance();

   /**
    * @return the current {@link Position} of the {@link Moveable}
    */
   Position getMoveablePosition();

   /**
    * 
    * @return <code>true</code> if a {@link EndPointMoveable} has reached it's
    *         end-point. Or <code>false</code> if not
    * 
    */
   boolean isDone();

}

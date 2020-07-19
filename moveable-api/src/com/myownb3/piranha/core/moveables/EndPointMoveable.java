/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link EndPointMoveable} is a {@link Moveable} which has an final end-Point
 * as destination
 * 
 * @author Dominic
 *
 */
public interface EndPointMoveable extends Moveable, Belligerent {

   /**
    * Moving this {@link EndPointMoveable} one increment closer to it's end-point
    * 
    * @return a {@link MoveResult}
    */
   MoveResult moveForward2EndPos();

   /**
    * Returns the {@link Position} to which this {@link EndPointMoveable} is heading to
    * 
    * @return the {@link Position} to which this {@link EndPointMoveable} is heading to
    */
   EndPosition getCurrentEndPos();

   /**
    * Sets the given {@link EndPosition} as it's end-position
    * 
    * @param position
    */
   void setEndPosition(EndPosition position);

   /**
    * Sets the new velocity of this {@link EndPointMoveable}
    * 
    * @param velocity
    *        the new velocity
    */
   void setVelocity(int velocity);
}

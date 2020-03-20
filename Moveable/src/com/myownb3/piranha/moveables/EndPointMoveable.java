/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.gridelement.Position;

/**
 * A {@link EndPointMoveable} is a {@link Moveable} which has an final end-Point
 * as destination
 * 
 * @author Dominic
 *
 */
public interface EndPointMoveable extends Moveable {

   /**
    * Prepares this {@link Moveable} for forwarding to it's end-point. This
    * includes also turning into the right direction
    */
   void prepare();

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
   Position getCurrentEndPos();

   /**
    * Sets the given {@link Position} as it's end-position
    * 
    * @param position
    */
   void setEndPosition(Position position);
}

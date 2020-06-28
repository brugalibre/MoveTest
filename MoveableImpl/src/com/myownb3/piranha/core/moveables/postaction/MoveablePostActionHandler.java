/**
 * 
 */
package com.myownb3.piranha.core.moveables.postaction;

import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link MoveablePostActionHandler} contains the 'post-action' logic of a
 * {@link Moveable}. That means all actions which has to be taken after a
 * certain move of post conditions
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface MoveablePostActionHandler {

   /**
    * Handles post actions for the given {@link Moveable} if
    * necessary. Depending on the actions the given Moveable has done
    * 
    * @param moveable
    *        the {@link Moveable} for which moves this handler post
    *        actions handles
    * @return <code>true</code> if the {@link Moveable} is supposed to continue moving forward and <code>false</code> if it has to stop
    */
   public boolean handlePostConditions(Moveable moveable);

   /**
    * Composes this {@link MoveablePostActionHandler} with the given other {@link MoveablePostActionHandler}
    * The two {@link MoveablePostActionHandler} are composed using short-circuit logical <code>and</code>.
    * 
    * @param other
    *        the other {@link MoveablePostActionHandler} which is to executed after this one
    * @return a combination of this and the other {@link MoveablePostActionHandler}
    */
   default MoveablePostActionHandler andThen(MoveablePostActionHandler other) {
      return moveable -> {
         return this.handlePostConditions(moveable) && other.handlePostConditions(moveable);
      };
   }
}

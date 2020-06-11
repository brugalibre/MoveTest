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
    */
   public void handlePostConditions(Moveable moveable);
}

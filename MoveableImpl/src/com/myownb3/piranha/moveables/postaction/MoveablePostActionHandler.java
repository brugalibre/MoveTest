/**
 * 
 */
package com.myownb3.piranha.moveables.postaction;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;

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
     * Handles post actions for the given {@link Moveable} and {@link Grid} if
     * necessary. Depending on the actions the given Moveable has done
     * 
     * @param grid     the {@link Grid}
     * @param moveable the {@link Moveable} for which moves this handler post
     *                 actions handles
     */
    public void handlePostConditions(Grid grid, Moveable moveable);
}

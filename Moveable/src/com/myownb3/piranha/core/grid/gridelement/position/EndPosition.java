/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.position;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * An {@link EndPosition} defines a {@link Position} which is set for a {@link EndPointMoveable} as it's end-position to reach
 * 
 * @author Dominic
 *
 */
public interface EndPosition extends Position {

   /**
    * Return <code>true</code> if the given {@link Moveable} has reached this {@link EndPosition}
    * 
    * @param moveable
    *        the {@link Moveable} to test
    * @return <code>true</code> if the given {@link Moveable} has reached this {@link EndPosition} otherwise return <code>false</code>
    */
   boolean hasReached(Moveable moveable);

   /**
    * Checks if the given {@link Moveable} at it's current Position has reached this {@link EndPosition}
    * 
    * @param moveable
    *        the {@link Moveable} to check
    * @return <code>true</code> if the given {@link Moveable} has reached this {@link EndPosition}. Otherwise returns
    *         <code>false</code>
    */
   boolean checkIfHasReached(Moveable moveable);
}

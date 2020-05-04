/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.gridelement.position.EndPosition;

/**
 * @author Dominic
 *
 */
public enum MovingStrategy {

   /**
    * Indicates that the {@link MoveableController} leads a {@link Moveable} by
    * moving it 'forward'
    * With this strategy the {@link Moveable} is always lead to an {@link EndPosition}
    */
   FORWARD,

   /**
    * Indicates that the {@link MoveableController} leads a {@link Moveable} by
    * moving it 'backward'
    */
   BACKWARD;
}

/**
 * 
 */
package com.myownb3.piranha.core.moveables.controller;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.moveables.Moveable;

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
    * moving it 'forward'
    * With this strategy the {@link Moveable} is lead forward without an {@link EndPosition}
    */
   FORWARD_WITHOUT_END_POS,

   /**
    * Leads the {@link Moveable} forward for one increment.
    */
   FORWARD_INCREMENTAL,

   /**
    * Indicates that the {@link MoveableController} leads a {@link Moveable} by
    * moving it 'backward'
    */
   BACKWARD;
}

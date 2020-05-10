/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import java.util.List;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A Moveable is an object which is able to move itself on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface Moveable extends GridElement {

   /**
    * The stepwitdh is used by the {@link Direction} as a divisor for dividing the
    * 'move-forward-unit' that any {@link Moveable} can move forward
    * 
    * @see Direction#getForwardX()
    * @see Direction#getForwardY()
    * @see Direction#getBackwardX()
    * @see Direction#getBackwardY()
    */
   public static final int STEP_WITDH = 10;

   /**
    * Return the {@link Position} which this {@link Moveable} had before it moved forward
    * 
    * @return the {@link Position} which this {@link Moveable} had before it moved forward
    */
   Position getPositionBefore();

   /**
    * Moves this {@link Moveable} one unit forward, considering the current
    * {@link Direction}
    */
   void moveForward();

   /**
    * Moves this {@link Moveable} for the given amount of times forward,
    * considering the current {@link Direction}
    * 
    * @param amount
    *        the amount of units to move forward
    */
   void moveForward(int amount);

   /**
    * Moves this {@link Moveable} one unit backward considering the current
    * {@link Direction}
    */
   void moveBackward();

   /**
    * Moves this {@link Moveable} for the given amount of times forward,
    * considering the current {@link Direction}
    * 
    * @param amount
    *        the amount of units to move backward
    */
   void moveBackward(int amount);

   /**
    * Turns this {@link Moveable} to the right
    */
   void turnRight();

   /**
    * Turns this {@link Moveable} to the left
    */
   void turnLeft();

   /**
    * Turns this {@link Moveable} for the given amount of degrees
    * 
    * @param degree
    */
   void makeTurn(double degree);

   /**
    * Turns this {@link Moveable} for the given amount of degrees. Note that there
    * are no post-conditions checked e.g. checking for evasion
    * 
    * @param degree
    */
   void makeTurnWithoutPostConditions(double degree);

   /**
    * 
    * @return a List with all the Points this {@link Moveable} has recently passed
    */
   List<Position> getPositionHistory();

   /**
    * Returns the smallest unit this Moveable can move forward or backward
    * 
    * @return
    */
   default double getSmallestStepWith() {
      return 1d / STEP_WITDH;
   }
}
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
    * Moves this {@link Moveable} one unit backward considering the current
    * {@link Direction}
    */
   void moveBackward();

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
   @Override
   default double getSmallestStepWith() {
      return 1d / MoveableConst.STEP_WITDH;
   }
}

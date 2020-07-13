package com.myownb3.piranha.core.weapon.target;

import java.util.Optional;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link TargetGridElement} is a {@link GridElement} which has been detected as a enemy target
 * Therefore it provides information about it's speed and a target- {@link Position} to acquire. With or without lead
 * 
 * @author Dominic
 *
 */
public interface TargetGridElement {

   /**
    * Evaluates and return <code>true</code> if the two {@link TargetGridElement} are the same
    * 
    * @param targetGridElementAvailableOpt
    *        an {@link Optional} {@link TargetGridElement}
    * @param otherTargetGridElementAvailableOpt
    *        another {@link TargetGridElement} to compare with
    * @return <code>true</code> if both {@link Optional}s are present and their {@link TargetGridElement} identical
    */
   static boolean isSameGridElementTarget(Optional<TargetGridElement> targetGridElementAvailableOpt,
         Optional<TargetGridElement> otherTargetGridElementAvailableOpt) {
      return targetGridElementAvailableOpt
            .map(nearestDetectedTargetGridElement -> nearestDetectedTargetGridElement.isSameGridElementTarget(otherTargetGridElementAvailableOpt))
            .orElse(false);
   }

   /**
    * Evaluates and return <code>true</code> if this {@link TargetGridElement} is the same than the given one
    * 
    * @param currentTargetGridElementOpt
    *        the other {@link TargetGridElement} as an {@link Optional}
    * @return <code>true</code> if this {@link TargetGridElement} is the same than the given one. Otherwise return <code>false</code>
    */
   boolean isSameGridElementTarget(Optional<TargetGridElement> currentTargetGridElementOpt);

   /**
    * @return the {@link GridElement}
    */
   GridElement getGridElement();

   /**
    * @return the target {@link Position} with lead
    */
   Position getTargetPosWithLead2Acquire();

   /**
    * @return current Position of the {@link TargetGridElement}
    */
   Position getCurrentGridElementPosition();

   /**
    * Sets the target {@link Position} with lead
    * 
    * @param targetPosWithLead2Acquire
    */
   void setTargetPosWithLead2Acquire(Position targetPosWithLead2Acquire);

   /**
    * Defines the previously evaluated target {@link Position}
    * 
    * @param prevAcquiredPos
    */
   void setPrevAcquiredPos(Position prevAcquiredPos);

   /**
    * Returns the velocity of this {@link GridElement}. Thant means the amount of 'movements' per cycle the TargetGridElement does
    * 
    * @return the velocity of this {@link GridElement}
    */
   int getTargetVelocity();

   /**
    * @return <code>true</code> if thi {@link TargetGridElement} is moving or <code>false</code> if not
    */
   boolean isMoving();
}

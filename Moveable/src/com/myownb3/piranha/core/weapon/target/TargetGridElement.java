package com.myownb3.piranha.core.weapon.target;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public interface TargetGridElement {

   GridElement getGridElement();

   Position getTargetPosWithLead2Acquire();

   Position getCurrentGridElementPosition();

   void setTargetPosWithLead2Acquire(Position targetPosWithLead2Acquire);

   void setPrevAcquiredPos(Position prevAcquiredPos);

   /**
    * Returns the velocity of this {@link GridElement}. Thant means the amount of 'movements' per cycle the TargetGridElement does
    * 
    * @return the velocity of this {@link GridElement}
    */
   int getTargetVelocity();

   boolean isMoving();

}

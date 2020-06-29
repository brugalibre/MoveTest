package com.myownb3.piranha.core.moveables;

import com.myownb3.piranha.core.grid.direction.Direction;

public class MoveableConst {

   private MoveableConst() {
      // private
   }

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
}

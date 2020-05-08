package com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate;

import static com.myownb3.piranha.util.MathUtil.round;
import static java.lang.Math.abs;
import static java.util.Objects.nonNull;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link Orientation2EndPosHelper} contains helper methods which helps a {@link Moveable} to orient itself
 * 
 * @author Dominic
 *
 */
public class Orientation2EndPosHelper {

   private static final int ANGLE_DECIMALS = 1;

   /**
    * Returns <code>true</code> if the angle between the {@link Moveable}s {@link Position} and it's end-Position is zero
    * Otherwise this method will return <code>false</code>
    * 
    * @param moveable
    *        the {@link Moveable}
    * @param endPos
    *        the desired end-{@link Position}
    * @return <code>true</code> if the angle between the {@link Moveable}s {@link Position} and the given end s end-Position is zero.
    *         Otherwise return <code>false</code>
    */
   public boolean isOrientatingNecessary(Moveable moveable, EndPosition endPos) {
      if (has2CheckOrientation(moveable, endPos)) {
         Position moveablePosition = moveable.getPosition();
         double calcedAngle = round(moveablePosition.calcAngleRelativeTo(endPos), ANGLE_DECIMALS);
         return abs(calcedAngle) != 0.0d;
      }
      return false;
   }

   private boolean has2CheckOrientation(Moveable moveable, EndPosition endPos) {
      return nonNull(endPos) && !endPos.hasReached(moveable);
   }
}

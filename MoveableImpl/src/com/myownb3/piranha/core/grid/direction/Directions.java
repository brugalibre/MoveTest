/**
 * 
 */
package com.myownb3.piranha.core.grid.direction;

/**
 * @author Dominic
 *
 */
public class Directions {

   private Directions() {
      // Do not instantiate
   }

   public static final DirectionImpl N = new DirectionImpl(90, "N");
   public static final DirectionImpl O = new DirectionImpl(0, "O");
   public static final DirectionImpl S = new DirectionImpl(270, "S");
   public static final DirectionImpl W = new DirectionImpl(180, "W");

   public static Direction of(Direction direction) {
      return new DirectionImpl(direction.getAngle(), direction.getCardinalDirection());
   }

   public static Direction of(double forwardX, double forwardY) {
      return new DirectionImpl(forwardX, forwardY);
   }

   /**
    * Creates a new {@link Direction} build with the given {@link Direction} but multiplied its {@link Direction#getForwardX()} and
    * {@link Direction#getForwardX()} with the given multiplier
    * 
    * @param direction
    *        the given {@link Direction}
    * @param multiplier
    *        the multiplier
    * @return a new instance of {@link Direction}
    */
   public static Direction of(Direction direction, int multiplier) {
      return Directions.of(direction.getForwardX() * multiplier, direction.getForwardY() * multiplier);
   }
}

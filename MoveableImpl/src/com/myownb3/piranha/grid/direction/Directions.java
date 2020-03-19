/**
 * 
 */
package com.myownb3.piranha.grid.direction;

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
}

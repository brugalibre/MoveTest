package com.myownb3.piranha.core.moveables.engine.accelerate.transmission;

/**
 * The {@link Gear} defines a gear, the maximal velocity for this gear as well as it's accelerating speed
 * 
 * @author Dominic
 *
 */
public interface Gear {

   /**
    * @return the number of the {@link Gear}
    */
   int getNumber();

   /**
    * @return the max velocity which is possible at this {@link Gear}
    */
   int getMaxVelocity();

   /**
    * @return the acceleration speed of the engine at this {@link Gear}
    */
   double getAccelerationSpeed();

}

package com.myownb3.piranha.core.destruction;

/**
 * {@link Damage} defines how much a {@link Destructive} can damage other {@link Destructible}
 * 
 * @author Dominic
 *
 */
public interface Damage {

   /**
    * @return the value of this {@link Damage}
    */
   double getDamageValue();
}

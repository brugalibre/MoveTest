package com.myownb3.piranha.core.destruction;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * {@link Destructive} defines every class which can damage other {@link Destructible}s
 * 
 * @author Dominic
 *
 */
public interface Destructive {

   /**
    * @return the amount of damage this {@link Destructive} can do to other {@link GridElement}s
    */
   Damage getDamage();
}

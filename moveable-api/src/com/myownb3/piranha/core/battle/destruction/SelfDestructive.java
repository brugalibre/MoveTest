package com.myownb3.piranha.core.battle.destruction;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

@FunctionalInterface
public interface SelfDestructive {

   /**
    * Depeding on the {@link GridElement} this {@link SelfDestructive} is collided with the {@link Damage} is greater or smaller
    * 
    * @param otherGridElement
    *        the other {@link GridElement} we collided with
    * @return the {@link Damage}
    */
   Damage getDamage(GridElement otherGridElement);
}

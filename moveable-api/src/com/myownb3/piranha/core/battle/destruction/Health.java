package com.myownb3.piranha.core.battle.destruction;

/**
 * Defines the state of health of any {@link Destructible}
 * 
 * @author Dominic
 *
 */
public interface Health {

   /**
    * Causes damage to this {@link Health}
    * 
    * @param damage
    *        the {@link Damage} which is caused
    */
   void causeDamage(Damage damage);

   /**
    * @return <code>true</code> if this {@link Health} is still healthy or <code>false</code> if it is already dead
    */
   boolean isHealthy();
}

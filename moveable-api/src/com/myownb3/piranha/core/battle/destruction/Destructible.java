package com.myownb3.piranha.core.battle.destruction;

/**
 * {@link Destructible} defines every class which can be damaged by other {@link Destructive}
 * If a {@link Destructible} is to damaged, it gets destroyed
 * 
 * @author Dominic
 *
 */
public interface Destructible {

   /**
    * @return <code>true</code> if this {@link Destructible} is destroyed or <code>false</code>
    *         if not
    */
   boolean isDestroyed();
}

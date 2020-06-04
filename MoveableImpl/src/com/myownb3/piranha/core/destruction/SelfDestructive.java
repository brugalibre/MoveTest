package com.myownb3.piranha.core.destruction;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link SelfDestructive} is use when a {@link Destructible} gets damaged by a collision with a none {@link Destructive}
 * {@link GridElement}
 * So basically the damage depends on the speed of the {@link Destructible}
 * 
 * @author Dominic
 *
 */
public class SelfDestructive implements Destructive {

   private Damage damage;

   private SelfDestructive(double velocity) {
      this.damage = DamageImpl.of(velocity * 10);
   }

   public static SelfDestructive of(double velocity) {
      return new SelfDestructive(velocity);
   }

   @Override
   public Damage getDamage() {
      return damage;
   }
}

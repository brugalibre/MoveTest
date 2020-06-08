package com.myownb3.piranha.core.destruction;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link DefaultSelfDestructiveImpl} is use when a {@link Destructible} gets damaged by a collision with a none {@link Destructive}
 * {@link GridElement}
 * So basically the damage depends on the speed of the {@link Destructible}
 * 
 * @author Dominic
 *
 */
public class DefaultSelfDestructiveImpl implements SelfDestructive {

   private Damage damage;

   private DefaultSelfDestructiveImpl(double velocity) {
      this.damage = DamageImpl.of(velocity * 10);
   }

   public static DefaultSelfDestructiveImpl of(double velocity) {
      return new DefaultSelfDestructiveImpl(velocity);
   }

   @Override
   public Damage getDamage(GridElement otherGridElement) {
      return damage;
   }
}

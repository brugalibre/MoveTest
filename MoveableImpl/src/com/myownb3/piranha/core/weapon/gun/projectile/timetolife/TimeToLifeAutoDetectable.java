package com.myownb3.piranha.core.weapon.gun.projectile.timetolife;

import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.Health;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;

/**
 * The {@link TimeToLifeAutoDetectable} is responsible for handling a TTL for a {@link Projectile} or a decoy flare
 * After a specific amount of time the {@link Projectile} or decoy flare is going to be destroyed
 * 
 * @author Dominic
 *
 */
public class TimeToLifeAutoDetectable {

   private TimeToLifeAutoDetectable() {
      // private
   }

   /**
    * Creates a A {@link AutoDetectable} which is responsible for handling a TTL for a {@link Projectile} or a decoy flare as a
    * {@link AutoDetectable}
    * 
    * @param gridElement
    *        the self destructible {@link GridElement}
    * @param timeToLife
    *        the amount of cycles until the given {@link GridElement} is destroyed
    * @return a {@link AutoDetectable}
    */
   public static AutoDetectable getTimeToLifeAutoDetectable(Health health, int timeToLife) {
      Counter counter = new Counter();
      return () -> {
         if (counter.incrementAndGet() == timeToLife) {
            health.causeDamage(DamageImpl.of(Double.MAX_VALUE));// Let's kill the health..
         }
      };
   }

   private static final class Counter {
      private int counterValue;

      private int incrementAndGet() {
         return counterValue++;
      }
   }
}

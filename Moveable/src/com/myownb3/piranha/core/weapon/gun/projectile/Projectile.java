package com.myownb3.piranha.core.weapon.gun.projectile;

import com.myownb3.piranha.core.collision.CollisionSensitiveGridElement;
import com.myownb3.piranha.core.weapon.gun.Gun;

/**
 * A {@link Projectile} represents the base class for all projectiles which can be fired by a {@link Gun}
 * 
 * @author Dominic
 *
 */
public interface Projectile extends CollisionSensitiveGridElement {

   /**
    * @return the type of this {@link Projectile}
    */
   ProjectileTypes getProjectileTypes();

   @Override
   default boolean isAimable() {
      return false;
   }

   @Override
   default boolean isAvoidable() {
      return true;
   }

   /**
    * @return <code>true</code> if this {@link Projectile} is destroyed or <code>false</code>
    *         if not
    */
   boolean isDestroyed();
}

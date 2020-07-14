package com.myownb3.piranha.core.battle.weapon.gun.projectile;

import com.myownb3.piranha.core.battle.destruction.Destructible;
import com.myownb3.piranha.core.battle.destruction.Destructive;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.battle.weapon.gun.Gun;
import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

/**
 * A {@link Projectile} represents the base class for all projectiles which can be fired by a {@link Gun}
 * 
 * @author Dominic
 *
 */
public interface Projectile extends Destructible, Destructive, CollisionSensitive, AutoDetectable {

   /**
    * @return the {@link Shape} of this {@link Projectile}
    */
   Shape getShape();

   /**
    * @return the type of this {@link Projectile} as a {@link ProjectileTypes}
    */
   ProjectileTypes getProjectileType();
}

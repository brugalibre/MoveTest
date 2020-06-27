package com.myownb3.piranha.core.weapon.gun.projectile;

import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.destruction.Destructible;
import com.myownb3.piranha.core.destruction.Destructive;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.core.weapon.gun.Gun;

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
}

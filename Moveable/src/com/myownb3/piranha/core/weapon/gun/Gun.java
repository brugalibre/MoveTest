package com.myownb3.piranha.core.weapon.gun;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.weapon.turret.Turret;

/**
 * The interface {@link Gun} represents a gun of a {@link Turret}
 * 
 * @author Dominic
 *
 */
public interface Gun {

   /**
    * @return the {@link GunShape} of this {@link Gun}
    */
   GunShape getShape();

   /**
    * Fires one salve of this {@link Gun} - depending on the actual implementation
    */
   void fire();

   /**
    * @return the {@link GunConfig} of this {@link Gun}
    */
   GunConfig getGunConfig();

   /**
    * Evaluates and sets the {@link Position} of this gun regarding it's dimensions and the {@link Position} where the gun is mounted
    * 
    * @param gunMountPosition
    */
   void evalAndSetGunPosition(Position gunMountPosition);
}

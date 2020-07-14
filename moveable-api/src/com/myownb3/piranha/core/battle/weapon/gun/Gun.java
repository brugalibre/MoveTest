package com.myownb3.piranha.core.battle.weapon.gun;

import java.io.Serializable;

import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The interface {@link Gun} represents a gun of a {@link Turret}
 * 
 * @author Dominic
 *
 */
public interface Gun extends Serializable {

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

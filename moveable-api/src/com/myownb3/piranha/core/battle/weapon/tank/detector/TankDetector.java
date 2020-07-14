package com.myownb3.piranha.core.battle.weapon.tank.detector;

import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;

/**
 * The {@link TankDetector} is used to check the sorroundings of a {@link Tank} for any missiles or other {@link Projectile}s
 * 
 * @author Dominic
 *
 */
public interface TankDetector extends AutoDetectable {

   /**
    * @return <code>true</code> this {@link TankDetector} has detected any missiles or {@link Projectile} arriving
    */
   boolean isUnderFire();
}

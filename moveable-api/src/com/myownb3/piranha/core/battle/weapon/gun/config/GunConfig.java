package com.myownb3.piranha.core.battle.weapon.gun.config;

import com.myownb3.piranha.core.battle.weapon.gun.Gun;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;

/**
 * The {@link ProjectileConfig} contains the configuration for a {@link Gun}
 * 
 * @author Dominic
 *
 */
public interface GunConfig {

   /**
    * @return the amount of shots fired per minute
    */
   int getRoundsPerMinute();

   /**
    * @return size of a salve the {@link Gun} can fire at once
    */
   int getSalveSize();

   /**
    * @return {@link ProjectileConfig}
    */
   ProjectileConfig getProjectileConfig();
}

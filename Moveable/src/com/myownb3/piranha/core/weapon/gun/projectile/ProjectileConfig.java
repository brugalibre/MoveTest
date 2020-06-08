package com.myownb3.piranha.core.weapon.gun.projectile;

import com.myownb3.piranha.core.grid.Dimension;

/**
 * The {@link ProjectileConfig} contains the configuration for a {@link Projectile}
 * 
 * @author Dominic
 *
 */
public interface ProjectileConfig {

   /**
    * @return dimension of the {@link Projectile}
    */
   Dimension getProjectileDimension();
}
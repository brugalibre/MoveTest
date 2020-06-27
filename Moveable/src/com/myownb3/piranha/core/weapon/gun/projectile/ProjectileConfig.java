package com.myownb3.piranha.core.weapon.gun.projectile;

import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.weapon.target.TargetGridElementEvaluator;

/**
 * The {@link ProjectileConfig} contains the configuration for a {@link Projectile}
 * 
 * @author Dominic
 *
 */
public interface ProjectileConfig {

   /**
    * @return the {@link DimensionInfo} for a {@link Projectile}
    */
   DimensionInfo getDimensionInfo();

   /**
    * @return the velocity of the {@link Projectile}
    */
   int getVelocity();

   /**
    * @return the damage of the {@link Projectile}
    */
   double getProjectileDamage();

   /**
    * 
    * @return the {@link TargetGridElementEvaluator}
    */
   TargetGridElementEvaluator getTargetGridElementEvaluator();

}

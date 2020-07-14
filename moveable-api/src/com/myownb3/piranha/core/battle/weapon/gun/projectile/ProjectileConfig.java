package com.myownb3.piranha.core.battle.weapon.gun.projectile;

import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;

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

package com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;

/**
 * The {@link ProjectileStrategyHandler} defines a handler which handles a strategy depending on a {@link ProjectileTypes}
 * 
 * @author Dominic
 *
 */
public interface ProjectileStrategyHandler {

   /**
    * Handles the specific strategy of the {@link Projectile}
    */
   void handleProjectileStrategy();

}

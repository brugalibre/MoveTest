package com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.factory;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.DefaultProjectileStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.MissileProjectileStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.ProjectileStrategyHandler;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

public class ProjectileStrategyHandlerFactory {

   /** The singleton instance of a {@link ProjectileStrategyHandlerFactory} */
   public static final ProjectileStrategyHandlerFactory INSTANCE = new ProjectileStrategyHandlerFactory();

   /**
    * Creates a new {@link ProjectileStrategyHandler} depending on the given {@link ProjectileTypes}
    * 
    * @param projectileType
    *        the type of projectile
    * @param projectileConfig
    * @param shape
    *        the {@link Projectile} Shape
    * @return a {@link ProjectileStrategyHandler}
    */
   public ProjectileStrategyHandler getProjectileStrategyHandler(ProjectileTypes projectileType, ProjectileConfig projectileConfig, Shape shape) {
      switch (projectileType) {
         case LASER_BEAM:
            // fall through
         case BULLET:
            return DefaultProjectileStrategyHandler.of(shape);
         case MISSILE:
            return MissileProjectileStrategyHandler.of(projectileConfig.getTargetGridElementEvaluator(), shape, projectileConfig.getVelocity(),
                  projectileConfig.getMissileRotationSpeed());
         default:
            throw new IllegalArgumentException("Unknown ProjectileTypes '" + projectileType + "';");
      }
   }

}

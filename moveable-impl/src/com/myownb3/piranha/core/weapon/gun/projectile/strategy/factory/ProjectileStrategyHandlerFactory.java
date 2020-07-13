package com.myownb3.piranha.core.weapon.gun.projectile.strategy.factory;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.projectile.strategy.BulletProjectileStrategyHandler;
import com.myownb3.piranha.core.weapon.gun.projectile.strategy.MissileProjectileStrategyHandler;
import com.myownb3.piranha.core.weapon.gun.projectile.strategy.ProjectileStrategyHandler;

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
         case BULLET:
            return BulletProjectileStrategyHandler.of(shape);
         case MISSILE:
            return MissileProjectileStrategyHandler.of(projectileConfig.getTargetGridElementEvaluator(), shape, projectileConfig.getVelocity());
         default:
            throw new IllegalArgumentException("Unknown ProjectileTypes '" + projectileType + "';");
      }
   }

}

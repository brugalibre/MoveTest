package com.myownb3.piranha.core.weapon.gun.projectile.factory;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.BulletImpl;
import com.myownb3.piranha.core.weapon.gun.projectile.BulletImpl.BulletBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;

/**
 * The {@link ProjectileFactory} is responsible for creating {@link Projectile}s of any kkind
 * 
 * @author Dominic
 *
 */
public class ProjectileFactory {

   public static final ProjectileFactory INSTANCE = new ProjectileFactory();
   private static final int AMOINT_OF_CIRCLE_BULLET_POINTS = 20;
   private Grid grid;

   /**
    * Creates a new {@link Projectile} for the given {@link ProjectileTypes} at the given {@link Position}
    * 
    * @param type
    *        the {@link ProjectileTypes}
    * @param position
    *        the start {@link Position} of the {@link Projectile}
    * @param projectileConfig
    *        the {@link ProjectileConfig} which defines the configuration for the projectile
    * @return
    */
   public Projectile createProjectile(ProjectileTypes type, Position position, ProjectileConfig projectileConfig) {
      checkGrid();
      switch (type) {
         case BULLET:
            return createNewBullet(position, projectileConfig);
         default:
            throw new IllegalArgumentException("Unsupported type of projectile '" + type + "'");
      }
   }

   private BulletImpl createNewBullet(Position position, ProjectileConfig projectileConfig) {
      return BulletBuilder.builder()
            .withGrid(grid)
            .withPosition(position)
            .withShape(CircleBuilder.builder()
                  .withAmountOfPoints(AMOINT_OF_CIRCLE_BULLET_POINTS)
                  .withRadius(projectileConfig.getProjectileDimension().getHeight())
                  .withCenter(position)
                  .build())
            .build();
   }

   public void registerGrid(Grid grid) {
      this.grid = requireNonNull(grid);
   }

   private void checkGrid() {
      if (isNull(grid)) {
         throw new IllegalStateException("Use 'ProjectileFactory.INSTANCE.registerGrid(myGrid)' before creating any projectiles!");
      }
   }

   /**
    * removes a previously registered {@link Grid}
    */
   public void degisterGrid() {
      this.grid = null;
   }
}

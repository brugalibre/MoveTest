package com.myownb3.piranha.core.weapon.gun.projectile.factory;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;

/**
 * The {@link ProjectileFactory} is responsible for creating {@link Projectile}s of any kind
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
      Shape projectileShape;
      switch (type) {
         case BULLET:
            projectileShape = buildBulletShape(position, projectileConfig);
            break;
         default:
            throw new IllegalArgumentException("Unsupported type of projectile '" + type + "'");
      }
      return createNewProjectile(position, projectileShape, projectileConfig.getBelligerentParty());
   }

   private ProjectileGridElement createNewProjectile(Position position, Shape projectileShape, BelligerentParty belligerentParty) {
      return ProjectileGridElementBuilder.builder()
            .withGrid(grid)
            .withPosition(position)
            .withBelligerentParty(belligerentParty)
            .withShape(projectileShape)
            .build();
   }

   private static CircleImpl buildBulletShape(Position position, ProjectileConfig projectileConfig) {
      return CircleBuilder.builder()
            .withAmountOfPoints(AMOINT_OF_CIRCLE_BULLET_POINTS)
            .withRadius(projectileConfig.getProjectileDimension().getHeight())
            .withCenter(position)
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
   public void deregisterGrid() {
      this.grid = null;
   }
}

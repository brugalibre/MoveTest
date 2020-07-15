package com.myownb3.piranha.core.battle.weapon.gun.projectile.factory;

import com.myownb3.piranha.core.battle.destruction.DestructionAudio;
import com.myownb3.piranha.core.battle.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.grid.gridelement.LazyGridElement;
import com.myownb3.piranha.core.grid.gridelement.factory.AbstractGridElementFactory;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link ProjectileFactory} is responsible for creating {@link Projectile}s of any kind
 * 
 * @author Dominic
 *
 */
public class ProjectileFactory extends AbstractGridElementFactory {

   public static final ProjectileFactory INSTANCE = new ProjectileFactory();
   private static final int AMOINT_OF_CIRCLE_BULLET_POINTS = 20;

   /**
    * Creates a new {@link Projectile} for the given {@link ProjectileTypes} at the given {@link Position}
    * 
    * @param type
    *        the {@link ProjectileTypes}
    * @param position
    *        the start {@link Position} of the {@link Projectile}
    * @param projectileConfig
    *        the {@link ProjectileConfig} which defines the configuration for the projectile
    * @return a new {@link ProjectileGridElement}
    */
   public ProjectileGridElement createProjectile(ProjectileTypes type, Position position, ProjectileConfig projectileConfig) {
      checkGrid();
      Shape projectileShape;
      switch (type) {
         case BULLET:
            projectileShape = buildBulletShape(position, projectileConfig);
            break;
         case LASER_BEAM:
            projectileShape = buildLaserBeamShape(position, projectileConfig);
            break;
         case MISSILE:
            projectileShape = buildMissileShape(position, projectileConfig);
            break;
         default:
            throw new IllegalArgumentException("Unsupported type of projectile '" + type + "'");
      }
      return createNewProjectile(projectileShape, projectileConfig, type);
   }

   private ProjectileGridElement createNewProjectile(Shape projectileShape, ProjectileConfig projectileConfig, ProjectileTypes type) {
      LazyGridElement lazyGridElement = new LazyGridElement();
      ProjectileGridElement projectileGridElement = ProjectileGridElementBuilder.builder()
            .withGrid(grid)
            .withDimensionInfo(projectileConfig.getDimensionInfo())
            .withVelocity(projectileConfig.getVelocity())
            .withProjectile(ProjectileBuilder.builder()
                  .withDamage(projectileConfig.getProjectileDamage())
                  .withProjectileTypes(type)
                  .withOnDestroyedCallbackHandler(getOnDestroyedCallbackHandler(lazyGridElement, type))
                  .withShape(projectileShape)
                  .withProjectileConfig(projectileConfig)
                  .build())
            .build();
      lazyGridElement.setGridElement(projectileGridElement);
      return projectileGridElement;
   }

   private OnDestroyedCallbackHandler getOnDestroyedCallbackHandler(LazyGridElement lazyGridElement, ProjectileTypes type) {
      return getDefaultOnDestroyedCallbackHandler(lazyGridElement).andThen(() -> {
         if (type == ProjectileTypes.MISSILE) {
            new DestructionAudio().playDefaultExplosion();
         }
      });
   }

   private static CircleImpl buildBulletShape(Position position, ProjectileConfig projectileConfig) {
      return CircleBuilder.builder()
            .withAmountOfPoints(AMOINT_OF_CIRCLE_BULLET_POINTS)
            .withRadius((int) projectileConfig.getDimensionInfo().getDimensionRadius())
            .withCenter(position)
            .build();
   }

   private Rectangle buildMissileShape(Position position, ProjectileConfig projectileConfig) {
      int dimensionRadius = (int) projectileConfig.getDimensionInfo().getDimensionRadius();
      return buildRectangleShape(position, dimensionRadius, 3d * dimensionRadius);
   }

   private Rectangle buildLaserBeamShape(Position position, ProjectileConfig projectileConfig) {
      int dimensionRadius = (int) projectileConfig.getDimensionInfo().getDimensionRadius();
      return buildRectangleShape(position, dimensionRadius, 5d * dimensionRadius);
   }

   private Rectangle buildRectangleShape(Position position, int dimensionRadius, double width) {
      return RectangleBuilder.builder()
            .withCenter(position)
            .withHeight(dimensionRadius)
            .withWidth(width)
            .withOrientation(Orientation.VERTICAL)
            .build();
   }
}

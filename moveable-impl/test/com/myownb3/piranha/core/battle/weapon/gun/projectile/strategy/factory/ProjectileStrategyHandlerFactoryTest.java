package com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.factory;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.DefaultProjectileStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.MissileProjectileStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.ProjectileStrategyHandler;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Positions;

class ProjectileStrategyHandlerFactoryTest {

   @Test
   void testGetBulletProjectileStrategyHandler() {

      // Given
      ProjectileTypes projectileType = ProjectileTypes.BULLET;
      Shape shape = PositionShapeBuilder.builder()
            .withPosition(Positions.of(5, 5)).build();

      // When
      ProjectileStrategyHandler projectileStrategyHandler =
            ProjectileStrategyHandlerFactory.INSTANCE.getProjectileStrategyHandler(projectileType, mock(ProjectileConfig.class), shape);

      // Then
      assertThat(projectileStrategyHandler, instanceOf(DefaultProjectileStrategyHandler.class));
   }

   @Test
   void testGetMissileProjectileStrategyHandler() {

      // Given
      ProjectileTypes projectileType = ProjectileTypes.MISSILE;
      Shape shape = PositionShapeBuilder.builder()
            .withPosition(Positions.of(5, 5)).build();

      // When
      ProjectileStrategyHandler projectileStrategyHandler =
            ProjectileStrategyHandlerFactory.INSTANCE.getProjectileStrategyHandler(projectileType, mock(ProjectileConfig.class), shape);

      // Then
      assertThat(projectileStrategyHandler, instanceOf(MissileProjectileStrategyHandler.class));
   }

   @Test
   void testGetProjectileStrategyHandler_ForUnknownType() {

      // Given
      ProjectileTypes projectileType = ProjectileTypes.NONE;

      // When
      Executable exe = () -> {
         ProjectileStrategyHandlerFactory.INSTANCE.getProjectileStrategyHandler(projectileType, mock(ProjectileConfig.class),
               mock(Shape.class));
      };
      // Then
      assertThrows(IllegalArgumentException.class, exe);
   }

}

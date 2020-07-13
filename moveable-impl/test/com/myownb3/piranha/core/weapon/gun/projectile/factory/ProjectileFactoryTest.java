package com.myownb3.piranha.core.weapon.gun.projectile.factory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;

class ProjectileFactoryTest {

   @BeforeEach
   public void tearDown() {
      ProjectileFactory.INSTANCE.deregisterGrid();
   }

   @Test
   void testOnCollision_Twice_RemoveDestroyedProjectile() {

      // Given
      MirrorGrid grid = spy(MirrorGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(0)
            .build());

      ProjectileFactory.INSTANCE.registerGrid(grid);

      Position pos = Positions.of(9.5, 8.5);
      ProjectileConfig projectileConfig = ProjectileConfigBuilder.builder()
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
            .withVelocity(5)
            .build();
      ProjectileGridElement projectileGridElementent = ProjectileFactory.INSTANCE.createProjectile(ProjectileTypes.MISSILE, pos, projectileConfig);

      // When
      projectileGridElementent.onCollision(Collections.singletonList(mock(Wall.class))); // first time
      projectileGridElementent.onCollision(Collections.singletonList(mock(Wall.class)));// second time

      // Then
      assertThat(projectileGridElementent.isDestroyed(), is(true));
      verify(grid).remove(eq(projectileGridElementent));
   }

   @Test
   void testCreateFromTypeMissile() {

      // Given
      ProjectileFactory.INSTANCE.registerGrid(mock(Grid.class));
      Position pos = Positions.of(5, 5);

      // When
      ProjectileConfig projectileConfig = mockProjectileConfig();
      when(projectileConfig.getDimensionInfo()).thenReturn(DimensionInfoBuilder.getDefaultDimensionInfo(5));
      Projectile projectile = ProjectileFactory.INSTANCE.createProjectile(ProjectileTypes.MISSILE, pos, projectileConfig);

      // Then
      assertThat(((ProjectileGridElement) projectile).getPosition(), is(pos));
   }

   @Test
   void testCreateFromTypeBullet() {

      // Given
      ProjectileFactory.INSTANCE.registerGrid(mock(Grid.class));
      Position pos = Positions.of(5, 5);

      // When
      ProjectileConfig projectileConfig = mockProjectileConfig();
      when(projectileConfig.getDimensionInfo()).thenReturn(DimensionInfoBuilder.getDefaultDimensionInfo(5));
      Projectile projectile = ProjectileFactory.INSTANCE.createProjectile(ProjectileTypes.BULLET, pos, projectileConfig);

      // Then
      assertThat(((ProjectileGridElement) projectile).getPosition(), is(pos));
   }

   @Test
   void testCreateFromTypeNone_WithGrid() {

      // Given
      ProjectileFactory.INSTANCE.registerGrid(mock(Grid.class));

      // When
      Executable exec = () -> {
         ProjectileFactory.INSTANCE.createProjectile(ProjectileTypes.NONE, null, mock(ProjectileConfig.class));
      };

      // Then
      assertThrows(IllegalArgumentException.class, exec);
   }

   @Test
   void testCreateFromTypeNone_WithoutGrid() {

      // Given

      // When
      Executable exec = () -> {
         ProjectileFactory.INSTANCE.createProjectile(ProjectileTypes.NONE, null, mock(ProjectileConfig.class));
      };

      // Then
      assertThrows(IllegalStateException.class, exec);
   }

   private ProjectileConfig mockProjectileConfig() {
      ProjectileConfig projectileConfig = mock(ProjectileConfig.class);
      when(projectileConfig.getVelocity()).thenReturn(1);
      return projectileConfig;
   }

}

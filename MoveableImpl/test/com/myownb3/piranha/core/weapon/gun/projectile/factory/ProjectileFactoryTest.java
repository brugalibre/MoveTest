package com.myownb3.piranha.core.weapon.gun.projectile.factory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;

class ProjectileFactoryTest {

   @Test
   void testCreateFromTypeBullet() {

      // Given
      ProjectileFactory.INSTANCE.registerGrid(mock(Grid.class));
      Position pos = Positions.of(5, 5);

      // When
      ProjectileConfig projectileConfig = mock(ProjectileConfig.class);
      when(projectileConfig.getProjectileDimension()).thenReturn(new DimensionImpl(0, 0, 5, 5));
      Projectile projectile = ProjectileFactory.INSTANCE.createProjectile(ProjectileTypes.BULLET, pos, projectileConfig);

      // Then
      assertThat(projectile.getPosition(), is(pos));
      assertThat(projectile.getProjectileTypes(), is(ProjectileTypes.BULLET));
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

}

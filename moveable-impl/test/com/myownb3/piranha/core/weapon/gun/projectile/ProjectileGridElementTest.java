package com.myownb3.piranha.core.weapon.gun.projectile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;

class ProjectileGridElementTest {
   @Test
   void test_StopMovingWhenDestroyed() {

      // Given
      Position startPos = Positions.of(0, 0);
      Position expectedPosition = Positions.of(0, 0.1);
      ProjectileGridElement moveable = ProjectileGridElementBuilder.builder()
            .withProjectile(ProjectileBuilder.builder()
                  .withShape(PositionShapeBuilder.builder()
                        .withPosition(startPos)
                        .build())
                  .withHealth(-2)
                  .withProjectileTypes(ProjectileTypes.BULLET)
                  .build())
            .withVelocity(10)
            .withGrid(GridBuilder.builder()
                  .build())
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
            .build();

      // When
      moveable.moveForward();

      // Then
      assertThat(moveable.getPosition(), is(expectedPosition));
   }

   @Test
   void testAutoDetectable() {

      // Given
      ProjectileGridElement projectileGridElement = spy(ProjectileGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMaxY(50)
                  .build())
            .withProjectile(ProjectileBuilder.builder()
                  .withDamage(10)
                  .withHealth(5)
                  .withShape(PositionShapeBuilder.builder()
                        .withPosition(Positions.of(5, 5))
                        .build())
                  .withProjectileTypes(ProjectileTypes.BULLET)
                  .build())
            .withVelocity(10)
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(10))
            .build());

      // When
      projectileGridElement.autodetect();

      // Then
      verify(projectileGridElement).moveForward();
   }

   @Test
   void testGetProjectileType() {

      // Given
      ProjectileImpl projectileImpl = spy(ProjectileBuilder.builder()
            .withDamage(10)
            .withHealth(5)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 5))
                  .build())
            .withProjectileTypes(ProjectileTypes.BULLET)
            .build());
      ProjectileGridElement projectileGridElement = spy(ProjectileGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMaxY(50)
                  .build())
            .withProjectile(projectileImpl)
            .withVelocity(10)
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(10))
            .build());

      // When
      projectileGridElement.getProjectileType();

      // Then
      verify(projectileImpl).getProjectileType();
   }

   @Test
   void testIsProjectileDestroyed() {

      // Given

      // When
      boolean actualIsDestroyed = ProjectileGridElement.isDestroyed(null);

      // Then
      assertThat(actualIsDestroyed, is(true));
   }

   @Test
   void testGetDamage() {

      // Given
      double damageValue = 10;
      Projectile projectile = mockProjectile(damageValue);
      ProjectileGridElement projectileGridElement = ProjectileGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withVelocity(10)
            .withProjectile(projectile)
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(10))
            .build();

      // When
      Damage actualDamage = projectileGridElement.getDamage();

      // Then
      assertThat(actualDamage.getDamageValue(), is(damageValue));
   }

   private Projectile mockProjectile(double damageValue) {
      Projectile projectile = mock(Projectile.class);
      Damage damage = mockDamage(damageValue);
      when(projectile.getDamage()).thenReturn(damage);
      when(projectile.getShape()).thenReturn(PositionShapeBuilder.builder()
            .withPosition(Positions.of(5, 5))
            .build());
      return projectile;
   }

   private Damage mockDamage(double damageValue) {
      Damage damage = mock(Damage.class);
      when(damage.getDamageValue()).thenReturn(damageValue);
      return damage;
   }
}

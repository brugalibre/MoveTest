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
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(startPos)
                  .build())
            .withVelocity(10)
            .withPosition(Positions.of(0, 0))
            .withGrid(GridBuilder.builder()
                  .build())
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
            .withHealth(-2)
            .build();

      // When
      moveable.moveForward(10);

      // Then
      assertThat(moveable.getPosition(), is(expectedPosition));
   }

   @Test
   void testAutoDetectable() {

      // Given
      ProjectileGridElement projectileGridElement = spy(ProjectileGridElementBuilder.builder()
            .withDamage(10)
            .withGrid(mock(Grid.class))
            .withHealth(5)
            .withPosition(Positions.of(5, 5))
            .withShape(mock(AbstractShape.class))
            .withProjectile(ProjectileBuilder.builder()
                  .build())
            .withVelocity(10)
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(10))
            .withPosition(Positions.of(5, 5))
            .build());
      when(projectileGridElement.getPosition()).thenReturn(Positions.of(5, 5));

      // When
      projectileGridElement.autodetect();

      // Then
      verify(projectileGridElement).moveForward(10);
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
            .withDamage(damageValue)
            .withGrid(mock(Grid.class))
            .withHealth(5)
            .withPosition(Positions.of(5, 5))
            .withShape(mock(AbstractShape.class))
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
      return projectile;
   }

   private Damage mockDamage(double damageValue) {
      Damage damage = mock(Damage.class);
      when(damage.getDamageValue()).thenReturn(damageValue);
      return damage;
   }
}

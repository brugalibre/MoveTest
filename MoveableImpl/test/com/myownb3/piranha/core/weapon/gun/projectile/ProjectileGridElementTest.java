package com.myownb3.piranha.core.weapon.gun.projectile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;

class ProjectileGridElementTest {

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
            .withProjectile(projectile)
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

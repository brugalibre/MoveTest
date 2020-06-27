package com.myownb3.piranha.core.weapon.gun.projectile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;

class ProjectileImplTest {

   @Test
   void testGetDamage() {

      // Given
      double expectedDamageValue = 10.0;
      Projectile projectile = ProjectileBuilder.builder()
            .withDamage(expectedDamageValue)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 5))
                  .build())
            .build();

      // When
      Damage damage = projectile.getDamage();

      // Then
      assertThat(damage.getDamageValue(), is(expectedDamageValue));
   }

}

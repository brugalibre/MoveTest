package com.myownb3.piranha.core.battle.weapon.gun.projectile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.destruction.Damage;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Positions;

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
            .withProjectileConfig(mock(ProjectileConfig.class))
            .withProjectileTypes(ProjectileTypes.BULLET)
            .build();

      // When
      Damage damage = projectile.getDamage();

      // Then
      assertThat(damage.getDamageValue(), is(expectedDamageValue));
   }

}

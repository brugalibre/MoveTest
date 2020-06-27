package com.myownb3.piranha.core.weapon.gun.projectile.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;

class ProjectileConfigImplTest {

   @Test
   void testGetProjectileDamage() {

      // Given
      double damage = 14.0;

      // When
      ProjectileConfig projectileConfig = ProjectileConfigBuilder.builder()
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
            .withVelocity(5)
            .withProjectileDamage(damage)
            .build();

      // Then
      assertThat(projectileConfig.getProjectileDamage(), is(damage));
   }

}

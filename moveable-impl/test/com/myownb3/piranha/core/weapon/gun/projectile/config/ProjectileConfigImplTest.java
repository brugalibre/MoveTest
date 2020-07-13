package com.myownb3.piranha.core.weapon.gun.projectile.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.target.TargetGridElementEvaluator;

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

   @Test
   void testGetProjectileSchnappiDamage() {

      // Given
      TargetGridElementEvaluator targetGridElementEvaluator = mock(TargetGridElementEvaluator.class);

      // When
      ProjectileConfig projectileConfig = ProjectileConfigBuilder.builder()
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
            .withVelocity(5)
            .withProjectileDamage(5)
            .withTargetGridElementEvaluator(targetGridElementEvaluator)
            .build();

      // Then
      assertThat(projectileConfig.getTargetGridElementEvaluator(), is(targetGridElementEvaluator));
   }

}

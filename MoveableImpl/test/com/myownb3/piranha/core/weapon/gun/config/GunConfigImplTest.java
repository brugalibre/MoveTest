package com.myownb3.piranha.core.weapon.gun.config;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;

class GunConfigImplTest {

   @Test
   void test_InvalidRoundsPerMinute() {

      // Given
      int roundsPerMinute = -1;

      // When
      Executable exec = () -> {
         GunConfigBuilder.builder()
               .withRoundsPerMinute(roundsPerMinute)
               .withProjectileConfig(ProjectileConfigBuilder.builder()
                     .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                     .withVelocity(1)
                     .build())
               .withSalveSize(1)
               .build();
      };

      // Then
      assertThrows(IllegalArgumentException.class, exec);
   }

   @Test
   void test_InvalidSalveSize() {

      // Given
      int salveSize = -1;

      // When
      Executable exec = () -> {
         GunConfigBuilder.builder()
               .withRoundsPerMinute(1)
               .withProjectileConfig(ProjectileConfigBuilder.builder()
                     .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                     .withVelocity(1)
                     .build())
               .withSalveSize(salveSize)
               .build();
      };

      // Then
      assertThrows(IllegalArgumentException.class, exec);
   }

   @Test
   void test_InvalidVelocity() {

      // Given
      int velocity = -1;

      // When
      Executable exec = () -> {
         GunConfigBuilder.builder()
               .withRoundsPerMinute(1)
               .withProjectileConfig(ProjectileConfigBuilder.builder()
                     .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                     .withVelocity(velocity)
                     .build())
               .withSalveSize(1)
               .build();
      };

      // Then
      assertThrows(IllegalArgumentException.class, exec);
   }

}

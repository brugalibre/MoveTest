package com.myownb3.piranha.core.weapon.gun.config;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.grid.Dimension;
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
                     .withDimension(mock(Dimension.class))
                     .build())
               .withSalveSize(1)
               .withVelocity(1)
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
                     .withDimension(mock(Dimension.class))
                     .build())
               .withSalveSize(salveSize)
               .withVelocity(1)
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
                     .withDimension(mock(Dimension.class))
                     .build())
               .withSalveSize(1)
               .withVelocity(velocity)
               .build();
      };

      // Then
      assertThrows(IllegalArgumentException.class, exec);
   }

}

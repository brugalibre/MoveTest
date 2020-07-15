package com.myownb3.piranha.core.battle.weapon.gun.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;

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

   @Test
   void test_WithAudioClip() {

      // Given
      AudioClip audioClip = mock(AudioClip.class);

      // When
      GunConfig gunConfig = GunConfigBuilder.builder()
            .withRoundsPerMinute(1)
            .withProjectileConfig(ProjectileConfigBuilder.builder()
                  .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                  .withVelocity(1)
                  .build())
            .withSalveSize(1)
            .withAudioClip(audioClip)
            .build();

      // Then
      assertThat(gunConfig.getAudioClip(), is(audioClip));
   }
}

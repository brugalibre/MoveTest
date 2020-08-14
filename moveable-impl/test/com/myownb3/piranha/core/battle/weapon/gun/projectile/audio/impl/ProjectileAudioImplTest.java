package com.myownb3.piranha.core.battle.weapon.gun.projectile.audio.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.audio.impl.ProjectileAudioImpl.ProjectileAudioBuilder;

class ProjectileAudioImplTest {

   @Test
   void testPlayOnCollisionAudio_UnknownProjectileType_DestructionAudio() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.NONE, false);

      // When
      Executable executable = () -> ProjectileAudioBuilder.builder()
            .withProjectile(projectile)
            .withProjectileDestructionAudioClip()
            .build();

      // Then
      assertThrows(IllegalStateException.class, executable);
   }

   @Test
   void testPlayOnCollisionAudio_UnknownProjectileType_ImpactAudio() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.NONE, false);

      // When
      Executable executable = () -> ProjectileAudioBuilder.builder()
            .withProjectile(projectile)
            .withProjectileImpactAudioClip()
            .build();

      // Then
      assertThrows(IllegalStateException.class, executable);
   }

   @Test
   void testPlayOnCollisionAudio_ProjectileTypeBullet() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.BULLET, false);
      ProjectileAudioImpl ProjectileAudio = spy(buildProjectileAudio(projectile));

      // When
      ProjectileAudio.playOnCollisionAudio();

      // Then
      verify(ProjectileAudio).playImpactAudio();
   }

   @Test
   void testPlayOnCollisionAudio_ProjectileTypeMissile() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.MISSILE, false);
      ProjectileAudioImpl ProjectileAudio = spy(buildProjectileAudio(projectile));

      // When
      ProjectileAudio.playOnCollisionAudio();

      // Then
      verify(ProjectileAudio).playImpactAudio();
   }

   @Test
   void testPlayOnCollisionAudio_ProjectileTypeMissile_Destroyed() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.MISSILE, true);
      ProjectileAudioImpl projectileAudio = spy(buildProjectileAudio(projectile));

      // When
      projectileAudio.playOnCollisionAudio();

      // Then
      verify(projectileAudio).playDestructionAudio();
   }

   private Projectile mockProjectile(ProjectileTypes projectileTypes, boolean isDestroyed) {
      Projectile projectile = mock(Projectile.class);
      when(projectile.getProjectileType()).thenReturn(projectileTypes);
      when(projectile.isDestroyed()).thenReturn(isDestroyed);
      return projectile;
   }

   private ProjectileAudioImpl buildProjectileAudio(Projectile projectile) {
      return ProjectileAudioBuilder.builder()
            .withProjectile(projectile)
            .withProjectileImpactAudioClip()
            .withProjectileDestructionAudioClip()
            .build();
   }
}

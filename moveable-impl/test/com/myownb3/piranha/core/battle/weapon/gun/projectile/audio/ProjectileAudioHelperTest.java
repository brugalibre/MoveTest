package com.myownb3.piranha.core.battle.weapon.gun.projectile.audio;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

class ProjectileAudioHelperTest {

   @Test
   void testPlayOnCollisionAudio_UnknownProjectileType() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.NONE, false);

      // When
      Executable executable = () -> new ProjectileAudioHelper().playOnCollisionAudio(projectile, Collections.emptyList());

      // Then
      assertThrows(IllegalStateException.class, executable);
   }

   @Test
   void testPlayOnCollisionAudio_ProjectileTypeLaserBeam() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.LASER_BEAM, false);
      ProjectileAudioHelper projectileAudioHelper = spy(new ProjectileAudioHelper());
      List<GridElement> gridElementsCollidedWith = Arrays.asList(mock(GridElement.class), mock(GridElement.class));

      // When
      projectileAudioHelper.playOnCollisionAudio(projectile, gridElementsCollidedWith);

      // Then
      verify(projectileAudioHelper, times(gridElementsCollidedWith.size())).buildAndPlayAudio(eq(AudioConstants.BULLET_INPACT_SOUND));
   }

   @Test
   void testPlayOnCollisionAudio_ProjectileTypeBullet() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.BULLET, true);
      ProjectileAudioHelper projectileAudioHelper = spy(new ProjectileAudioHelper());
      List<GridElement> gridElementsCollidedWith = Arrays.asList(mock(GridElement.class));

      // When
      projectileAudioHelper.playOnCollisionAudio(projectile, gridElementsCollidedWith);

      // Then
      verify(projectileAudioHelper).buildAndPlayAudio(eq(AudioConstants.MISSILE_INPACT_SOUND));
   }

   @Test
   void testPlayOnCollisionAudio_ProjectileTypeMissile() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.MISSILE, false);
      ProjectileAudioHelper projectileAudioHelper = spy(new ProjectileAudioHelper());

      // When
      projectileAudioHelper.playOnCollisionAudio(projectile, Collections.emptyList());

      // Then
      verify(projectileAudioHelper).buildAndPlayAudio(eq(AudioConstants.MISSILE_INPACT_SOUND));
   }

   @Test
   void testPlayOnCollisionAudio_ProjectileTypeMissile_Destroyed() {

      // Given
      Projectile projectile = mockProjectile(ProjectileTypes.MISSILE, true);
      ProjectileAudioHelper projectileAudioHelper = spy(new ProjectileAudioHelper());

      // When
      projectileAudioHelper.playOnCollisionAudio(projectile, Collections.emptyList());

      // Then
      verify(projectileAudioHelper).playDefaultExplosion();
   }

   private Projectile mockProjectile(ProjectileTypes projectileTypes, boolean isDestroyed) {
      Projectile projectile = mock(Projectile.class);
      when(projectile.getProjectileType()).thenReturn(projectileTypes);
      when(projectile.isDestroyed()).thenReturn(isDestroyed);
      return projectile;
   }

}

package com.myownb3.piranha.core.battle.weapon.guncarriage.sound;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.core.battle.weapon.guncarriage.sound.GunCarriageAudio.GunCarriageAudioBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.state.GunCarriageStates;

class GunCarriageAudioTest {

   @Test
   void testPlayTurretEnd2TurnAudio() {

      // Given
      GunCarriageStates gunCarriageState = GunCarriageStates.END_TURNING;
      AudioClip turretBegin2TurnAudio = mock(AudioClip.class);
      AudioClip turretTurnAudio = mock(AudioClip.class);
      AudioClip turretEnd2TurnAudio = mock(AudioClip.class);
      GunCarriageAudio gunCarriageAudio = GunCarriageAudioBuilder.builder()
            .withTurretBegin2TurnAudio(turretBegin2TurnAudio)
            .withTurretEndTurnAudio(turretEnd2TurnAudio)
            .withTurretTurnAudio(turretTurnAudio)
            .build();

      // When
      gunCarriageAudio.playGunCarriageAudio(gunCarriageState);

      // Then
      verify(turretEnd2TurnAudio).play();
      verify(turretTurnAudio, never()).play();
      verify(turretBegin2TurnAudio, never()).play();
   }

   @Test
   void testPlayTurretBegin2TurnAudio() {

      // Given
      GunCarriageStates gunCarriageState = GunCarriageStates.START_TURNING;
      AudioClip turretBegin2TurnAudio = mock(AudioClip.class);
      AudioClip turretTurnAudio = mock(AudioClip.class);
      AudioClip turretEnd2TurnAudio = mock(AudioClip.class);
      GunCarriageAudio gunCarriageAudio = GunCarriageAudioBuilder.builder()
            .withTurretBegin2TurnAudio(turretBegin2TurnAudio)
            .withTurretEndTurnAudio(turretEnd2TurnAudio)
            .withTurretTurnAudio(turretTurnAudio)
            .build();

      // When
      gunCarriageAudio.playGunCarriageAudio(gunCarriageState);

      // Then
      verify(turretBegin2TurnAudio).play();
      verify(turretTurnAudio, never()).play();
      verify(turretEnd2TurnAudio, never()).play();
   }

   @Test
   void testPlayTurretTurnAudio() {

      // Given
      GunCarriageStates gunCarriageState = GunCarriageStates.TURNING;
      AudioClip turretBegin2TurnAudio = mock(AudioClip.class);
      AudioClip turretTurnAudio = mock(AudioClip.class);
      AudioClip turretEnd2TurnAudio = mock(AudioClip.class);
      GunCarriageAudio gunCarriageAudio = GunCarriageAudioBuilder.builder()
            .withTurretBegin2TurnAudio(turretBegin2TurnAudio)
            .withTurretEndTurnAudio(turretEnd2TurnAudio)
            .withTurretTurnAudio(turretTurnAudio)
            .build();

      // When
      gunCarriageAudio.playGunCarriageAudio(gunCarriageState);

      // Then
      verify(turretBegin2TurnAudio, never()).play();
      verify(turretTurnAudio).play();
      verify(turretEnd2TurnAudio, never()).play();
   }

   @Test
   void testPlayTurretNoAudioAtAll() {

      // Given
      GunCarriageStates gunCarriageState = GunCarriageStates.IDLE;
      AudioClip turretBegin2TurnAudio = mock(AudioClip.class);
      AudioClip turretTurnAudio = mock(AudioClip.class);
      AudioClip turretEnd2TurnAudio = mock(AudioClip.class);
      GunCarriageAudio gunCarriageAudio = GunCarriageAudioBuilder.builder()
            .withTurretBegin2TurnAudio(turretBegin2TurnAudio)
            .withTurretEndTurnAudio(turretEnd2TurnAudio)
            .withTurretTurnAudio(turretTurnAudio)
            .build();

      // When
      gunCarriageAudio.playGunCarriageAudio(gunCarriageState);

      // Then
      verify(turretBegin2TurnAudio, never()).play();
      verify(turretTurnAudio, never()).play();
      verify(turretEnd2TurnAudio, never()).play();
   }

}

package com.myownb3.piranha.core.moveables.engine.audio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.core.moveables.engine.EngineStates;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio.EngineAudioBuilder;

class EngineAudioTest {

   @Test
   void testPlayEngineIdleAudio() {

      // Given
      AudioClip engineIdleAudio = mock(AudioClip.class);
      AudioClip engineAccelerateAudio = mock(AudioClip.class);
      AudioClip engineMoveAudio = mock(AudioClip.class);
      AudioClip engineSlowDownAudio = mock(AudioClip.class);
      EngineAudio engineAudio = EngineAudioBuilder.builder()
            .withEngineAccelerateTurnAudio(engineAccelerateAudio)
            .withEngineIdleAudio(engineIdleAudio)
            .withEngineMoveAudio(engineMoveAudio)
            .withEngineSlowDownAudio(engineSlowDownAudio)
            .build();

      // When
      engineAudio.playEngineAudio(EngineStates.IDLE);

      // Then
      verify(engineIdleAudio).play();
      verify(engineAccelerateAudio, never()).play();
      verify(engineMoveAudio, never()).play();
      verify(engineSlowDownAudio, never()).play();
   }

   @Test
   void testPlayEngineMovingForwardsAudio() {

      // Given
      AudioClip engineIdleAudio = mock(AudioClip.class);
      AudioClip engineAccelerateAudio = mock(AudioClip.class);
      AudioClip engineMoveAudio = mock(AudioClip.class);
      AudioClip engineSlowDownAudio = mock(AudioClip.class);
      EngineAudio engineAudio = EngineAudioBuilder.builder()
            .withEngineAccelerateTurnAudio(engineAccelerateAudio)
            .withEngineIdleAudio(engineIdleAudio)
            .withEngineMoveAudio(engineMoveAudio)
            .withEngineSlowDownAudio(engineSlowDownAudio)
            .build();

      // When
      engineAudio.playEngineAudio(EngineStates.MOVING_FORWARDS);

      // Then
      verify(engineIdleAudio, never()).play();
      verify(engineAccelerateAudio, never()).play();
      verify(engineMoveAudio).play();
      verify(engineSlowDownAudio, never()).play();
   }

   @Test
   void testPlayEngineMovingBackwardsAudio() {

      // Given
      AudioClip engineIdleAudio = mock(AudioClip.class);
      AudioClip engineAccelerateAudio = mock(AudioClip.class);
      AudioClip engineMoveAudio = mock(AudioClip.class);
      AudioClip engineSlowDownAudio = mock(AudioClip.class);
      EngineAudio engineAudio = EngineAudioBuilder.builder()
            .withEngineAccelerateTurnAudio(engineAccelerateAudio)
            .withEngineIdleAudio(engineIdleAudio)
            .withEngineMoveAudio(engineMoveAudio)
            .withEngineSlowDownAudio(engineSlowDownAudio)
            .build();

      // When
      engineAudio.playEngineAudio(EngineStates.MOVING_BACKWARDS);

      // Then
      verify(engineIdleAudio, never()).play();
      verify(engineAccelerateAudio, never()).play();
      verify(engineMoveAudio).play();
      verify(engineSlowDownAudio, never()).play();
   }

   @Test
   void testPlayEngineAcceleratingAudio() {

      // Given
      AudioClip engineIdleAudio = mock(AudioClip.class);
      AudioClip engineAccelerateAudio = mock(AudioClip.class);
      AudioClip engineMoveAudio = mock(AudioClip.class);
      AudioClip engineSlowDownAudio = mock(AudioClip.class);
      EngineAudio engineAudio = EngineAudioBuilder.builder()
            .withEngineAccelerateTurnAudio(engineAccelerateAudio)
            .withEngineIdleAudio(engineIdleAudio)
            .withEngineMoveAudio(engineMoveAudio)
            .withEngineSlowDownAudio(engineSlowDownAudio)
            .build();

      // When
      engineAudio.playEngineAudio(EngineStates.ACCELERATING);

      // Then
      verify(engineIdleAudio, never()).play();
      verify(engineAccelerateAudio).play();
      verify(engineMoveAudio, never()).play();
      verify(engineSlowDownAudio, never()).play();
   }

   @Test
   void testPlayEngineSlowingDownAudio() {

      // Given
      AudioClip engineIdleAudio = mock(AudioClip.class);
      AudioClip engineAccelerateAudio = mock(AudioClip.class);
      AudioClip engineMoveAudio = mock(AudioClip.class);
      AudioClip engineSlowDownAudio = mock(AudioClip.class);
      EngineAudio engineAudio = EngineAudioBuilder.builder()
            .withEngineAccelerateTurnAudio(engineAccelerateAudio)
            .withEngineIdleAudio(engineIdleAudio)
            .withEngineMoveAudio(engineMoveAudio)
            .withEngineSlowDownAudio(engineSlowDownAudio)
            .build();

      // When
      engineAudio.playEngineAudio(EngineStates.SLOWINGDOWN);

      // Then
      verify(engineIdleAudio, never()).play();
      verify(engineAccelerateAudio, never()).play();
      verify(engineMoveAudio, never()).play();
      verify(engineSlowDownAudio).play();
   }

   @Test
   void testPlayNoEngineAudio() {

      // Given
      AudioClip engineIdleAudio = mock(AudioClip.class);
      AudioClip engineAccelerateAudio = mock(AudioClip.class);
      AudioClip engineMoveAudio = mock(AudioClip.class);
      AudioClip engineSlowDownAudio = mock(AudioClip.class);
      EngineAudio engineAudio = EngineAudioBuilder.builder()
            .withEngineAccelerateTurnAudio(engineAccelerateAudio)
            .withEngineIdleAudio(engineIdleAudio)
            .withEngineMoveAudio(engineMoveAudio)
            .withEngineSlowDownAudio(engineSlowDownAudio)
            .build();

      // When
      engineAudio.playEngineAudio(EngineStates.NONE);

      // Then
      verify(engineIdleAudio, never()).play();
      verify(engineAccelerateAudio, never()).play();
      verify(engineMoveAudio, never()).play();
      verify(engineSlowDownAudio, never()).play();
   }
}

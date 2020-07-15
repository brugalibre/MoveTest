package com.myownb3.piranha.audio.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.sound.sampled.Clip;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.audio.impl.exception.AudioClipException;

class AudioClipImplTest {

   @Test
   void testBuildAndPlay() {

      // Given
      AudioClipImpl audioClipImpl = AudioClipBuilder.builder()
            .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
            .build();

      // When
      audioClipImpl.play();

      // Then
      audioClipImpl.stop();
   }

   @Test
   void testDontStartClipSinceItsStillRunning() {

      // Given
      Clip clip = mock(Clip.class);
      AudioClipImpl audioClipImpl = AudioClipBuilder.builder()
            .withRestartRunningAudio(false)
            .withClip(clip)
            .build();

      // When
      audioClipImpl.play();
      when(clip.isActive()).thenReturn(true);
      audioClipImpl.play();

      // Then
      verify(clip).start();
   }

   @Test
   void testStartClip() {

      // Given
      Clip clip = mock(Clip.class);
      AudioClipImpl audioClipImpl = spy(AudioClipBuilder.builder()
            .withRestartRunningAudio(true)
            .withClip(clip)
            .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
            .build());

      // When
      audioClipImpl.play();
      when(clip.isActive()).thenReturn(true);
      audioClipImpl.play();

      // Then
      verify(clip, times(2)).start();
   }

   @Test
   void testBuildWithUnsupportedFileType() {

      // Given
      String audioResource = "audio/test.mp3";// It must be an mp3-file since this is not supported

      // When
      Executable ex = () -> AudioClipBuilder.builder()
            .withRestartRunningAudio(true)
            .withAudioResource(audioResource)
            .build();

      // Then
      assertThrows(AudioClipException.class, ex);
   }
}

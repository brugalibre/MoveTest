package com.myownb3.piranha.audio.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.sound.sampled.Clip;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.worker.WorkerThreadFactory;

class AudioClipCloserTest {

   @AfterEach
   public void cleanUp() {
      WorkerThreadFactory.INSTANCE.shutdown();
      AudioClipCloser.INSTANCE.stop();
   }

   @BeforeEach
   public void setUp() {
      WorkerThreadFactory.INSTANCE.restart();
      AudioClipCloser.INSTANCE.stop();
   }

   @Test
   void testAddAndCheckCloseableAudioClipWithNPE() throws InterruptedException {

      // Given
      Clip clip = mock(Clip.class);
      AudioClip audioClip = spy(AudioClipBuilder.builder()
            .withClip(clip)
            .build());
      doThrow(NullPointerException.class).when(audioClip).isCloseable();

      // When
      AudioClipCloser.INSTANCE.registerAudioClip(audioClip);
      AudioClipCloser.INSTANCE.start();
      Thread.sleep(AudioClipCloser.CLEANUP_INTERVALL);

      // Then
      verify(audioClip, never()).close(); // probably a nonsen verify, we would never know if there was an exception within the audioclipcloser
   }

   @Test
   void testAddAndCheckNonCloseableAudioClip() throws InterruptedException {

      // Given
      Clip clip = mock(Clip.class);
      AudioClip audioClip = spy(AudioClipBuilder.builder()
            .withClip(clip)
            .build());

      // When
      AudioClipCloser.INSTANCE.registerAudioClip(audioClip);
      AudioClipCloser.INSTANCE.start();
      Thread.sleep(AudioClipCloser.CLEANUP_INTERVALL);

      // Then
      verify(audioClip, never()).close();
   }

   @Test
   void testAddAndCheckWithCloseableAudioClip() throws InterruptedException {

      // Given
      Clip clip = mockClip(false);
      AudioClip audioClip = spy(AudioClipBuilder.builder()
            .withClip(clip)
            .withIsCloseableSupplier(() -> true)
            .build());

      // When
      AudioClipCloser.INSTANCE.registerAudioClip(audioClip);
      AudioClipCloser.INSTANCE.start();
      Thread.sleep(100);

      // Then
      verify(audioClip).close();
   }

   @Test
   void testStopAudioClipCloser() throws InterruptedException, ExecutionException {

      // Given

      // When
      Future<Optional<Void>> futurVoidOptional = WorkerThreadFactory.INSTANCE.executeAsync(AudioClipCloser.INSTANCE);
      Thread.sleep(AudioClipCloser.CLEANUP_INTERVALL * 2l);
      AudioClipCloser.INSTANCE.stop();

      // Then
      assertThat(futurVoidOptional.get().isPresent(), is(false));
   }

   @Test
   void testAddAndCheckWithCloseableAndActiveAudioClip() throws InterruptedException {

      // Given
      Clip clip = mockClip(true);
      AudioClip audioClip = spy(AudioClipBuilder.builder()
            .withClip(clip)
            .withIsCloseableSupplier(() -> true)
            .build());

      // When
      AudioClipCloser.INSTANCE.registerAudioClip(audioClip);
      AudioClipCloser.INSTANCE.start();
      Thread.sleep(AudioClipCloser.CLEANUP_INTERVALL);

      // Then
      verify(audioClip, never()).close();
   }

   private Clip mockClip(boolean value) {
      Clip clip = mock(Clip.class);
      when(clip.isActive()).thenReturn(value);
      return clip;
   }
}

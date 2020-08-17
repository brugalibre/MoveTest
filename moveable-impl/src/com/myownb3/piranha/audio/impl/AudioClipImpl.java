package com.myownb3.piranha.audio.impl;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.util.function.BooleanSupplier;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.audio.impl.exception.AudioClipException;

public class AudioClipImpl implements AudioClip {

   private String audioResource;
   private boolean restartRunningAudio;
   private Clip clip;
   private BooleanSupplier isCloseableSupplier;

   private AudioClipImpl(String audioResource, boolean restartRunningAudio, BooleanSupplier isCloseableSupplier) {
      this.audioResource = audioResource;
      this.restartRunningAudio = restartRunningAudio;
      this.clip = getInitialClip();
      this.isCloseableSupplier = isCloseableSupplier;
   }

   private AudioClipImpl(Clip clip, String audioResource, boolean restartRunningAudio, BooleanSupplier isCloseableSupplier) {
      this.restartRunningAudio = restartRunningAudio;
      this.audioResource = audioResource;
      this.clip = clip;
      this.isCloseableSupplier = isCloseableSupplier;
   }

   private Clip getInitialClip() {
      try {
         return getAndPrepareClip();
      } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
         throw new AudioClipException(e);
      }
   }

   @Override
   public void stop() {
      clip.stop();
   }

   @Override
   public void play() {
      if (!clip.isActive() || restartRunningAudio) {
         clip.setFramePosition(0);
         clip.start();
         AudioClipCloser.INSTANCE.registerAudioClip(this);
      }
   }

   private Clip getAndPrepareClip() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
      Clip newClip = AudioSystem.getClip();
      newClip.open(getAudioInputStream(audioResource));
      return newClip;
   }

   private AudioInputStream getAudioInputStream(String audioRessource) throws UnsupportedAudioFileException, IOException {
      return AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(audioRessource));
   }

   @Override
   public boolean isCloseable() {
      System.err.println("Is Closeable '" + isCloseableSupplier.getAsBoolean() + "' is active:'" + clip.isActive() + "'");
      return isCloseableSupplier.getAsBoolean() && !clip.isActive();
   }

   @Override
   public void close() {
      clip.close();
   }

   public static class AudioClipBuilder {

      private String audioResource;
      private boolean restartRunningAudio;
      private Clip clip;
      private BooleanSupplier isCloseableSupplier;

      private AudioClipBuilder() {
         restartRunningAudio = true;
         isCloseableSupplier = () -> false;
      }

      public AudioClipBuilder withAudioResource(String audioResource) {
         this.audioResource = audioResource;
         return this;
      }

      public AudioClipBuilder withRestartRunningAudio(boolean restartRunningAudio) {
         this.restartRunningAudio = restartRunningAudio;
         return this;
      }

      public AudioClipBuilder withClip(Clip clip) {
         this.clip = clip;
         return this;
      }

      public AudioClipBuilder withIsCloseableSupplier(BooleanSupplier isCloseableSupplier) {
         this.isCloseableSupplier = isCloseableSupplier;
         return this;
      }

      public AudioClipImpl build() {
         if (nonNull(clip)) {
            return new AudioClipImpl(clip, audioResource, restartRunningAudio, isCloseableSupplier);
         }
         return new AudioClipImpl(audioResource, restartRunningAudio, isCloseableSupplier);
      }

      public static AudioClipBuilder builder() {
         return new AudioClipBuilder();
      }

   }
}

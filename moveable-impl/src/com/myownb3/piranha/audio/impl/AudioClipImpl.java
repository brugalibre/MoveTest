package com.myownb3.piranha.audio.impl;

import static java.util.Objects.nonNull;

import java.io.IOException;

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

   private AudioClipImpl(String audioResource, boolean restartRunningAudio) {
      this.audioResource = audioResource;
      this.restartRunningAudio = restartRunningAudio;
      this.clip = getInitialClip();
   }

   private AudioClipImpl(Clip clip, String audioResource, boolean restartRunningAudio) {
      this.restartRunningAudio = restartRunningAudio;
      this.audioResource = audioResource;
      this.clip = clip;
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

   public static class AudioClipBuilder {

      private String audioResource;
      private boolean restartRunningAudio;
      private Clip clip;

      private AudioClipBuilder() {
         restartRunningAudio = true;
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

      public AudioClipImpl build() {
         if (nonNull(clip)) {
            return new AudioClipImpl(clip, audioResource, restartRunningAudio);
         }
         return new AudioClipImpl(audioResource, restartRunningAudio);
      }

      public static AudioClipBuilder builder() {
         return new AudioClipBuilder();
      }

   }
}

package com.myownb3.piranha.core.moveables.engine.audio;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.moveables.engine.EngineStates;

public class EngineAudio {
   private AudioClip engineIdleAudio;
   private AudioClip engineAccelerateAudio;
   private AudioClip engineSlowDownAudio;
   private AudioClip engineMoveAudio;

   private EngineAudio(AudioClip engineIdleAudio, AudioClip engineAccelerateAudio, AudioClip engineSlowDownAudio, AudioClip engineMoveAudio) {
      this.engineIdleAudio = engineIdleAudio;
      this.engineAccelerateAudio = engineAccelerateAudio;
      this.engineSlowDownAudio = engineSlowDownAudio;
      this.engineMoveAudio = engineMoveAudio;
   }

   /**
    * Plays the audio of a {@link Engine} according it's current {@link EngineStates}
    * 
    * @param engineState
    */
   public void playEngineAudio(EngineStates engineState) {
      switch (engineState) {
         case IDLE:
            engineIdleAudio.play();
            engineAccelerateAudio.stop();
            engineMoveAudio.stop();
            break;
         case ACCELERATING:
            engineAccelerateAudio.play();
            engineIdleAudio.stop();
            engineSlowDownAudio.stop();
            engineMoveAudio.stop();
            break;
         case SLOWINGDOWN_NATURALLY:
         case SLOWINGDOWN:
            engineSlowDownAudio.play();
            engineIdleAudio.stop();
            engineAccelerateAudio.stop();
            engineMoveAudio.stop();
            break;
         case MOVING_BACKWARDS: // Fall through
         case MOVING_FORWARDS:
            engineMoveAudio.play();
            engineIdleAudio.stop();
            engineAccelerateAudio.stop();
            engineSlowDownAudio.stop();
            break;
         default:
            // Do nothing here
            break;
      }
   }

   public static class EngineAudioBuilder {
      private AudioClip engineIdleAudio;
      private AudioClip engineAccelerateAudio;
      private AudioClip engineSlowDownAudio;
      private AudioClip engineMoveAudio;

      private EngineAudioBuilder() {
         // private 
      }

      public EngineAudioBuilder withEngineIdleAudio(AudioClip engineIdleAudio) {
         this.engineIdleAudio = engineIdleAudio;
         return this;
      }

      public EngineAudioBuilder withEngineAccelerateTurnAudio(AudioClip engineAccelerateAudio) {
         this.engineAccelerateAudio = engineAccelerateAudio;
         return this;
      }

      public EngineAudioBuilder withEngineSlowDownAudio(AudioClip engineSlowDownAudio) {
         this.engineSlowDownAudio = engineSlowDownAudio;
         return this;
      }

      public EngineAudioBuilder withEngineMoveAudio(AudioClip engineMoveAudio) {
         this.engineMoveAudio = engineMoveAudio;
         return this;
      }

      public EngineAudioBuilder withDefaultAudio() {
         this.engineIdleAudio = AudioClipBuilder.builder()
               .withAudioResource(AudioConstants.ENGINE_IDLE)
               .withRestartRunningAudio(false)
               .build();
         this.engineAccelerateAudio = AudioClipBuilder.builder()
               .withRestartRunningAudio(false)
               .withAudioResource(AudioConstants.ENGINE_ACCELERATING)
               .build();
         this.engineSlowDownAudio = AudioClipBuilder.builder()
               .withRestartRunningAudio(false)
               .withAudioResource(AudioConstants.ENGINE_SLOWING_DOWN)
               .build();
         this.engineMoveAudio = AudioClipBuilder.builder()
               .withRestartRunningAudio(false)
               .withAudioResource(AudioConstants.ENGINE_MOVING)
               .build();
         return this;
      }

      public EngineAudio build() {
         return new EngineAudio(engineIdleAudio, engineAccelerateAudio, engineSlowDownAudio, engineMoveAudio);
      }

      public static EngineAudioBuilder builder() {
         return new EngineAudioBuilder();
      }
   }
}

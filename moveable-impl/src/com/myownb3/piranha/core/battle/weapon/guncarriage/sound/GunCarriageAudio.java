package com.myownb3.piranha.core.battle.weapon.guncarriage.sound;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.guncarriage.state.GunCarriageStates;

public class GunCarriageAudio {

   private AudioClip turretBegin2TurnAudio;
   private AudioClip turretEndTurnAudio;
   private AudioClip turretTurnAudio;

   private GunCarriageAudio(AudioClip turretBegin2TurnAudio, AudioClip turretEndTurnAudio, AudioClip turretTurnAudio) {
      this.turretBegin2TurnAudio = turretBegin2TurnAudio;
      this.turretEndTurnAudio = turretEndTurnAudio;
      this.turretTurnAudio = turretTurnAudio;
   }

   /**
    * Plays the audio of a {@link GunCarriage} according it's current {@link GunCarriageStates}
    * 
    * @param gunCarriageState
    */
   public void playGunCarriageAudio(GunCarriageStates gunCarriageState) {
      switch (gunCarriageState) {
         case START_TURNING:
            turretBegin2TurnAudio.play();
            break;
         case END_TURNING:
            turretEndTurnAudio.play();
            break;
         case TURNING:
            turretTurnAudio.play();
            break;
         default:
            // Do nothing here
            break;
      }
   }

   public static class GunCarriageAudioBuilder {
      private AudioClip turretBegin2TurnAudio;
      private AudioClip turretEndTurnAudio;
      private AudioClip turretTurnAudio;

      private GunCarriageAudioBuilder() {
         // private 
      }

      public GunCarriageAudioBuilder withTurretBegin2TurnAudio(AudioClip turretBegin2TurnAudio) {
         this.turretBegin2TurnAudio = turretBegin2TurnAudio;
         return this;
      }

      public GunCarriageAudioBuilder withTurretEndTurnAudio(AudioClip turretEndTurnAudio) {
         this.turretEndTurnAudio = turretEndTurnAudio;
         return this;
      }

      public GunCarriageAudioBuilder withTurretTurnAudio(AudioClip turretTurnAudio) {
         this.turretTurnAudio = turretTurnAudio;
         return this;
      }

      public GunCarriageAudioBuilder withAudio() {
         this.turretTurnAudio = AudioClipBuilder.builder()
               .withAudioResource(AudioConstants.TURRET_ROTATE)
               .withRestartRunningAudio(false)
               .build();
         this.turretBegin2TurnAudio = AudioClipBuilder.builder()
               .withAudioResource(AudioConstants.TURRET_START_ROTATE)
               .build();
         this.turretEndTurnAudio = AudioClipBuilder.builder()
               .withAudioResource(AudioConstants.TURRET_END_ROTATE)
               .build();
         return this;
      }

      public GunCarriageAudio build() {
         return new GunCarriageAudio(turretBegin2TurnAudio, turretEndTurnAudio, turretTurnAudio);
      }

      public static GunCarriageAudioBuilder builder() {
         return new GunCarriageAudioBuilder();
      }
   }
}

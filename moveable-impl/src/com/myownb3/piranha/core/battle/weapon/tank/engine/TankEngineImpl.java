package com.myownb3.piranha.core.battle.weapon.tank.engine;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;

public class TankEngineImpl implements TankEngine {

   private MoveableController moveableController;
   private Optional<AudioClip> audioClipOpt;

   private TankEngineImpl(MoveableController moveableController, AudioClip audioClip) {
      this.moveableController = requireNonNull(moveableController);
      this.audioClipOpt = Optional.ofNullable(audioClip);
   }

   @Override
   public void moveForward() {
      moveableController.leadMoveable();
      audioClipOpt.ifPresent(AudioClip::play);
   }

   @Override
   public EndPointMoveable getMoveable() {
      return moveableController.getMoveable();
   }

   public static class TankEngineBuilder {

      private MoveableController moveableController;
      private AudioClip audioClip;

      private TankEngineBuilder() {
         // private
      }

      public TankEngineBuilder withMoveableController(MoveableController moveableController) {
         this.moveableController = moveableController;
         return this;
      }

      public TankEngineBuilder withAudioClip(AudioClip audioClip) {
         this.audioClip = audioClip;
         return this;
      }

      public TankEngineImpl build() {
         return new TankEngineImpl(moveableController, audioClip);
      }

      public static TankEngineBuilder builder() {
         return new TankEngineBuilder();
      }

   }
}

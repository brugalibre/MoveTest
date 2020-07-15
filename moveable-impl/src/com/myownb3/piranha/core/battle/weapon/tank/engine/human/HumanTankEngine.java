package com.myownb3.piranha.core.battle.weapon.tank.engine.human;

import java.util.Optional;
import java.util.function.Supplier;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.moveables.EndPointMoveable;

public class HumanTankEngine implements TankEngine, HumanToTankInteractionCallbackHandler {

   private Supplier<EndPointMoveable> endPointMoveableSupplier;
   private boolean moveForward;
   private boolean moveBackward;
   private boolean turnLeft;
   private boolean turnRight;
   private Optional<AudioClip> audioClipOpt;
   @Visible4Testing
   double turnAngle = 5.0;

   private HumanTankEngine(Supplier<EndPointMoveable> endPointMoveableSupplier, AudioClip audioClip) {
      this.endPointMoveableSupplier = endPointMoveableSupplier;
      this.audioClipOpt = Optional.ofNullable(audioClip);
   }

   @Override
   public void moveForward() {
      if (moveForward) {
         getMoveable().moveForward();
      } else if (moveBackward) {
         getMoveable().moveBackward();
      }

      if (turnLeft) {
         getMoveable().makeTurn(-turnAngle);
      } else if (turnRight) {
         getMoveable().makeTurn(turnAngle);
      }
      audioClipOpt.ifPresent(AudioClip::play);
   }

   @Override
   public EndPointMoveable getMoveable() {
      return endPointMoveableSupplier.get();
   }

   @Override
   public void onForward(boolean isPressed) {
      moveForward = isPressed;
      moveBackward = false;
   }

   @Override
   public void onBackward(boolean isPressed) {
      moveForward = false;
      moveBackward = isPressed;
   }

   @Override
   public void onTurnRight(boolean isPressed) {
      turnLeft = false;
      turnRight = isPressed;
   }

   @Override
   public void onTurnLeft(boolean isPressed) {
      turnLeft = isPressed;
      turnRight = false;
   }

   public static class HumanTankEngineBuilder {
      private Supplier<EndPointMoveable> endPointMoveableSupplier;
      private AudioClip audioClip;

      private HumanTankEngineBuilder() {
         // private 
      }

      public static HumanTankEngineBuilder builder() {
         return new HumanTankEngineBuilder();
      }

      public HumanTankEngineBuilder withLazyMoveable(Supplier<EndPointMoveable> endPointMoveableSupplier) {
         this.endPointMoveableSupplier = endPointMoveableSupplier;
         return this;
      }

      public HumanTankEngineBuilder withAudioClip(AudioClip audioClip) {
         this.audioClip = audioClip;
         return this;
      }

      public HumanTankEngine build() {
         return new HumanTankEngine(endPointMoveableSupplier, audioClip);
      }
   }
}

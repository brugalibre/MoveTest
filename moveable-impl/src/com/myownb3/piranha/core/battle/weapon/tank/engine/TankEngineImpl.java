package com.myownb3.piranha.core.battle.weapon.tank.engine;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.EngineStates;
import com.myownb3.piranha.core.moveables.engine.MovingDirections;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio;

public class TankEngineImpl implements TankEngine {

   private MoveableController moveableController;
   private Optional<EngineAudio> engineAudioOpt;
   private EngineStateHandler engineStateHandler;
   private EngineStates engineState;

   private TankEngineImpl(MoveableController moveableController, EngineStateHandler engineStateHandler, EngineAudio engineAudio) {
      this.moveableController = requireNonNull(moveableController);
      this.engineAudioOpt = Optional.ofNullable(engineAudio);
      this.engineStateHandler = engineStateHandler;
      this.engineState = EngineStates.IDLE;
   }

   @Override
   public void moveForward() {
      moveableController.leadMoveable();
      engineState = engineStateHandler.handleEngineState(MovingDirections.FORWARDS, engineState);
      engineAudioOpt.ifPresent(engineAudio -> engineAudio.playEngineAudio(engineState));
   }

   @Override
   public void stopMoveForward() {
      engineState = engineStateHandler.handleEngineState(MovingDirections.NONE, engineState);
      engineAudioOpt.ifPresent(engineAudio -> engineAudio.playEngineAudio(engineState));
   }

   @Override
   public EndPointMoveable getMoveable() {
      return moveableController.getMoveable();
   }

   public static class TankEngineBuilder {

      private MoveableController moveableController;
      private EngineAudio engineAudio;
      private int velocity;
      private EngineStateHandler engineStateHandler;

      private TankEngineBuilder() {
         // private
      }

      public TankEngineBuilder withMoveableController(MoveableController moveableController) {
         this.moveableController = moveableController;
         return this;
      }

      public TankEngineBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public TankEngineBuilder withEngineStateHandler(EngineStateHandler engineStateHandler) {
         this.engineStateHandler = engineStateHandler;
         return this;
      }

      public TankEngineBuilder withDefaultEngineStateHandler() {
         int acceleratingSpeed = 1800;
         double manuallySlowDownSpeed = 200;
         double naturallySlowDownSpeed = 900;

         this.engineStateHandler = new EngineStateHandler(EngineAcceleratorBuilder.builder()
               .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                     .addGear(GearBuilder.builder()
                           .withAccelerationSpeed(acceleratingSpeed)
                           .withMaxVelocity(velocity / 2)
                           .withNumber(1)
                           .buil())
                     .addGear(GearBuilder.builder()
                           .withAccelerationSpeed(acceleratingSpeed)
                           .withMaxVelocity(velocity)
                           .withNumber(2)
                           .buil())
                     .build())
               .withManuallySlowDownSpeed(manuallySlowDownSpeed)
               .withNaturallySlowDownSpeed(naturallySlowDownSpeed)
               .build());
         return this;
      }

      public TankEngineBuilder withEngineAudio(EngineAudio engineAudio) {
         this.engineAudio = engineAudio;
         return this;
      }

      public TankEngineImpl build() {
         requireNonNull(moveableController);
         requireNonNull(engineStateHandler);
         return new TankEngineImpl(moveableController, engineStateHandler, engineAudio);
      }

      public static TankEngineBuilder builder() {
         return new TankEngineBuilder();
      }

   }
}

package com.myownb3.piranha.core.battle.weapon.tank.engine.human;

import static com.myownb3.piranha.core.moveables.engine.MovingDirections.BACKWARDS;
import static com.myownb3.piranha.core.moveables.engine.MovingDirections.FORWARDS;
import static com.myownb3.piranha.core.moveables.engine.MovingDirections.NONE;
import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Supplier;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.EngineStates;
import com.myownb3.piranha.core.moveables.engine.MovingDirections;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio;

public class HumanTankEngine implements TankEngine, HumanToTankInteractionCallbackHandler {

   private Supplier<EndPointMoveable> endPointMoveableSupplier;
   // User input variables
   private boolean isForwardsPressed;
   private boolean isBackwardsPressed;
   private boolean isTurnLeftPressed;
   private boolean isTurnRightPressed;

   private Optional<EngineAudio> engineAudioOpt;
   private EngineStates engineState;
   private EngineStateHandler engineStateHandler;
   private MovingDirections movingDirection;

   @Visible4Testing
   double turnAngle = 5.0;

   private HumanTankEngine(Supplier<EndPointMoveable> endPointMoveableSupplier, EngineStateHandler engineStateHandler, EngineAudio engineAudio) {
      this.endPointMoveableSupplier = endPointMoveableSupplier;
      this.engineAudioOpt = Optional.ofNullable(engineAudio);
      this.engineState = EngineStates.IDLE;
      this.movingDirection = MovingDirections.NONE;
      this.engineStateHandler = engineStateHandler;
   }

   @Override
   public void moveForward() {
      if (movingDirection == FORWARDS) {
         getMoveable().moveForward();
      } else if (movingDirection == BACKWARDS) {
         getMoveable().moveBackward();
      }
      if (isTurnLeftPressed) {
         getMoveable().makeTurn(-turnAngle);
      } else if (isTurnRightPressed) {
         getMoveable().makeTurn(turnAngle);
      }
      getMoveable().setVelocity(engineStateHandler.getCurrentVelocity());
      handleAndSetEngineState();
      evalAndSetCurrentMovingDirection();
      playEngineAudio();
   }

   private void playEngineAudio() {
      engineAudioOpt.ifPresent(engineAudio -> engineAudio.playEngineAudio(engineState));
   }

   private void handleAndSetEngineState() {
      MovingDirections movingDirection4EngineStateHandler = getMovingDirection4EngineStateHandler();
      engineState = engineStateHandler.handleEngineState(movingDirection4EngineStateHandler, engineState);
   }

   private MovingDirections getMovingDirection4EngineStateHandler() {
      if (isForwardsPressed) {
         return FORWARDS;
      } else if (isBackwardsPressed) {
         return BACKWARDS;
      }
      return NONE;
   }

   private void evalAndSetCurrentMovingDirection() {
      this.movingDirection = evalNewMovingDirection();
   }

   @Visible4Testing
   MovingDirections evalNewMovingDirection() {
      switch (engineState) {
         case IDLE: // Fall throuh
         case ACCELERATING: // Fall throuh
         case SLOWINGDOWN: // Fall throuh
         case SLOWINGDOWN_NATURALLY:
            if (isForwardsPressed && !engineStateHandler.isEngineMovingBackward()) {
               return FORWARDS;
            } else if (isBackwardsPressed && !engineStateHandler.isEngineMovingForward()) {
               return BACKWARDS;
            }
            return engineStateHandler.getCurrentVelocity() > 0 ? movingDirection : NONE;
         case MOVING_BACKWARDS:
            return BACKWARDS;
         case MOVING_FORWARDS:
            return FORWARDS;
         default:
            return NONE;
      }
   }

   @Override
   public void stopMoveForward() {
      engineState = engineStateHandler.handleEngineState(MovingDirections.NONE, engineState);
      playEngineAudio();
   }

   @Override
   public EndPointMoveable getMoveable() {
      return endPointMoveableSupplier.get();
   }

   @Override
   public void onForwardPressed(boolean isPressed) {
      isForwardsPressed = isPressed;
      isBackwardsPressed = false;
   }

   @Override
   public void onBackwardPressed(boolean isPressed) {
      isForwardsPressed = false;
      isBackwardsPressed = isPressed;
   }

   @Override
   public void onTurnRightPressed(boolean isPressed) {
      isTurnLeftPressed = false;
      isTurnRightPressed = isPressed;
   }

   @Override
   public void onTurnLeftPressed(boolean isPressed) {
      isTurnLeftPressed = isPressed;
      isTurnRightPressed = false;
   }

   public static class HumanTankEngineBuilder {
      private Supplier<EndPointMoveable> endPointMoveableSupplier;
      private EngineAudio engineAudio;
      private int velocity;
      private EngineStateHandler engineStateHandler;

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

      public HumanTankEngineBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public HumanTankEngineBuilder withEngineStateHandler(EngineStateHandler engineStateHandler) {
         this.engineStateHandler = engineStateHandler;
         return this;
      }

      public HumanTankEngineBuilder withDefaultEngineStateHandler() {
         int accelerationSpeed = 1300;
         double manuallySlowDownSpeed = 200;
         double naturallySlowDownSpeed = 900;
         this.engineStateHandler = new EngineStateHandler(EngineAcceleratorBuilder.builder()
               .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                     .addGear(GearBuilder.builder()
                           .withAccelerationSpeed(accelerationSpeed)
                           .withMaxVelocity(velocity / 3)
                           .withNumber(1)
                           .buil())
                     .addGear(GearBuilder.builder()
                           .withAccelerationSpeed(accelerationSpeed)
                           .withMaxVelocity(2 * velocity / 3)
                           .withNumber(1)
                           .buil())
                     .addGear(GearBuilder.builder()
                           .withAccelerationSpeed(accelerationSpeed)
                           .withMaxVelocity(velocity)
                           .withNumber(3)
                           .buil())
                     .build())
               .withManuallySlowDownSpeed(manuallySlowDownSpeed)
               .withNaturallySlowDownSpeed(naturallySlowDownSpeed)
               .build());
         return this;
      }

      public HumanTankEngineBuilder withEngineAudio(EngineAudio engineAudio) {
         this.engineAudio = engineAudio;
         return this;
      }

      public HumanTankEngine build() {
         requireNonNull(engineStateHandler);
         requireNonNull(endPointMoveableSupplier);
         return new HumanTankEngine(endPointMoveableSupplier, engineStateHandler, engineAudio);
      }
   }
}

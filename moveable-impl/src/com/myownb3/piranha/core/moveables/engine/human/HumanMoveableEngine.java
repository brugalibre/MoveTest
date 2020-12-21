package com.myownb3.piranha.core.moveables.engine.human;

import static com.myownb3.piranha.core.moveables.engine.MovingDirections.BACKWARD;
import static com.myownb3.piranha.core.moveables.engine.MovingDirections.FORWARD;
import static com.myownb3.piranha.core.moveables.engine.MovingDirections.NONE;
import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Supplier;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.EngineStates;
import com.myownb3.piranha.core.moveables.engine.MoveableEngine;
import com.myownb3.piranha.core.moveables.engine.MovingDirections;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio;

public class HumanMoveableEngine implements MoveableEngine, HumanToEngineInteractionCallbackHandler {

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

   private HumanMoveableEngine(Supplier<EndPointMoveable> endPointMoveableSupplier, EngineStateHandler engineStateHandler, EngineAudio engineAudio) {
      this.endPointMoveableSupplier = endPointMoveableSupplier;
      this.engineAudioOpt = Optional.ofNullable(engineAudio);
      this.engineState = EngineStates.IDLE;
      this.movingDirection = MovingDirections.NONE;
      this.engineStateHandler = engineStateHandler;
   }

   @Override
   public void moveForward() {
      if (movingDirection == FORWARD) {
         getMoveable().moveForward();
      } else if (movingDirection == BACKWARD) {
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
         return FORWARD;
      } else if (isBackwardsPressed) {
         return BACKWARD;
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
               return FORWARD;
            } else if (isBackwardsPressed && !engineStateHandler.isEngineMovingForward()) {
               return BACKWARD;
            }
            return engineStateHandler.getCurrentVelocity() > 0 ? movingDirection : NONE;
         case MOVING_BACKWARDS:
            return BACKWARD;
         case MOVING_FORWARDS:
            return FORWARD;
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

   public static class HumanMoveableEngineBuilder {
      private Supplier<EndPointMoveable> endPointMoveableSupplier;
      private EngineAudio engineAudio;
      private int velocity;
      private EngineStateHandler engineStateHandler;

      private HumanMoveableEngineBuilder() {
         // private
      }

      public static HumanMoveableEngineBuilder builder() {
         return new HumanMoveableEngineBuilder();
      }

      public HumanMoveableEngineBuilder withLazyMoveable(Supplier<EndPointMoveable> endPointMoveableSupplier) {
         this.endPointMoveableSupplier = endPointMoveableSupplier;
         return this;
      }

      public HumanMoveableEngineBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public HumanMoveableEngineBuilder withEngineStateHandler(EngineStateHandler engineStateHandler) {
         this.engineStateHandler = engineStateHandler;
         return this;
      }

      public HumanMoveableEngineBuilder withDefaultEngineStateHandler() {
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
                           .withNumber(2)
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

      public HumanMoveableEngineBuilder withEngineAudio(EngineAudio engineAudio) {
         this.engineAudio = engineAudio;
         return this;
      }

      public HumanMoveableEngine build() {
         requireNonNull(engineStateHandler);
         requireNonNull(endPointMoveableSupplier);
         return new HumanMoveableEngine(endPointMoveableSupplier, engineStateHandler, engineAudio);
      }
   }
}

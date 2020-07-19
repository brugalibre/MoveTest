package com.myownb3.piranha.core.moveables.engine;

import com.myownb3.piranha.core.moveables.engine.accelerate.EngineAccelerator;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;

/**
 * 
 * The {@link EngineStateHandler} handles the current state of an engine
 * 
 * @author Dominic
 *
 */
public class EngineStateHandler {

   private EngineAccelerator engineAccelerator;
   private boolean isEngineMovingBackward = false;
   private boolean isEngineMovingForward = false;

   public EngineStateHandler() {
      this.engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withAccelerationSpeed(1)
                        .withMaxVelocity(1)
                        .withNumber(1)
                        .buil())
                  .build())
            .build();
   }

   public EngineStateHandler(EngineAccelerator engineAccelerator) {
      this.engineAccelerator = engineAccelerator;
   }

   /**
    * @return the current velocity of the engine
    */
   public int getCurrentVelocity() {
      return engineAccelerator.getCurrentVelocity();
   }

   public EngineStates handleEngineState(MovingDirections movingDirection, EngineStates engineState) {
      boolean forwards = isMovingForwards(movingDirection);
      boolean backwards = isMovingBackwards(movingDirection);
      EngineStates newEngineState;
      switch (engineState) {
         case IDLE:
            newEngineState = handleIdle(forwards || backwards);
            break;
         case ACCELERATING:
            newEngineState = handleAccelerating(forwards, backwards);
            break;
         case MOVING_FORWARDS:
            newEngineState = handleMovingForward(forwards, backwards);
            break;
         case MOVING_BACKWARDS:
            newEngineState = handleMovingBackwards(forwards, backwards);
            break;
         case SLOWINGDOWN:
            newEngineState = handleSlowingDown(forwards, backwards);
            break;
         case SLOWINGDOWN_NATURALLY:
            newEngineState = handleSlowingDownNaturally(forwards, backwards);
            break;
         default:
            throw new IllegalStateException("Unknown State '" + engineState + "'!");
      }
      setIsMovingForwardOrBackward(newEngineState, forwards, backwards);
      return newEngineState;
   }

   private void setIsMovingForwardOrBackward(EngineStates newEngineState, boolean forwards, boolean backwards) {
      switch (newEngineState) {
         case ACCELERATING:
            setIsMovingForwardOrBackward(forwards, backwards);
            break;
         case MOVING_FORWARDS:
            setIsMovingForwardOrBackward(true, false);
            break;
         case MOVING_BACKWARDS:
            setIsMovingForwardOrBackward(false, true);
            break;
         default:
            break;
      }
   }

   private void setIsMovingForwardOrBackward(boolean isEngineMovingForward, boolean isEngineMovingBackward) {
      this.isEngineMovingForward = isEngineMovingForward && engineAccelerator.getCurrentVelocity() > 0;
      this.isEngineMovingBackward = isEngineMovingBackward && engineAccelerator.getCurrentVelocity() > 0;
   }

   /**
    * @return <code>true</code> if the engine is currently moving forward. Otherwise <code>false</code>
    */
   public boolean isEngineMovingBackward() {
      return isEngineMovingBackward;
   }

   /**
    * 
    * @return <code>true</code> if the engine is currently moving backward. Otherwise <code>false</code>
    */
   public boolean isEngineMovingForward() {
      return isEngineMovingForward;
   }

   private EngineStates handleMovingBackwards(boolean forwards, boolean backwards) {
      // If we currently are moving backwards and 'backwards' is true -> continue moving backwards
      if (backwards) {
         return EngineStates.MOVING_BACKWARDS;
      }
      // Otherwise we are either slowing down manually (hiting the break) or naturally
      return forwards ? EngineStates.SLOWINGDOWN : EngineStates.SLOWINGDOWN_NATURALLY;
   }

   private EngineStates handleMovingForward(boolean forwards, boolean backwards) {
      // If we currently are moving forwards and 'forwards' is true -> continue moving forwards
      if (forwards) {
         return EngineStates.MOVING_FORWARDS;
      }
      // Otherwise we are either slowing down manually (hiting the break) or naturally
      return backwards ? EngineStates.SLOWINGDOWN : EngineStates.SLOWINGDOWN_NATURALLY;
   }

   private EngineStates handleIdle(boolean forwardsOrBackwards) {
      return forwardsOrBackwards ? EngineStates.ACCELERATING : EngineStates.IDLE;
   }

   private EngineStates handleSlowingDown(boolean forwards, boolean backwards) {
      return handleSlowingDownInternal(forwards, backwards);
   }

   private EngineStates handleSlowingDownNaturally(boolean forwards, boolean backwards) {
      return handleSlowingDownInternal(forwards, backwards);
   }

   private EngineStates handleSlowingDownInternal(boolean forwards, boolean backwards) {
      if (isAcceleratingIntoOpositDirection(forwards, backwards)) {
         engineAccelerator.slowdown();
         return engineAccelerator.isDoneAccelerating() ? EngineStates.ACCELERATING : EngineStates.SLOWINGDOWN;
      }
      engineAccelerator.slowdownNaturally();
      return engineAccelerator.isDoneAccelerating() ? EngineStates.IDLE : EngineStates.SLOWINGDOWN_NATURALLY;
   }

   /*
    * We need to hit the break when the engine is accelerating in the oposit direction.
    * e.g. when a vehicle is moving forward and the user input is 'move backwards'
    */
   private boolean isAcceleratingIntoOpositDirection(boolean forwards, boolean backwards) {
      return isEngineMovingForward && backwards || isEngineMovingBackward && forwards;
   }

   private EngineStates handleAccelerating(boolean forwards, boolean backwards) {
      if (forwards) {
         engineAccelerator.accelerate();
         return engineAccelerator.isDoneAccelerating() ? EngineStates.MOVING_FORWARDS : EngineStates.ACCELERATING;
      } else if (backwards) {
         engineAccelerator.accelerate();
         return engineAccelerator.isDoneAccelerating() ? EngineStates.MOVING_BACKWARDS : EngineStates.ACCELERATING;
      }
      engineAccelerator.slowdownNaturally();
      return engineAccelerator.isDoneAccelerating() ? EngineStates.IDLE : EngineStates.SLOWINGDOWN_NATURALLY;
   }

   private boolean isMovingBackwards(MovingDirections movingDirection) {
      return movingDirection == MovingDirections.BACKWARDS;
   }

   private static boolean isMovingForwards(MovingDirections movingDirection) {
      return movingDirection == MovingDirections.FORWARDS;
   }
}

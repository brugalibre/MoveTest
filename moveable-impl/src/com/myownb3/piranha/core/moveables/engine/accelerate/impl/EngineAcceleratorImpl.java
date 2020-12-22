package com.myownb3.piranha.core.moveables.engine.accelerate.impl;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.myownb3.piranha.core.moveables.engine.EngineStates;
import com.myownb3.piranha.core.moveables.engine.accelerate.EngineAccelerator;
import com.myownb3.piranha.core.moveables.engine.accelerate.transmission.EngineTransmissionConfig;

public class EngineAcceleratorImpl implements EngineAccelerator {
   private static final int ZERO = 0;

   private EngineTransmissionConfig engineTransmissionConfig;
   private double manuallySlowDownSpeed;
   private double naturallySlowDownSpeed;
   private EngineStates engineState;

   private long lastTimeStamp;
   private int currentGrading;

   private EngineAcceleratorImpl(EngineTransmissionConfig engineTransmissionConfig, double manuallySlowDownSpeed, double naturallySlowDownSpeed) {
      this.currentGrading = ZERO;
      this.engineTransmissionConfig = engineTransmissionConfig;
      this.manuallySlowDownSpeed = manuallySlowDownSpeed;
      this.naturallySlowDownSpeed = naturallySlowDownSpeed;
      this.engineState = EngineStates.IDLE;
      initTimeStamp();
   }

   @Override
   public void accelerate() {
      this.engineState = EngineStates.ACCELERATING;
      accelerateOrSlowDown(1);
   }

   @Override
   public void slowdown() {
      this.engineState = EngineStates.SLOWINGDOWN;
      accelerateOrSlowDown(-1);
   }

   @Override
   public void slowdownNaturally() {
      this.engineState = EngineStates.SLOWINGDOWN_NATURALLY;
      accelerateOrSlowDown(-1);
   }

   private void accelerateOrSlowDown(int increment) {
      setTimeStamp();
      if (isTimeUp()) {
         int newGrading = currentGrading + increment;
         this.currentGrading = max(min(newGrading, engineTransmissionConfig.getAmountOfGears()), ZERO);
         initTimeStamp();
      }
   }

   @Override
   public boolean isDoneAccelerating() {
      boolean isAtFirstOrLastGear = false;
      switch (engineState) {
         case ACCELERATING:
            isAtFirstOrLastGear = currentGrading == engineTransmissionConfig.getAmountOfGears();
            break;
         case SLOWINGDOWN:// Fall through
         case SLOWINGDOWN_NATURALLY:
            isAtFirstOrLastGear = currentGrading == ZERO;
            break;
         default:
            isAtFirstOrLastGear = true;
            break;
      }
      return isTimeUp()
            && isAtFirstOrLastGear;
   }

   private boolean isTimeUp() {
      long now = System.currentTimeMillis();
      return now - lastTimeStamp >= getCurrentAcceleratingSpeed();
   }

   /**
    * Returns the velocity according to the current grading and gear transmission ratio
    * 
    * @return the velocity according to the current grading
    */
   @Override
   public int getCurrentVelocity() {
      if (currentGrading == ZERO) {
         return 0;
      }
      return engineTransmissionConfig.getCurrentMaxVelocity(currentGrading);
   }

   /**
    * Returns the accelerations speed according to the current grading and gear transmission ratio
    * 
    * @return the accelerations speed according to the current grading
    */
   @Override
   public double getCurrentAcceleratingSpeed() {
      switch (engineState) {
         case ACCELERATING:
            if (currentGrading == 0) {
               return 0;
            }
            return engineTransmissionConfig.getCurrentAccelerationSpeed(currentGrading);
         case SLOWINGDOWN:
            return manuallySlowDownSpeed;
         case SLOWINGDOWN_NATURALLY:
            return naturallySlowDownSpeed;
         default:
            return 0;
      }
   }

   private void initTimeStamp() {
      this.lastTimeStamp = 0;
   }

   private void setTimeStamp() {
      if (isTimeStampInitialized()) {
         lastTimeStamp = System.currentTimeMillis();
      }
   }

   private boolean isTimeStampInitialized() {
      return lastTimeStamp == 0;
   }

   public static class EngineAcceleratorBuilder {
      private EngineTransmissionConfig engineTransmissionConfig;
      private double naturallySlowDownSpeed;
      private double manuallySlowDownSpeed;

      private EngineAcceleratorBuilder() {
         // private 
      }

      public EngineAcceleratorBuilder withEngineTransmissionConfig(EngineTransmissionConfig engineTransmissionConfig) {
         this.engineTransmissionConfig = engineTransmissionConfig;
         return this;
      }

      public EngineAcceleratorBuilder withManuallySlowDownSpeed(double manuallySlowDownSpeed) {
         this.manuallySlowDownSpeed = manuallySlowDownSpeed;
         return this;
      }

      public EngineAcceleratorBuilder withNaturallySlowDownSpeed(double naturallySlowDownSpeed) {
         this.naturallySlowDownSpeed = naturallySlowDownSpeed;
         return this;
      }

      public EngineAcceleratorImpl build() {
         return new EngineAcceleratorImpl(engineTransmissionConfig, manuallySlowDownSpeed, naturallySlowDownSpeed);
      }

      public static EngineAcceleratorBuilder builder() {
         return new EngineAcceleratorBuilder();
      }
   }
}

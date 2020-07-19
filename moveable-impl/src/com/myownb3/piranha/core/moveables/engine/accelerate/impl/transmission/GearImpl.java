package com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission;

import com.myownb3.piranha.core.moveables.engine.accelerate.transmission.Gear;

public class GearImpl implements Gear {

   private double accelerationSpeed;
   private int number;
   private int maxVelocity;

   private GearImpl(double accelerationSpeed, int number, int maxVelocity) {
      this.accelerationSpeed = accelerationSpeed;
      this.number = number;
      this.maxVelocity = maxVelocity;
   }

   @Override
   public int getNumber() {
      return number;
   }

   @Override
   public int getMaxVelocity() {
      return maxVelocity;
   }

   @Override
   public double getAccelerationSpeed() {
      return accelerationSpeed;
   }

   public static class GearBuilder {
      private double accelerationSpeed;
      private int number;
      private int maxVelocity;

      private GearBuilder() {
         // private
      }

      public GearBuilder withNumber(int number) {
         this.number = number;
         return this;
      }

      public GearBuilder withAccelerationSpeed(double accelerationSpeed) {
         this.accelerationSpeed = accelerationSpeed;
         return this;
      }

      public GearBuilder withMaxVelocity(int maxVelocity) {
         this.maxVelocity = maxVelocity;
         return this;
      }

      public Gear buil() {
         return new GearImpl(accelerationSpeed, number, maxVelocity);
      }

      public static GearBuilder builder() {
         return new GearBuilder();
      }
   }
}

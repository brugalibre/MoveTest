package com.myownb3.piranha.core.weapon.tank.countermeasure;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link DecoyFlareDispenser} is responsible for deploying decoy flares
 * 
 * @author Dominic
 *
 */
public class DecoyFlareDispenser {
   public static final int DIMENSION_RADIUS_MULTIPLIER = 3;
   private DecoyFlareConfig decoyFlareConfig;
   private int minTimeBetweenDispensing;
   private long lastTimeStamp;

   private DecoyFlareDispenser(DecoyFlareConfig decoyFlareConfig, int minTimeBetweenDispensing) {
      this.decoyFlareConfig = decoyFlareConfig;
      this.minTimeBetweenDispensing = minTimeBetweenDispensing;
   }

   /**
    * Dispenses the decoy flares from the given {@link Position}
    * 
    * @param deployFromPosition
    *        the origin {@link Position}
    */
   public void dispenseDecoyFlares(Position deployFromPosition) {
      if (isDispenserAvailable()) {
         dispenseDecoyFlaresInternal(deployFromPosition);
      }
      setLastTimeStamp();
   }

   private void dispenseDecoyFlaresInternal(Position deployFromPositionIn) {
      double angleIncrement = getAngleIncrement();
      Position deployFromPosition = deployFromPositionIn.rotate(-decoyFlareConfig.getDecoyFlareSpreadAngle() / 2);
      for (int i = 0; i < decoyFlareConfig.getAmountDecoyFlares(); i++) {
         Position deployFromPosition4Decoy = getDeployPosition4CurrentDecoyFlare(deployFromPosition, i + 1, angleIncrement);
         DecoyFlareFactory.INSTANCE.createDecoyFlare(deployFromPosition4Decoy, decoyFlareConfig);
      }
   }

   private double getAngleIncrement() {
      double decoyFlareSpreadAngle = decoyFlareConfig.getDecoyFlareSpreadAngle();
      return decoyFlareSpreadAngle / (decoyFlareConfig.getAmountDecoyFlares() + 1);
   }

   private Position getDeployPosition4CurrentDecoyFlare(Position deployFromPosition, int currentDecoyCounter, double angleIncrement) {
      double distance = getDistance();
      return deployFromPosition.rotate(currentDecoyCounter * angleIncrement)
            .movePositionForward4Distance(distance);
   }

   private double getDistance() {
      double dimensionRadius = decoyFlareConfig.getDimensionInfo().getDimensionRadius();
      return dimensionRadius * DIMENSION_RADIUS_MULTIPLIER;
   }

   private boolean isDispenserAvailable() {
      long now = System.currentTimeMillis();
      return now - lastTimeStamp >= minTimeBetweenDispensing;
   }

   private void setLastTimeStamp() {
      lastTimeStamp = System.currentTimeMillis();
   }

   public DecoyFlareConfig getDecoyFlareConfig() {
      return decoyFlareConfig;
   }

   public static class DecoyFlareDispenserBuilder {
      private DecoyFlareConfig decoyFlareConfig;
      private int minTimeBetweenDispensing;

      private DecoyFlareDispenserBuilder() {
         // private
      }

      public DecoyFlareDispenserBuilder withDecoyFlareConfig(DecoyFlareConfig decoyFlareConfig) {
         this.decoyFlareConfig = decoyFlareConfig;
         return this;
      }

      public DecoyFlareDispenserBuilder withMinTimeBetweenDispensing(int minTimeBetweenDispensing) {
         this.minTimeBetweenDispensing = minTimeBetweenDispensing;
         return this;
      }

      public DecoyFlareDispenser build() {
         requireNonNull(decoyFlareConfig);
         return new DecoyFlareDispenser(decoyFlareConfig, minTimeBetweenDispensing);
      }

      public static DecoyFlareDispenserBuilder builder() {
         return new DecoyFlareDispenserBuilder();
      }
   }
}

/**
 * 
 */
package com.myownb3.piranha.detector.config.impl;

import com.myownb3.piranha.detector.config.DetectorConfig;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;

public class DetectorConfigImpl implements DetectorConfig {

   private int detectorReach;
   private int evasionDistance;
   private double detectorAngle;
   private double evasionAngle;
   private double evasionAngleInc;

   private DetectorConfigImpl(int detectorReach, int evasionDistance, double detectorAngle, double evasionAngle, double evasionAngleInc) {
      this.evasionAngleInc = evasionAngleInc;
      this.detectorReach = detectorReach;
      this.evasionDistance = evasionDistance;
      this.detectorAngle = detectorAngle;
      this.evasionAngle = evasionAngle;
   }

   @Override
   public double getEvasionAngleInc() {
      return evasionAngleInc;
   }

   @Override
   public final int getDetectorReach() {
      return this.detectorReach;
   }

   @Override
   public final int getEvasionDistance() {
      return this.evasionDistance;
   }

   @Override
   public final double getDetectorAngle() {
      return this.detectorAngle;
   }

   @Override
   public final double getEvasionAngle() {
      return this.evasionAngle;
   }

   /**
    * Creates a new {@link DetectorConfig} for the given {@link EvasionStateMachineConfig}
    * 
    * @param config
    *        the {@link EvasionStateMachineConfig}
    * @return a new {@link DetectorConfig} for the given {@link EvasionStateMachineConfig}
    */
   public static DetectorConfig of(EvasionStateMachineConfig config) {
      return DetectorConfigBuilder.builder()
            .withDetectorAngle(config.getDetectorAngle())
            .withDetectorReach(config.getDetectorReach())
            .withEvasionDistance(config.getEvasionDistance())
            .withEvasionAngle(config.getEvasionAngle())
            .withEvasionAngleInc(config.getEvasionAngleInc())
            .build();
   }

   public static class DetectorConfigBuilder {
      private int detectorReach;
      private double detectorAngle;
      private int evasionDistance;
      private double evasionAngle;
      private double evasionAngleInc;

      private DetectorConfigBuilder() {
         // private 
      }

      public DetectorConfigBuilder withEvasionAngleInc(double evasionAngleInc) {
         this.evasionAngleInc = evasionAngleInc;
         return this;
      }

      public DetectorConfigBuilder withDetectorReach(int detectorReach) {
         this.detectorReach = detectorReach;
         return this;
      }

      public DetectorConfigBuilder withEvasionDistance(int evasionDistance) {
         this.evasionDistance = evasionDistance;
         return this;
      }

      public DetectorConfigBuilder withDetectorAngle(double detectorAngle) {
         this.detectorAngle = detectorAngle;
         return this;
      }

      public DetectorConfigBuilder withEvasionAngle(double evasionAngle) {
         this.evasionAngle = evasionAngle;
         return this;
      }

      public DetectorConfig build() {
         return new DetectorConfigImpl(detectorReach, evasionDistance, detectorAngle, evasionAngle, evasionAngleInc);
      }

      public static DetectorConfigBuilder builder() {
         return new DetectorConfigBuilder();
      }
   }
}

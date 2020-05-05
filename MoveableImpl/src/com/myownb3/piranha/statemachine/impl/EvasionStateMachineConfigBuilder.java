/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import com.myownb3.piranha.detector.config.DetectorConfig;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;

/**
 * The {@link EvasionStateMachineConfigBuilder} is the builder to build a default the {@link EvasionStateMachineConfig}
 */
public class EvasionStateMachineConfigBuilder {

   // Attributes of the different EvasionStateHandlerse
   private double returningMinDistance;
   private double returningAngleIncMultiplier;
   private int passingDistance;
   private double orientationAngle;

   private double returningAngleMargin;
   private int postEvasionReturnAngle;
   private DetectorConfig detectorConfig;

   private EvasionStateMachineConfigBuilder() {
      // private
   }

   public EvasionStateMachineConfigBuilder withOrientationAngle(double orientationAngle) {
      this.orientationAngle = orientationAngle;
      return this;
   }

   public EvasionStateMachineConfigBuilder withReturningMinDistance(double returningMinDistance) {
      this.returningMinDistance = returningMinDistance;
      return this;
   }

   public EvasionStateMachineConfigBuilder withReturningAngleMargin(double returningAngleMargin) {
      this.returningAngleMargin = returningAngleMargin;
      return this;
   }

   public EvasionStateMachineConfigBuilder withReturningAngleIncMultiplier(double returningAngleIncMultiplier) {
      this.returningAngleIncMultiplier = returningAngleIncMultiplier;
      return this;
   }

   public EvasionStateMachineConfigBuilder withPassingDistance(int passingDistance) {
      this.passingDistance = passingDistance;
      return this;
   }

   public EvasionStateMachineConfigBuilder withPostEvasionReturnAngle(int postEvasionReturnAngle) {
      this.postEvasionReturnAngle = postEvasionReturnAngle;
      return this;
   }

   public EvasionStateMachineConfigBuilder withDetectorConfig(DetectorConfig detectorConfig) {
      this.detectorConfig = detectorConfig;
      return this;
   }

   public EvasionStateMachineConfig build() {
      return new EvasionStateMachineConfigImpl(returningAngleIncMultiplier, orientationAngle, returningMinDistance, returningAngleMargin,
            passingDistance, postEvasionReturnAngle, detectorConfig);
   }

   /**
    * @return a new instance of a EvasionStateMachineConfigBuilder
    */
   public static EvasionStateMachineConfigBuilder builder() {
      return new EvasionStateMachineConfigBuilder();
   }
}

/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

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

   // Attributes for Detector
   private int detectorReach;
   private int detectorAngle;
   private int evasionAngle;
   private double evasionAngleInc;
   private double returningAngleMargin;

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

   public EvasionStateMachineConfigBuilder withEvasionAngleInc(double evasionAngleInc) {
      this.evasionAngleInc = evasionAngleInc;
      return this;
   }

   public EvasionStateMachineConfigBuilder withDetectorReach(int detectorReach) {
      this.detectorReach = detectorReach;
      return this;
   }

   public EvasionStateMachineConfigBuilder withPassingDistance(int passingDistance) {
      this.passingDistance = passingDistance;
      return this;
   }

   public EvasionStateMachineConfigBuilder withDetectorAngle(int detectorAngle) {
      this.detectorAngle = detectorAngle;
      return this;
   }

   public EvasionStateMachineConfigBuilder withEvasionAngle(int evasionAngle) {
      this.evasionAngle = evasionAngle;
      return this;
   }

   public EvasionStateMachineConfig build() {
      return new EvasionStateMachineConfigImpl(returningAngleIncMultiplier, orientationAngle, returningMinDistance, returningAngleMargin,
            detectorReach, passingDistance, detectorAngle, evasionAngle, evasionAngleInc);
   }

   /**
    * @return a new instance of a EvasionStateMachineConfigBuilder
    */
   public static EvasionStateMachineConfigBuilder builder() {
      return new EvasionStateMachineConfigBuilder();
   }
}

/**
 * 
 */
package com.myownb3.piranha.core.statemachine.impl;

import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;

/**
 * The {@link EvasionStateMachineConfigImpl} is the default implementation for
 * the {@link EvasionStateMachineConfig}
 */
public class EvasionStateMachineConfigImpl implements EvasionStateMachineConfig {

   // Attributes of the different EvasionStateHandlerse
   private double returningMinDistance;
   private double returningAngleIncMultiplier;
   private int passingDistance;
   private double orientationAngle;
   private double returningAngleMargin;
   private int postEvasionReturnAngle;

   private DetectorConfig detectorConfig;

   /**
    * Creates a new {@link EvasionStateMachineConfig} with the given values.
    * <b>Attention:</b> Because this constructor has some default value, it's usage is only for testing purpose! It's highly recommended to
    * use the {@link EvasionStateMachineConfigBuilder}
    * 
    * @param angleIncMultiplier
    * @param minDistance
    * @param angleMargin
    * @param detectorReach
    * @param detectorAngle
    * @param evasionAngle
    * @param evasionAngleInc
    * 
    * @see {@link EvasionStateMachineConfigBuilder}
    */
   public EvasionStateMachineConfigImpl(double angleIncMultiplier, double minDistance, double angleMargin,
         int detectorReach, int detectorAngle, int evasionAngle, double evasionAngleInc) {
      this(angleIncMultiplier, 10, minDistance, angleMargin, 2 * detectorReach / 3, 4, DetectorConfigBuilder.builder()
            .withDetectorReach(detectorReach)
            .withEvasionAngle(evasionAngle)
            .withDetectorAngle(detectorAngle)
            .withEvasionAngleInc(evasionAngleInc)
            .withEvasionDistance(2 * detectorReach / 3)
            .build());
   }

   /*package */ EvasionStateMachineConfigImpl(double angleIncMultiplier, double orientationAngle, double minDistance,
         double angleMargin, int passingDistance, int postEvasionReturnAngle, DetectorConfig detectorConfig) {
      this.orientationAngle = orientationAngle;
      this.postEvasionReturnAngle = postEvasionReturnAngle;
      this.returningAngleIncMultiplier = angleIncMultiplier;
      this.returningMinDistance = minDistance;
      this.returningAngleMargin = angleMargin;

      this.passingDistance = passingDistance;
      this.detectorConfig = detectorConfig;
   }

   @Override
   public final double getOrientationAngle() {
      return this.orientationAngle;
   }

   @Override
   public int getPostEvasionReturnAngle() {
      return postEvasionReturnAngle;
   }

   @Override
   public final double getReturningMinDistance() {
      return this.returningMinDistance;
   }

   @Override
   public double getReturningAngleMargin() {
      return returningAngleMargin;
   }

   @Override
   public final double getReturningAngleIncMultiplier() {
      return this.returningAngleIncMultiplier;
   }

   @Override
   public double getEvasionAngleInc() {
      return detectorConfig.getEvasionAngleInc();
   }

   @Override
   public final int getDetectorReach() {
      return this.detectorConfig.getDetectorReach();
   }

   @Override
   public final int getEvasionDistance() {
      return this.detectorConfig.getEvasionDistance();
   }

   @Override
   public final int getPassingDistance() {
      return this.passingDistance;
   }

   @Override
   public final double getDetectorAngle() {
      return this.detectorConfig.getDetectorAngle();
   }

   @Override
   public final double getEvasionAngle() {
      return this.detectorConfig.getEvasionAngle();
   }
}

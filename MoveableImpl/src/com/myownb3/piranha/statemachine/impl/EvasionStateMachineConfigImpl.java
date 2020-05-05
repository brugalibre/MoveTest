/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;

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

   // Attributes for Detector
   private int detectorReach;
   private int evasionDistance;
   private int detectorAngle;
   private int evasionAngle;
   private double evasionAngleInc;
   private double returningAngleMargin;
   private int postEvasionReturnAngle;

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
      this(angleIncMultiplier, 10, minDistance, angleMargin, detectorReach, 2 * detectorReach / 3, 2 * detectorReach / 3,
            detectorAngle, evasionAngle, evasionAngleInc, 4);
   }

   /*package */ EvasionStateMachineConfigImpl(double angleIncMultiplier, double orientationAngle, double minDistance,
         double angleMargin, int detectorReach, int evasionDistance, int passingDistance, int detectorAngle, int evasionAngle,
         double evasionAngleInc, int postEvasionReturnAngle) {
      this.orientationAngle = orientationAngle;
      this.postEvasionReturnAngle = postEvasionReturnAngle;
      this.returningAngleIncMultiplier = angleIncMultiplier;
      this.returningMinDistance = minDistance;
      this.returningAngleMargin = angleMargin;

      this.passingDistance = passingDistance;
      this.evasionAngleInc = evasionAngleInc;
      this.detectorReach = detectorReach;
      this.evasionDistance = evasionDistance;
      this.detectorAngle = detectorAngle;
      this.evasionAngle = evasionAngle;
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
   public final int getPassingDistance() {
      return this.passingDistance;
   }

   @Override
   public final int getDetectorAngle() {
      return this.detectorAngle;
   }

   @Override
   public final int getEvasionAngle() {
      return this.evasionAngle;
   }
}

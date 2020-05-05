package com.myownb3.piranha.detector.evasion.impl;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.myownb3.piranha.detector.detectionaware.DetectionAware;
import com.myownb3.piranha.detector.evasion.EvasionAngleEvaluator;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.position.Position;

public class DefaultEvasionAngleEvaluatorImpl implements EvasionAngleEvaluator {

   private double angleInc;
   private double detectorAngle;
   private DetectionAware detectionAware;

   private DefaultEvasionAngleEvaluatorImpl(double detectorAngle, double angleInc) {
      this.angleInc = angleInc;
      this.detectorAngle = detectorAngle;
   }

   @Override
   public double getEvasionAngleRelative2(Position position) {
      Optional<Avoidable> evasionAvoidable = detectionAware.getNearestEvasionAvoidable(position);
      if (evasionAvoidable.isPresent()) {
         return getEvasionAngle4DetectedPositions(evasionAvoidable.get(), position);
      }
      return 0.0;
   }


   private double getEvasionAngle4DetectedPositions(Avoidable avoidable, Position position) {
      int negCounter = 0;
      int posCounter = 0;
      for (Position detectedPosition : detectionAware.getDetectedPositions4GridElement(avoidable)) {
         double evasionAngle = getEvasionAngleRelative2(avoidable, detectedPosition);
         if (evasionAngle < 0) {
            negCounter++;
         } else {
            posCounter++;
         }
      }
      return negCounter > posCounter ? angleInc : -angleInc;
   }


   private double getEvasionAngleRelative2(Avoidable avoidable, Position detectedPosition) {
      boolean isInUpperBounds = avoidable.getShape().isWithinUpperBounds(detectedPosition, detectorAngle);
      return isInUpperBounds ? -angleInc : angleInc; // - -> Turn to the left & + Turn to the right
   }

   @Override
   public void setDetectionAware(DetectionAware detectionAware) {
      this.detectionAware = requireNonNull(detectionAware);
   }

   public static class DefaultEvasionAngleEvaluatorBuilder {

      private double angleInc;
      private double detectorAngle;

      private DefaultEvasionAngleEvaluatorBuilder() {
         // private
      }

      public DefaultEvasionAngleEvaluatorBuilder withDetectorAngle(double detectorAngle) {
         this.detectorAngle = detectorAngle;
         return this;
      }

      public DefaultEvasionAngleEvaluatorBuilder withAngleInc(double angleInc) {
         this.angleInc = angleInc;
         return this;
      }

      public DefaultEvasionAngleEvaluatorImpl build() {
         return new DefaultEvasionAngleEvaluatorImpl(detectorAngle, angleInc);
      }

      public static DefaultEvasionAngleEvaluatorBuilder builder() {
         return new DefaultEvasionAngleEvaluatorBuilder();
      }
   }
}

package com.myownb3.piranha.core.detector.evasion.impl;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.myownb3.piranha.core.detector.detectionaware.DetectionAware;
import com.myownb3.piranha.core.detector.evasion.EvasionAngleEvaluator;
import com.myownb3.piranha.core.grid.gridelement.Avoidable;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class DefaultEvasionAngleEvaluatorImpl implements EvasionAngleEvaluator {

   private double angleInc;
   private DetectionAware detectionAware;

   private DefaultEvasionAngleEvaluatorImpl(double angleInc) {
      this.angleInc = angleInc;
   }

   @Override
   public double getEvasionAngleRelative2(Position position) {
      Optional<Avoidable> evasionAvoidable = detectionAware.getNearestEvasionAvoidable(position);
      if (evasionAvoidable.isPresent()) {
         return getEvasionAngle4DetectedPositions(evasionAvoidable.get(), position);
      }
      return 0.0;
   }


   private double getEvasionAngle4DetectedPositions(Avoidable avoidable, Position detectorPosition) {
      Shape avoidableShape = avoidable.getShape();
      boolean isInUpperBounds =
            avoidableShape.isWithinUpperBounds(detectionAware.getDetectedPositions4GridElement(avoidable), detectorPosition);
      return isInUpperBounds ? -angleInc : angleInc; // - -> Turn to the left & + Turn to the right
   }

   @Override
   public void setDetectionAware(DetectionAware detectionAware) {
      this.detectionAware = requireNonNull(detectionAware);
   }

   public static class DefaultEvasionAngleEvaluatorBuilder {

      private double angleInc;

      private DefaultEvasionAngleEvaluatorBuilder() {
         // private
      }

      public DefaultEvasionAngleEvaluatorBuilder withAngleInc(double angleInc) {
         this.angleInc = angleInc;
         return this;
      }

      public DefaultEvasionAngleEvaluatorImpl build() {
         return new DefaultEvasionAngleEvaluatorImpl(angleInc);
      }

      public static DefaultEvasionAngleEvaluatorBuilder builder() {
         return new DefaultEvasionAngleEvaluatorBuilder();
      }
   }
}

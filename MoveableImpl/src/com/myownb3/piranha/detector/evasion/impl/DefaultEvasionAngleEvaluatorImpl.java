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

   public DefaultEvasionAngleEvaluatorImpl(double detectorAngle, double angleInc) {
      this.angleInc = angleInc;
      this.detectorAngle = detectorAngle;
   }

   @Override
   public double getEvasionAngleRelative2(Position position) {
      Optional<Avoidable> evasionAvoidable = detectionAware.getNearestEvasionAvoidable(position);
      if (evasionAvoidable.isPresent()) {
         Avoidable avoidable = evasionAvoidable.get();
         boolean isInUpperBounds = avoidable.getShape().isWithinUpperBounds(position, detectorAngle);
         return isInUpperBounds ? -angleInc : angleInc; // - -> Turn to the left & + Turn to the right
      }
      return 0.0;
   }

   @Override
   public void setDetectionAware(DetectionAware detectionAware) {
      this.detectionAware = requireNonNull(detectionAware);
   }
}

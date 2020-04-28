package com.myownb3.piranha.detector.evasion.impl;

import com.myownb3.piranha.detector.evasion.EvasionAngleEvaluator;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.position.Position;

public class DefaultEvasionAngleEvaluatorImpl implements EvasionAngleEvaluator {

   private double angleInc;
   private double detectorAngle;

   public DefaultEvasionAngleEvaluatorImpl(double detectorAngle, double angleInc) {
      this.angleInc = angleInc;
      this.detectorAngle = detectorAngle;
   }

   @Override
   public double getEvasionAngleRelative2(Avoidable avoidable, Position position) {
      boolean isInUpperBounds = avoidable.getShape().isWithinUpperBounds(position, detectorAngle);
      return isInUpperBounds ? -angleInc : angleInc; // - -> Turn to the left & + Turn to the right
   }
}

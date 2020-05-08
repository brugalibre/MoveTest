package com.myownb3.piranha.core.detector.cluster.tripple;

import com.myownb3.piranha.core.detector.IDetector;

public class IDetectorInfoImpl implements IDetectorInfo {

   private IDetector detector;
   private double offsetAngle;

   private IDetectorInfoImpl(IDetector detector, double offsetAngle) {
      this.detector = detector;
      this.offsetAngle = offsetAngle;
   }

   @Override
   public IDetector getDetector() {
      return this.detector;
   }

   @Override
   public double getOffsetAngle() {
      return this.offsetAngle;
   }

   public static IDetectorInfoImpl of(IDetector detector, double offsetAngle) {
      return new IDetectorInfoImpl(detector, offsetAngle);
   }
}

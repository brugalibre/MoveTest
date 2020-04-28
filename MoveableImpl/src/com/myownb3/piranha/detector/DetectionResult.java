package com.myownb3.piranha.detector;

public class DetectionResult {
   boolean isEvasion;
   boolean isDetected;

   public DetectionResult(boolean isEvasion, boolean isDetected) {
      this.isDetected = isDetected;
      this.isEvasion = isEvasion;
   }

   public DetectionResult() {
      this(false, false);
   }

   public boolean getIsDetected() {
      return isDetected;
   }

   public boolean getIsEvasion() {
      return isEvasion;
   }
}

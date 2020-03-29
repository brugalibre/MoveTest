package com.myownb3.piranha.detector;

class DetectionResult {
   boolean isEvasion;
   boolean isDetected;

   public DetectionResult(boolean isEvasion, boolean isDetected) {
      this.isDetected = isDetected;
      this.isEvasion = isEvasion;
   }

   boolean getIsDetected() {
      return isDetected;
   }

   boolean getIsEvasion() {
      return isEvasion;
   }
}

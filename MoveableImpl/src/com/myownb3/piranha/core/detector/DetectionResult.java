package com.myownb3.piranha.core.detector;

import com.myownb3.piranha.core.grid.gridelement.position.Position;

public class DetectionResult {
   boolean isEvasion;
   boolean isDetected;
   Position detectedPosition;

   public DetectionResult(boolean isEvasion, boolean isDetected, Position detectedPosition) {
      this.isDetected = isDetected;
      this.isEvasion = isEvasion;
      this.detectedPosition = detectedPosition;
   }

   public DetectionResult() {
      this(false, false, null);
   }

   public boolean getIsDetected() {
      return isDetected;
   }

   public boolean getIsEvasion() {
      return isEvasion;
   }

   public Position getDetectedPosition() {
      return this.detectedPosition;
   }

}

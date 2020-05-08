package com.myownb3.piranha.core.detector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;

class DetectionResultTest {

   @Test
   void testBuildDetectionResult() {

      // Given
      Position detectedPos = Positions.of(0, 0);
      // When
      DetectionResult detectionResult = new DetectionResult(false, false, detectedPos);
      // Then
      assertThat(detectionResult.getDetectedPosition(), is(detectedPos));
   }
}

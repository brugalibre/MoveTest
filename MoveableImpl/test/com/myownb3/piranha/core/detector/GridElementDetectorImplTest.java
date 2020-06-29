package com.myownb3.piranha.core.detector;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.detector.GridElementDetectorImpl.GridElementDetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;

class GridElementDetectorImplTest {

   @Test
   void testIsEvasion() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetector()
            .withDetectedGridElement()
            .withGridElement()
            .withGrid()
            .build();

      // When
      tcb.gridElementDetector.isEvasion(tcb.gridElement);

      // Then
      verify(tcb.detector).isEvasion(eq(tcb.gridElement));
   }

   void testCheckSurrounding_TestDefaultCheckSurroundingFilter() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetector()
            .withDetectedGridElement()
            .withGridElement()
            .withGrid()
            .build();

      // When
      tcb.gridElementDetector.checkSurrounding(tcb.gridElement);

      // Then
      verify(tcb.detectedGridElement).isDetectedBy(eq(tcb.gridElement.getForemostPosition()), eq(tcb.detector));
   }

   private static class TestCaseBuilder {

      private Detector detector;
      private Grid grid;
      private GridElement detectedGridElement;
      private GridElement gridElement;
      private GridElementDetectorImpl gridElementDetector;

      private TestCaseBuilder withDetector() {
         this.detector = mock(Detector.class);
         return this;
      }

      private TestCaseBuilder withGrid() {
         grid = mock(Grid.class);
         when(grid.getAllAvoidableGridElementsWithinDistance(eq(gridElement), Mockito.anyInt()))
               .thenReturn(Collections.singletonList(detectedGridElement));
         return this;
      }

      private TestCaseBuilder withDetectedGridElement() {
         this.detectedGridElement = mock(GridElement.class);
         when(detectedGridElement.getForemostPosition()).thenReturn(Positions.of(5, 5));
         return this;
      }

      private TestCaseBuilder withGridElement() {
         this.gridElement = mock(GridElement.class);
         return this;
      }

      private TestCaseBuilder build() {
         this.gridElementDetector = GridElementDetectorBuilder.builder()
               .withDetector(detector)
               .withGrid(grid)
               .build();
         return this;
      }
   }
}

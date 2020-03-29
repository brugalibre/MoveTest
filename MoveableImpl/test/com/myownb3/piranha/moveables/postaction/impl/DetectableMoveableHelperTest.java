/**
 * 
 */
package com.myownb3.piranha.moveables.postaction.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.moveables.Moveable;

/**
 * @author Dominic
 *
 */
class DetectableMoveableHelperTest {

   @Test
   void testGetDetectedAvoidable() {

      // Given
      Avoidable detectedAvoidable = mock(Avoidable.class);
      Detector detector = mockDetector(detectedAvoidable);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveable()
            .withExpectedAvoidables(mockExpectedAvoidables(detectedAvoidable))
            .withGrid()
            .withHelper(new DetectableMoveableHelper(detector))
            .build();

      // When
      List<Avoidable> actualDetectedAvoidable = tcb.helper.getDetectedAvoidable(tcb.grid, tcb.moveable);

      // Then
      assertThat(actualDetectedAvoidable.size(), is(1));
      assertThat(actualDetectedAvoidable.get(0), is(detectedAvoidable));
   }

   private Detector mockDetector(Avoidable detectedAvoidable) {
      Detector detector = mock(Detector.class);
      when(detector.hasObjectDetected(eq(detectedAvoidable))).thenReturn(true);
      return detector;
   }

   private class TestCaseBuilder {

      private Grid grid;
      private Moveable moveable;
      private DetectableMoveableHelper helper;
      private List<Avoidable> expectedAvoidables;

      public TestCaseBuilder withMoveable() {
         this.moveable = mockMoveable();
         return this;
      }

      public TestCaseBuilder build() {
         return this;
      }

      public TestCaseBuilder withHelper(DetectableMoveableHelper helper) {
         this.helper = helper;
         return this;
      }

      public TestCaseBuilder withExpectedAvoidables(List<Avoidable> expectedAvoidables) {
         this.expectedAvoidables = expectedAvoidables;
         return this;
      }

      public TestCaseBuilder withGrid() {
         Grid grid = mockGrid(expectedAvoidables, moveable);
         this.grid = grid;
         return this;
      }
   }

   private List<Avoidable> mockExpectedAvoidables(Avoidable detectedAvoidable) {
      List<Avoidable> expectedAvoidables = new ArrayList<>();
      Avoidable avoidable = mock(Avoidable.class);
      expectedAvoidables.add(avoidable);
      expectedAvoidables.add(detectedAvoidable);
      return expectedAvoidables;
   }

   private Grid mockGrid(List<Avoidable> expectedAvoidables, GridElement gridElement) {
      Grid grid = mock(Grid.class);
      when(grid.getAllAvoidables(eq(gridElement))).thenReturn(expectedAvoidables);
      return grid;
   }

   private Moveable mockMoveable() {
      return mock(Moveable.class);
   }

}

/**
 * 
 */
package com.myownb3.piranha.core.moveables.postaction.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * @author Dominic
 *
 */
class DetectableMoveableHelperTest {

   @Test
   void testGetDetectedGridElement() {

      // Given
      Obstacle detectedGridElement = mock(Obstacle.class);
      Detector detector = mockDetector(detectedGridElement);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveable()
            .withExpectedObstacles(mockExpectedGridElements(detectedGridElement))
            .withGrid()
            .withHelper(new DetectableMoveableHelper(detector))
            .build();

      // When
      List<GridElement> actualDetectedGridElement = tcb.helper.getDetectedGridElement(tcb.grid, tcb.moveable);

      // Then
      assertThat(actualDetectedGridElement.size(), is(1));
      assertThat(actualDetectedGridElement.get(0), is(detectedGridElement));
   }

   private Detector mockDetector(Obstacle detectedObstacle) {
      Detector detector = mock(Detector.class);
      when(detector.hasObjectDetected(eq(detectedObstacle))).thenReturn(true);
      return detector;
   }

   private class TestCaseBuilder {

      private Grid grid;
      private Moveable moveable;
      private DetectableMoveableHelper helper;
      private List<GridElement> expectedGridElements;

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

      public TestCaseBuilder withExpectedObstacles(List<GridElement> expectedGridElement) {
         this.expectedGridElements = expectedGridElement;
         return this;
      }

      public TestCaseBuilder withGrid() {
         Grid grid = mockGrid(expectedGridElements, moveable);
         this.grid = grid;
         return this;
      }
   }

   private List<GridElement> mockExpectedGridElements(Obstacle detectedObstacle) {
      List<GridElement> expectedGridElements = new ArrayList<>();
      Obstacle obstacle = mock(Obstacle.class);
      expectedGridElements.add(obstacle);
      expectedGridElements.add(detectedObstacle);
      return expectedGridElements;
   }

   private Grid mockGrid(List<GridElement> expectedObstacles, GridElement gridElement) {
      Grid grid = mock(Grid.class);
      when(grid.getAllAvoidableGridElements(eq(gridElement))).thenReturn(expectedObstacles);
      return grid;
   }

   private Moveable mockMoveable() {
      return mock(Moveable.class);
   }

}

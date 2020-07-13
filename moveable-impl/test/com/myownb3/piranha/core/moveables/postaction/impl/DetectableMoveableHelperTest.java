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
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * @author Dominic
 *
 */
class DetectableMoveableHelperTest {

   @Test
   void testGetDetectedGridElement() {

      // Given
      Obstacle detectedGridElement = mockObstacle();
      Detector detector = mockDetector(detectedGridElement);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveable()
            .withExpectedObstacles(mockExpectedGridElements(detectedGridElement))
            .withGrid()
            .withHelper(detector)
            .build();

      // When
      List<GridElement> actualDetectedGridElement = tcb.helper.getDetectedGridElements(tcb.moveable);

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

      public TestCaseBuilder withHelper(Detector detector) {
         this.helper = new DetectableMoveableHelper(grid, detector);
         return this;
      }

      public TestCaseBuilder withExpectedObstacles(List<GridElement> expectedGridElement) {
         this.expectedGridElements = expectedGridElement;
         return this;
      }

      public TestCaseBuilder withGrid() {
         this.grid = mockGrid(expectedGridElements, moveable);
         return this;
      }

      private Grid mockGrid(List<GridElement> expectedObstacles, GridElement gridElement) {
         this.grid = mock(Grid.class);
         when(grid.getAllGridElements(eq(gridElement))).thenReturn(expectedObstacles);
         return grid;
      }

      private Moveable mockMoveable() {
         return mock(Moveable.class);
      }
   }

   private List<GridElement> mockExpectedGridElements(Obstacle detectedObstacle) {
      List<GridElement> expectedGridElements = new ArrayList<>();
      Obstacle obstacle = mockObstacle();
      expectedGridElements.add(obstacle);
      expectedGridElements.add(detectedObstacle);
      return expectedGridElements;
   }

   private Obstacle mockObstacle() {
      Obstacle obstacle = mock(Obstacle.class);
      when(obstacle.isAvoidable()).thenReturn(true);
      return obstacle;
   }



}

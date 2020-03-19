/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.shape.PointShape;

/**
 * @author Dominic
 *
 */
class SimpleGridElementTest {

   @Test
   void testNotAvoidableObject() {

      // Given
      Position detectorPos = Positions.of(5, 5);
      Position gridElemPos = Positions.of(4, 4);
      PointShape shape = spy(new PointShape(gridElemPos));
      GridElement gridElement = new SimpleGridElement(mock(DefaultGrid.class), gridElemPos, shape);
      Detector detector = mock(Detector.class);

      // When
      gridElement.isDetectedBy(detectorPos, detector);

      // Then
      verify(shape, never()).detectObject(any(), eq(detectorPos), eq(detector));
   }

   @Test
   void testGetFurthermostBackPosition() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      Position expectedBackPos = Positions.of(gridElemPos);
      expectedBackPos.rotate(180);
      PointShape pointShape = new PointShape(gridElemPos);
      GridElement gridElement = new SimpleGridElement(mock(DefaultGrid.class), gridElemPos, pointShape);

      // When
      Position positionOnPathFor = gridElement.getFurthermostBackPosition();

      // Then
      assertThat(positionOnPathFor, is(expectedBackPos));
   }

   @Test
   void testGetFurthermostFrontPosition() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      PointShape pointShape = new PointShape(gridElemPos);
      GridElement gridElement = new SimpleGridElement(Mockito.mock(DefaultGrid.class), gridElemPos, pointShape);

      // When
      Position positionOnPathFor = gridElement.getFurthermostFrontPosition();

      // Then
      assertThat(positionOnPathFor, is(gridElemPos));
   }

   /**
    * Test method for
    * {@link com.myownb3.piranha.grid.gridelement.SimpleGridElement#toString()}.
    */
   @Test
   void testToString() {
      // Given
      Position position = Positions.of(1, 1);
      Grid grid = GridBuilder.builder(5, 5)
            .build();
      SimpleGridElement element = new SimpleGridElement(grid, position);
      String expectedToString =
            "Position: Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '1.0', Y-Axis: '1.0'\nMax x:'5, Min x:'0; Max y:'5, Min y:'0";

      // When
      String toString = element.toString();

      // Then
      assertThat(toString, is(expectedToString));
   }

}

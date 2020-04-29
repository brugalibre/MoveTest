/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape;

/**
 * @author Dominic
 *
 */
class SimpleGridElementTest {

   @Test
   void testGetDimensionRadius() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      PositionShape pointShape = spy(new PositionShape(gridElemPos));
      GridElement gridElement = new SimpleGridElement(mock(DefaultGrid.class), gridElemPos, pointShape);

      // When
      gridElement.getDimensionRadius();

      // Then
      verify(pointShape).getDimensionRadius();
   }

   @Test
   void testGetFurthermostBackPosition() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      Position expectedBackPos = Positions.of(gridElemPos);
      expectedBackPos.rotate(180);
      PositionShape pointShape = new PositionShape(gridElemPos);
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
      PositionShape pointShape = new PositionShape(gridElemPos);
      GridElement gridElement = new SimpleGridElement(Mockito.mock(DefaultGrid.class), gridElemPos, pointShape);

      // When
      Position positionOnPathFor = gridElement.getFurthermostFrontPosition();

      // Then
      assertThat(positionOnPathFor, is(gridElemPos));
   }

   @Test
   void testName() {
      // Given
      AbstractGridElement gridElement = new SimpleGridElement(Mockito.mock(DefaultGrid.class), Positions.of(4, 4));
      String expectedName = "name";
      gridElement.setName(expectedName);

      // When
      String actualName = gridElement.getName();

      // Then
      assertThat(actualName, is(expectedName));
   }

   /**
    * Test method for
    * {@link com.myownb3.piranha.grid.gridelement.AbstractGridElement#toString()}.
    */
   @Test
   void testToString() {
      // Given
      Position position = Positions.of(1, 1);
      Grid grid = GridBuilder.builder(5, 5)
            .build();
      AbstractGridElement element = new SimpleGridElement(grid, position);
      String expectedToString =
            "Position: Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '1.0', Y-Axis: '1.0'\nMax x:'5, Min x:'0; Max y:'5, Min y:'0";

      // When
      String toString = element.toString();

      // Then
      assertThat(toString, is(expectedToString));
   }

}

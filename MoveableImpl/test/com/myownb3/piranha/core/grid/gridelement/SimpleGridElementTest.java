/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.DefaultGrid;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * @author Dominic
 *
 */
class SimpleGridElementTest {

   @Test
   void testTransformShapeAfterGridElementCreation() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      Circle circle = CircleBuilder.builder()
            .withRadius(54)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(0, 0))
            .build();

      // When
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withPosition(gridElemPos)
            .withShape(circle)
            .build();

      // Then
      assertThat(circle.getCenter(), is(gridElement.getPosition()));
   }

   @Test
   void testGetDimensionRadius() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      PositionShape pointShape = spy(PositionShapeBuilder.builder()
            .withPosition(gridElemPos)
            .build());
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withPosition(gridElemPos)
            .withShape(pointShape)
            .build();

      // When
      gridElement.getDimensionRadius();

      // Then
      verify(pointShape).getDimensionRadius();
   }

   @Test
   void testVelocity() {
      // Given
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withPosition(Positions.of(4, 4))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(4, 4))
                  .build())
            .build();

      // When
      int actualVelocity = gridElement.getVelocity();

      // Then
      assertThat(actualVelocity, is(0));
   }

   @Test
   void testGetRearmostPosition() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      Position expectedBackPos = gridElemPos.rotate(180);
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withPosition(gridElemPos)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElemPos)
                  .build())
            .build();

      // When
      Position positionOnPathFor = gridElement.getRearmostPosition();

      // Then
      assertThat(positionOnPathFor, is(expectedBackPos));
   }

   @Test
   void testGetForemostPosition() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withPosition(gridElemPos)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElemPos)
                  .build())
            .build();

      // When
      Position positionOnPathFor = gridElement.getForemostPosition();

      // Then
      assertThat(positionOnPathFor, is(gridElemPos));
   }

   @Test
   void testName() {
      // Given
      AbstractGridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withPosition(Positions.of(4, 4))
            .build();
      String expectedName = "name";
      gridElement.setName(expectedName);

      // When
      String actualName = gridElement.getName();

      // Then
      assertThat(actualName, is(expectedName));
   }

   /**
    * Test method for
    * {@link com.myownb3.piranha.core.grid.gridelement.AbstractGridElement#toString()}.
    */
   @Test
   void testToString() {
      // Given
      Position position = Positions.of(1, 1);
      AbstractGridElement element = SimpleGridElementBuilder.builder()
            .withGrid(GridBuilder.builder(5, 5)
                  .build())
            .withPosition(position)
            .build();
      String expectedToString =
            "Position: Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '1.0', Y-Axis: '1.0'\nMax x:'5, Min x:'0; Max y:'5, Min y:'0";

      // When
      String toString = element.toString();

      // Then
      assertThat(toString, is(expectedToString));
   }

}

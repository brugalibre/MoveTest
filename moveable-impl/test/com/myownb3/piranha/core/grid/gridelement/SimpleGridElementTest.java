/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.DefaultGrid;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

/**
 * @author Dominic
 *
 */
class SimpleGridElementTest {

   @Test
   void testGetEmptyPath_OtherDimensionInfoToHigh() {

      //  Given
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(CircleBuilder.builder()
                  .withRadius((int) 5)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(4, 4))
                  .build())
            .build();

      GridElement otherGridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(CircleBuilder.builder()
                  .withRadius((int) 5)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(4, 4, 5000))
                  .build())
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(5)
                  .build())
            .build();

      // When
      List<PathSegment> actualPath = gridElement.getPath(otherGridElement);

      // Then
      assertThat(actualPath, is(Collections.emptyList()));
   }

   @Test
   void testGridElementDimension_VerifyHeightAndDistanceFromGround() {
      // Given
      double radius = 54.0;
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(CircleBuilder.builder()
                  .withRadius((int) radius)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(4, 4))
                  .build())
            .build();

      // When
      DimensionInfo dimension = gridElement.getDimensionInfo();

      // Then
      assertThat(dimension.getDimensionRadius(), is(radius));
   }

   @Test
   void testGridElementDimension_VerifyDimensionRadius() {
      // Given
      double radius = 54.0;
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(CircleBuilder.builder()
                  .withRadius((int) radius)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(4, 4))
                  .build())
            .build();

      // When
      DimensionInfo dimension = gridElement.getDimensionInfo();

      // Then
      assertThat(dimension.getDimensionRadius(), is(radius));
   }

   @Test
   void testTransformShapeAfterGridElementCreation() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      Circle circle = CircleBuilder.builder()
            .withRadius(54)
            .withAmountOfPoints(4)
            .withCenter(gridElemPos)
            .build();

      // When
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(circle)
            .build();

      // Then
      assertThat(circle.getCenter(), is(gridElement.getPosition()));
   }

   @Test
   void testGetDimensionInfo() {
      // Given
      Position gridElemPos = Positions.of(4, 4);
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElemPos)
                  .build())
            .build();

      // When
      double actualDimensionRadius = gridElement.getDimensionInfo().getDimensionRadius();

      // Then
      assertThat(actualDimensionRadius, is(gridElement.getShape().getDimensionRadius()));
   }

   @Test
   void testVelocity() {
      // Given
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(4, 4))
                  .build())
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(position)
                  .build())
            .build();
      String expectedToString =
            "Position: Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '1.0', Y-Axis: '1.0', Z-Axis: '0.0'";

      // When
      String toString = element.toString();

      // Then
      assertThat(toString, is(expectedToString));
   }

}

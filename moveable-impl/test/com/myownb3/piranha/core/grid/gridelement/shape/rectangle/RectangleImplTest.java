package com.myownb3.piranha.core.grid.gridelement.shape.rectangle;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;

public class RectangleImplTest {

   @Test
   void testDimension() {

      // Given
      Position center = Positions.of(2, 2);
      double height = 10;
      double width = 20;

      // When
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.VERTICAL);

      // Then
      assertThat(rectangle.getHeight(), is(height));
      assertThat(rectangle.getWidth(), is(width));
   }

   @Test
   void testGetPath4Detection() {

      // Given
      GridElement gridElement = mock(GridElement.class);
      Rectangle rectangle = buildRectangleWithGridElement(Positions.of(0, 0), 10, 20, Orientation.HORIZONTAL, gridElement);
      List<Position> expectedPath = getExpectedPath(rectangle);
      Detector detector = spy(DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build());
      Position detectorPosition = Positions.of(0, 0);

      // We must not enter the real detectObjectAlongPath because the detector sorts the incoming path-elements, so our verify statement would never be true!
      doNothing().when(detector).detectObjectAlongPath(eq(gridElement), eq(expectedPath), eq(detectorPosition));

      // When
      rectangle.detectObject(detectorPosition, detector);

      // Then
      verify(detector).detectObjectAlongPath(eq(gridElement), eq(expectedPath), eq(detectorPosition));
   }

   protected Rectangle buildRectangleWithGridElement(Position center, int height, int width, Orientation orientation, GridElement gridElement) {
      Rectangle rectangle = buildRectangle(center, height, width, orientation);
      ((AbstractShape) rectangle).setGridElement(gridElement);
      return rectangle;
   }

   private static List<Position> getExpectedPath(Rectangle rectangle) {
      return Arrays.asList(
            Positions.of(10, 5),
            Positions.of(9, 5),
            Positions.of(8, 5),
            Positions.of(7, 5),
            Positions.of(6, 5),
            Positions.of(5, 5),
            Positions.of(4, 5),
            Positions.of(3, 5),
            Positions.of(2, 5),
            Positions.of(1, 5),
            Positions.of(0, 5),
            Positions.of(-1, 5),
            Positions.of(-2, 5),
            Positions.of(-3, 5),
            Positions.of(-4, 5),
            Positions.of(-5, 5),
            Positions.of(-6, 5),
            Positions.of(-7, 5),
            Positions.of(-8, 5),
            Positions.of(-9, 5),
            Positions.of(-10, 5),
            Positions.of(-10, 4),
            Positions.of(-10, 3),
            Positions.of(-10, 2),
            Positions.of(-10, 1),
            Positions.of(-10, 0),
            Positions.of(-10, -1),
            Positions.of(-10, -2),
            Positions.of(-10, -3),
            Positions.of(-10, -4),
            Positions.of(-10, -5),
            Positions.of(-9, -5),
            Positions.of(-8, -5),
            Positions.of(-7, -5),
            Positions.of(-6, -5),
            Positions.of(-5, -5),
            Positions.of(-4, -5),
            Positions.of(-3, -5),
            Positions.of(-2, -5),
            Positions.of(-1, -5),
            Positions.of(0, -5),
            Positions.of(1, -5),
            Positions.of(2, -5),
            Positions.of(3, -5),
            Positions.of(4, -5),
            Positions.of(5, -5),
            Positions.of(6, -5),
            Positions.of(7, -5),
            Positions.of(8, -5),
            Positions.of(9, -5),
            Positions.of(10, -5),
            Positions.of(10, -4),
            Positions.of(10, -3),
            Positions.of(10, -2),
            Positions.of(10, -1),
            Positions.of(10, 0),
            Positions.of(10, 1),
            Positions.of(10, 2),
            Positions.of(10, 3),
            Positions.of(10, 4));
   }

   @Test
   void testBuildPath4Detection_Vertical() {

      // Given
      List<Position> expectedPath = Arrays.asList(Positions.of(5, 10),
            Positions.of(-5, 10),
            Positions.of(-5, -10),
            Positions.of(5, -10));
      Position center = Positions.of(0, 0);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.VERTICAL);

      // When
      List<Position> actualPath = getPositionsOfPath(rectangle);

      // Then
      assertPath(actualPath, expectedPath);
   }

   @Test
   void testBuildPath4Detection_BuildButNoValidOrientation() {

      // Given
      Position center = Positions.of(0, 0);
      double height = 10;
      double width = 20;

      // When
      Executable executable = () -> {
         buildRectangle(center, height, width, Orientation.NONE);
      };
      // Then
      assertThrows(IllegalStateException.class, executable);
   }

   @Test
   void testBuildPath4Detection_Horizontal() {

      // Given
      List<Position> expectedPath = Arrays.asList(Positions.of(10, 5),
            Positions.of(-10, 5),
            Positions.of(-10, -5),
            Positions.of(10, -5));
      Position center = Positions.of(0, 0);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.HORIZONTAL);

      // When
      List<Position> actualPath = getPositionsOfPath(rectangle);

      // Then
      assertPath(actualPath, expectedPath);
   }

   @Test
   void testBuildPath4Detection_TransformHorizontal() {

      // Given
      List<Position> expectedPath = Arrays.asList(Positions.of(15, 10),
            Positions.of(-5, 10),
            Positions.of(-5, 0),
            Positions.of(15, 0));
      Position center = Positions.of(0, 0);
      Position newPos = Positions.of(5, 5);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.HORIZONTAL);

      // When
      rectangle.transform(newPos);
      List<Position> actualPath = getPositionsOfPath(rectangle);

      // Then
      assertPath(actualPath, expectedPath);
   }

   private List<Position> getPositionsOfPath(Rectangle rectangle) {
      return rectangle.getPath()
            .stream()
            .map(PathSegment::getBegin)
            .collect(Collectors.toList());
   }

   @Test
   void testGetForemostPosition_Horizontal() {

      // Given
      Position expectedForemostPos = Positions.of(0, 5);
      Position center = Positions.of(0, 0);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.HORIZONTAL);

      // When
      Position actualForemostPos = rectangle.getForemostPosition();

      // Then
      assertThat(actualForemostPos, is(expectedForemostPos));
   }

   @Test
   void testGetRearmostPosition_Horizontal() {

      // Given
      Position expectedForemostPos = Positions.of(0, -5);
      Position center = Positions.of(0, 0);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.HORIZONTAL);

      // When
      Position actualForemostPos = rectangle.getRearmostPosition();

      // Then
      assertThat(actualForemostPos, is(expectedForemostPos));
   }

   @Test
   void testGetForemostPosition_Vertical() {

      // Given
      Position expectedForemostPos = Positions.of(2, 12);
      Position center = Positions.of(2, 2);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.VERTICAL);

      // When
      Position actualForemostPos = rectangle.getForemostPosition();

      // Then
      assertThat(actualForemostPos, is(expectedForemostPos));
   }

   @Test
   void testGetUpperLeftPositionHorizontal() {

      // Given
      Position expectedUpperLeftPosition = Positions.of(-10, 5);
      Position center = Positions.of(0, 0);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.HORIZONTAL);

      // When
      Position actualUpperLeftPosition = rectangle.getUpperLeftPosition();

      // Then
      assertThat(actualUpperLeftPosition, is(expectedUpperLeftPosition));
   }

   @Test
   void testGetUpperLeftPositionVertical() {

      // Given
      Position expectedUpperLeftPosition = Positions.of(-5, 10);
      Position center = Positions.of(0, 0);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.VERTICAL);

      // When
      Position actualUpperLeftPosition = rectangle.getUpperLeftPosition();

      // Then
      assertThat(actualUpperLeftPosition, is(expectedUpperLeftPosition));
   }

   @Test
   void testGetForemostPosition_Invalid() {

      // Given
      Position center = Positions.of(2, 2);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.HORIZONTAL);
      setOrientation2None(rectangle);

      // When
      Executable executable = () -> {
         rectangle.getForemostPosition();
      };
      // Then
      assertThrows(IllegalStateException.class, executable);
   }

   @Test
   void testDimensionRadius() {

      // Given
      Position center = Positions.of(2, 2);
      double height = 10;
      double width = 20;
      Rectangle rectangle = buildRectangle(center, height, width, Orientation.VERTICAL);

      // When
      double dimensionRadius = rectangle.getDimensionRadius();

      // Then
      assertThat(dimensionRadius, is(width));
   }

   private void assertPath(List<Position> actualPath, List<Position> expectedPath) {
      for (int i = 0; i < actualPath.size(); i++) {
         Position actualPathPosition = actualPath.get(i);
         Position expectedPathPos = expectedPath.get(i);
         assertThat(actualPathPosition.getX(), is(expectedPathPos.getX()));
         assertThat(actualPathPosition.getY(), is(expectedPathPos.getY()));
      }
   }

   private static Rectangle buildRectangle(Position center, double height, double width, Orientation orientation) {
      return RectangleBuilder.builder()
            .withCenter(center)
            .withWidth(width)
            .withHeight(height)
            .withOrientation(orientation)
            .withDistanceBetweenPosOnColDetectionPath(1)
            .build();
   }

   private static void setOrientation2None(Rectangle rectangle) {
      try {
         Field field = RectangleImpl.class.getDeclaredField("orientation");
         field.setAccessible(true);
         field.set(rectangle, Orientation.NONE);
      } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
         fail(e);
      }
   }
}

package com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl.RectanglePathBuilderImpl.RectangleSides;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class RectanglePathBuilderImplTest {
   @Test
   void testBuildPath_WithInvalidRectangleSides() {

      // Given
      RectangleSides none = RectangleSides.NONE;

      // When
      Executable executable = () -> {
         new RectanglePathBuilderImpl(10, none).buildRectanglePath(Positions.of(5, 5), Orientation.HORIZONTAL, 5, 5);
      };

      // Then
      assertThrows(IllegalStateException.class, executable);
   }

   @Test
   void testBuildPathWithOrientationHorizontal() {

      // Given
      Position center = Positions.of(0, 0);
      int amountOfPoints = 10;
      int width = 10;
      int height = 6;
      RectanglePathBuilderImpl rectanglePathBuilderImpl = new RectanglePathBuilderImpl(amountOfPoints, RectangleSides.FRONT_AND_BACK);

      // When
      List<PathSegment> actualRectangleShape = rectanglePathBuilderImpl.buildRectanglePath(center, Orientation.HORIZONTAL, width, height);

      // Then
      assertPath(actualRectangleShape, getExpectedPathSegmentsHorizontal());
   }

   @Test
   void testBuildPathWithOrientationVertical() {

      // Given
      Position center = Positions.of(0, 0);
      int width = 10;
      int amountOfPoints = 10;
      int height = 6;
      RectanglePathBuilderImpl rectanglePathBuilderImpl = new RectanglePathBuilderImpl(amountOfPoints, RectangleSides.FRONT_AND_BACK);

      // When
      List<PathSegment> actualRectangleShape = rectanglePathBuilderImpl.buildRectanglePath(center, Orientation.VERTICAL, width, height);

      // Then
      assertPath(actualRectangleShape, getExpectedPathSegmentsVertical());
   }

   private static List<PathSegment> getExpectedPathSegmentsVertical() {
      return Arrays.asList(
            new PathSegmentImpl(Positions.of(3, 5, 0), Positions.of(2, 5, 0)),
            new PathSegmentImpl(Positions.of(2, 5, 0), Positions.of(1, 5, 0)),
            new PathSegmentImpl(Positions.of(1, 5, 0), Positions.of(0, 5, 0)),
            new PathSegmentImpl(Positions.of(0, 5, 0), Positions.of(-1, 5, 0)),
            new PathSegmentImpl(Positions.of(-1, 5, 0), Positions.of(-2, 5, 0)),
            new PathSegmentImpl(Positions.of(-2, 5, 0), Positions.of(-3, 5, 0)),

            new PathSegmentImpl(Positions.of(-3, 5, 0), Positions.of(-3, -5, 0)),

            new PathSegmentImpl(Positions.of(-3, -5, 0), Positions.of(-2, -5, 0)),
            new PathSegmentImpl(Positions.of(-2, -5, 0), Positions.of(-1, -5, 0)),
            new PathSegmentImpl(Positions.of(-1, -5, 0), Positions.of(0, -5, 0)),
            new PathSegmentImpl(Positions.of(0, -5, 0), Positions.of(1, -5, 0)),
            new PathSegmentImpl(Positions.of(1, -5, 0), Positions.of(2, -5, 0)),
            new PathSegmentImpl(Positions.of(2, -5, 0), Positions.of(3, -5, 0)),

            new PathSegmentImpl(Positions.of(3, -5, 0), Positions.of(3, 5, 0)));
   }

   private static List<PathSegment> getExpectedPathSegmentsHorizontal() {
      return Arrays.asList(
            new PathSegmentImpl(Positions.of(5, 3, 0), Positions.of(4, 3, 0)),
            new PathSegmentImpl(Positions.of(4, 3, 0), Positions.of(3, 3, 0)),
            new PathSegmentImpl(Positions.of(3, 3, 0), Positions.of(2, 3, 0)),
            new PathSegmentImpl(Positions.of(2, 3, 0), Positions.of(1, 3, 0)),
            new PathSegmentImpl(Positions.of(1, 3, 0), Positions.of(0, 3, 0)),
            new PathSegmentImpl(Positions.of(0, 3, 0), Positions.of(-1, 3, 0)),
            new PathSegmentImpl(Positions.of(-1, 3, 0), Positions.of(-2, 3, 0)),
            new PathSegmentImpl(Positions.of(-2, 3, 0), Positions.of(-3, 3, 0)),
            new PathSegmentImpl(Positions.of(-3, 3, 0), Positions.of(-4, 3, 0)),
            new PathSegmentImpl(Positions.of(-4, 3, 0), Positions.of(-5, 3, 0)),
            new PathSegmentImpl(Positions.of(-5, 3, 0), Positions.of(-5, -3, 0)),
            new PathSegmentImpl(Positions.of(-5, -3, 0), Positions.of(4, -3, 0)),
            new PathSegmentImpl(Positions.of(-4, -3, 0), Positions.of(-3, -3, 0)),
            new PathSegmentImpl(Positions.of(-3, -3, 0), Positions.of(-2, -3, 0)),
            new PathSegmentImpl(Positions.of(-2, -3, 0), Positions.of(-1, -3, 0)),
            new PathSegmentImpl(Positions.of(-1, -3, 0), Positions.of(0, -3, 0)),
            new PathSegmentImpl(Positions.of(0, -3, 0), Positions.of(1, -3, 0)),
            new PathSegmentImpl(Positions.of(1, -3, 0), Positions.of(2, -3, 0)),
            new PathSegmentImpl(Positions.of(2, -3, 0), Positions.of(3, -3, 0)),
            new PathSegmentImpl(Positions.of(3, -3, 0), Positions.of(4, -3, 0)),
            new PathSegmentImpl(Positions.of(4, -3, 0), Positions.of(5, -3, 0)),

            new PathSegmentImpl(Positions.of(5, -3, 0), Positions.of(5, 3, 0)));
   }

   private void assertPath(List<PathSegment> actualPath, List<PathSegment> expectedPath) {
      for (int i = 0; i < actualPath.size(); i++) {
         PathSegment actualPathSegment = actualPath.get(i);
         PathSegment expectedPathSegment = expectedPath.get(i);
         Position actualBegin = actualPathSegment.getBegin();
         Position actualEnd = actualPathSegment.getEnd();
         Position expectedBegin = expectedPathSegment.getBegin();
         Position expectedEnd = expectedPathSegment.getEnd();
         assertThat(actualBegin.getX(), is(expectedBegin.getX()));
         assertThat(actualEnd.getY(), is(expectedEnd.getY()));
      }
   }
}

/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.grid.gridelement.shape.CircleImpl.CircleBuilder;

/**
 * @author Dominic
 *
 */
class CircleImplTest {

   @Test
   void testGetPositionOnPathFor() {
      // Given
      int amountOfPoints = 4;
      int radius = 5;
      Position center = Positions.of(0, 0);
      center.rotate(45);
      Circle circle = new CircleBuilder(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(center)
            .build();

      // When
      Position positionOnPathFor = circle.getPositionOnPathFor(center);

      // Then
      assertThat(positionOnPathFor.getDirection(), is(center.getDirection()));
   }

   @Test
   void test_CreateCircle_CreateCircleAndTransform() {

      // Given
      int amountOfPoints = 4;
      int radius = 5;
      int x = 0;
      int y = 0;
      int newX = 1;
      int newY = 1;
      Position endPos = Positions.of(newX, newY);
      Circle circle = new CircleBuilder(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(Positions.of(x, y))
            .build();
      List<Position> oldPath = circle.getPath()
            .stream()
            .map(Positions::of)
            .collect(Collectors.toList());

      // When
      circle.transform(endPos);

      // Then
      List<Position> path = circle.getPath();
      Collections.sort(path, new CircePathPositionComparator());
      Collections.sort(oldPath, new CircePathPositionComparator());

      assertAngle(amountOfPoints, path);
      assertThat(path.size(), is(amountOfPoints));
      //	assertCoordinates(newX - x, oldPath, path);
   }

   //    private static void assertCoordinates(int distance, List<Position> oldPath, List<Position> path) {
   //	for (Position newPathPos : path) {
   //	    for (Position olPathPos : oldPath) {
   //		double distanceFromOldToNew = newPathPos.calcDistanceTo(olPathPos);
   ////		assertThat(distanceFromOldToNew, is (1));
   //		break;
   //	    }
   //	}
   //    }

   @Test
   void test_CreateCircle_4Points() {

      // Given
      int amountOfPoints = 4;
      int expectedOfPoints = 4;
      int radius = 5;
      Position center = Positions.of(0, 0);

      // When
      Circle circle = new CircleBuilder(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(center)
            .build();

      // Then
      List<Position> path = circle.getPath();
      assertThat(path.size(), is(expectedOfPoints));
      Collections.shuffle(path);
      Collections.sort(path, new CircePathPositionComparator());

      assertThat(circle.getRadius(), is(radius));
      assertThat(circle.getCenter(), is(center));
      assertAngle(amountOfPoints, path);
   }

   private static void assertAngle(int amountOfPoints, List<Position> path) {
      double expectedAngle = 0;
      double angleInc = 360 / amountOfPoints;
      for (Position pathPos : path) {
         assertThat(pathPos.getDirection().getAngle(), is(expectedAngle));
         expectedAngle = expectedAngle + angleInc;
      }
   }

   @Test
   void test_CreateCircle_ToLessPoints() {

      // Given
      int amountOfPoints = 3;
      int radius = 5;
      Position center = Positions.of(0, 0);

      // When
      Executable ex = () -> {
         new CircleBuilder(radius)
               .withAmountOfPoints(amountOfPoints)
               .withCenter(center)
               .build();
      };
      // Then
      assertThrows(IllegalArgumentException.class, ex);
   }

   private class CircePathPositionComparator implements Comparator<Position> {

      @Override
      public int compare(Position pos1, Position pos2) {
         Direction dir1 = pos1.getDirection();
         Direction dir2 = pos2.getDirection();
         Double angleAsDouble = dir1.getAngle();
         return angleAsDouble.compareTo(dir2.getAngle());
      }

   }
}

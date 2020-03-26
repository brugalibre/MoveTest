/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.detector.collision.CollisionDetectedException;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.detector.collision.CollisionDetectorImpl.CollisionDetectorBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.CircleImpl.CircleBuilder;

/**
 * @author Dominic
 *
 */
class CircleImplTest {

   @Test
   void testCheck4Collision_CheckPath() {

      // Given
      int radius = 5;

      // When
      Circle circle = new CircleBuilder(radius)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(Directions.N, 0, 0))
            .build();

      // Then
      List<Position> path = new ArrayList<>(circle.getPath());
      Collections.sort(path, new CircePathPositionComparator());
      // Degree = 0
      assertThat(path.get(0).getX(), is(5.0d));
      assertThat(path.get(0).getY(), is(0.0d));

      // Degree = 90
      assertThat(path.get(1).getX(), is(0.0d));
      assertThat(path.get(1).getY(), is(5.0d));

      // Degree = 180
      assertThat(path.get(2).getX(), is(-5.0d));
      assertThat(path.get(2).getY(), is(0.0d));

      // Degree = 270
      assertThat(path.get(3).getX(), is(0.0d));
      assertThat(path.get(3).getY(), is(-5.0d));
   }

   @Test
   void testCheck4Collision_CheckRadius() {

      // Given
      int radius = 5;
      int expectedDistance = radius;
      Circle circle = new CircleBuilder(radius)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(0, 0))
            .build();

      // When
      Position center = circle.getCenter();
      Position firstPathPos = circle.getPath().get(0);
      int distanceFromCenterToPath = (int) center.calcDistanceTo(firstPathPos);

      // Then
      assertThat(distanceFromCenterToPath, is(expectedDistance));
   }

   @Test
   void testCheck4Collision_NoMatch() {

      // Given
      Position newPosition = Positions.of(5, 5);
      newPosition.rotate(11);
      Circle circle = new CircleBuilder(5)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(0, 0))
            .build();

      // When
      Executable ex = () -> {
         circle.check4Collision(mock(CollisionDetector.class), newPosition, singletonList(mock(Avoidable.class)));
      };

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   @Test
   void testCheck4Collision_Collision() {

      // Given
      Position newPosition = Positions.of(0, 1);
      CollisionDetector collisionDetector = CollisionDetectorBuilder.builder()
            .withCollisionDistance(2)
            .withDefaultCollisionHandler()
            .build();

      ObstacleImpl avoidable = new ObstacleImpl(mock(Grid.class), Positions.of(0, 6));
      Circle circle = new CircleBuilder(5)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(0, 0))
            .build();

      // When
      Executable ex = () -> {
         circle.check4Collision(collisionDetector, newPosition, singletonList(avoidable));
      };

      // Then
      assertThrows(CollisionDetectedException.class, ex);
   }

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
   void testGetPositionOnPathForReverted() {
      // Given
      int amountOfPoints = 4;
      int radius = 5;
      Position center = Positions.of(0, 0);
      Circle circle = new CircleBuilder(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(center)
            .build();

      // When
      center.rotate(167);
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
      List<Position> path = new ArrayList<>(circle.getPath());
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
      List<Position> path = new ArrayList<>(circle.getPath());
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
}

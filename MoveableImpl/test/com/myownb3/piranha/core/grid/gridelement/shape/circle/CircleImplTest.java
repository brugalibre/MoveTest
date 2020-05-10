/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape.circle;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * @author Dominic
 *
 */
class CircleImplTest {

   @Test
   void testDetectObject_NoDetection() {

      // Given
      boolean expectedHasDetection = false;
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Position detectorPosition = Positions.of(0, 6);
      Moveable moveable = mock(Moveable.class);
      Circle circle = CircleBuilder.builder()
            .withRadius(5)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(0, 0))
            .withGridElement(moveable)
            .build();

      // When
      boolean actualHasDetection = circle.detectObject(detectorPosition, detector);

      // Then
      assertThat(actualHasDetection, is(expectedHasDetection));
   }

   @Test
   void testDetectObject_WithDetectionAndEvasion() {

      // Given
      boolean expectedHasDetection = true;
      boolean expectedIsEvasion = true;
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Position detectorPosition = Positions.of(0, -6);
      Obstacle obstacle = mock(Obstacle.class);
      when(obstacle.isAvoidable()).thenReturn(true);
      Circle circle = CircleBuilder.builder()
            .withRadius(5)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(0, 0))
            .withGridElement(obstacle)
            .build();

      // When
      circle.detectObject(detectorPosition, detector);
      boolean actualHasDetection = detector.hasObjectDetected(obstacle);
      boolean actualIsEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(actualHasDetection, is(expectedHasDetection));
      assertThat(actualIsEvasion, is(expectedIsEvasion));
   }

   @Test
   void testCheckPathOfCircle() {

      // Given
      int radius = 5;

      // When
      Circle circle = CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(Directions.N, 0, 0))
            .build();

      // Then
      List<Position> path = circle.getPath()
            .stream()
            .map(PathSegment::getBegin)
            .collect(Collectors.toList());
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
      Circle circle = CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(0, 0))
            .build();

      // When
      Position center = circle.getCenter();
      Position firstPathPos = circle.getPath()
            .stream()
            .map(PathSegment::getBegin)
            .collect(Collectors.toList())
            .get(0);
      int distanceFromCenterToPath = (int) center.calcDistanceTo(firstPathPos);

      // Then
      assertThat(distanceFromCenterToPath, is(expectedDistance));
   }

   @Test
   void testGetFurthermostFrontPosition() {
      // Given
      int amountOfPoints = 4;
      int radius = 5;
      Position center = Positions.of(0, 0).rotate(45);
      Circle circle = CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(center)
            .build();

      // When
      Position positionOnPathFor = circle.getFurthermostFrontPosition();

      // Then
      assertThat(positionOnPathFor.getDirection(), is(center.getDirection()));
   }

   @Test
   void testGetFurthermostBackPosition() {
      // Given
      int amountOfPoints = 4;
      int radius = 5;
      Position center = Positions.of(0, 0).rotate(45);
      Circle circle = CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(center)
            .build();

      // When
      Position positionOnPathFor = circle.getFurthermostBackPosition();

      // Then
      Position centerInverted = center.rotate(180);
      assertThat(positionOnPathFor.getDirection(), is(centerInverted.getDirection()));
   }

   @Test
   void testGetPositionOnPathForReverted() {
      // Given
      int amountOfPoints = 4;
      int radius = 5;
      Position center = Positions.of(0, 0).rotate(167);
      Circle circle = CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(center)
            .build();

      // When
      Position positionOnPathFor = circle.getFurthermostFrontPosition();

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
      Circle circle = CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(Positions.of(x, y))
            .build();
      List<Position> oldPath = circle.getPath()
            .stream()
            .map(PathSegment::getBegin)
            .map(Positions::of)
            .collect(Collectors.toList());

      // When
      circle.transform(endPos);

      // Then
      List<Position> path = circle.getPath()
            .stream()
            .map(PathSegment::getBegin)
            .collect(Collectors.toList());
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
      Circle circle = CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(amountOfPoints)
            .withCenter(center)
            .build();

      // Then
      List<Position> path = circle.getPath()
            .stream()
            .map(PathSegment::getBegin)
            .collect(Collectors.toList());
      assertThat(path.size(), is(expectedOfPoints));
      Collections.shuffle(path);
      Collections.sort(path, new CircePathPositionComparator());

      assertThat(circle.getRadius(), is(radius));
      assertThat(circle.getCenter(), is(center));
      assertThat(circle.getDimensionRadius(), is(5.0));
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
         CircleBuilder.builder()
               .withRadius(radius)
               .withAmountOfPoints(amountOfPoints)
               .withCenter(center)
               .build();
      };
      // Then
      assertThrows(IllegalArgumentException.class, ex);
   }
}

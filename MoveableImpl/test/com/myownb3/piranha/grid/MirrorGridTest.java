/**
 * 
 */
package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.test.Assert;

/**
 * @author Dominic
 *
 */
class MirrorGridTest {

   @Test
   public void testMirror1Quadrant_Circle() {

      // Given
      int radius = 5;
      double forwardX = 0.0d;
      double forwardY = 1d;
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(0)
            .build();
      Direction direction = mockDirection(forwardX, forwardY);
      Position position = Positions.of(direction, 9.5, 8.5);
      double expectedRotationDeg = 180;

      Position expectedPosition = Positions.of(Directions.N, 9.5, 10.0);
      GridElement gridElement = buildGridElement(grid, position, buildCircle(position, radius));

      // When
      Position createdPosition = grid.moveForward(gridElement);

      // Then
      Assert.assertThatPosition(createdPosition, is(expectedPosition), 3);
      Mockito.verify(direction).rotate(expectedRotationDeg);
   }

   private static Direction mockDirection(double forwardX, double forwardY) {
      Direction direction = spy(Direction.class);
      when(direction.getAngle()).thenReturn(90d);
      when(direction.getForwardX()).thenReturn(forwardX);
      when(direction.getForwardY()).thenReturn(forwardY);

      return direction;
   }

   @Test
   public void testMirror1Quadrant_45_X() {

      // Given
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(20)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(0)
            .build();
      Position position = Positions.of(Directions.N, 0, 0);
      Position position2 = Positions.of(Directions.N, 6, 0);
      position.rotate(-45);
      position2.rotate(-45);
      double expectedEndDegree = 135;

      testMirrorInternal(grid, position, position2, expectedEndDegree);
   }

   /**
    * @param grid
    * @param position
    * @param position2
    * @param expectedEndDegree
    */
   private void testMirrorInternal(Grid grid, Position position, Position position2, double expectedEndDegree) {
      // When
      for (int i = 0; i < 150; i++) {
         position = grid.moveForward(buildGridElement(grid, position));
         position2 = grid.moveForward(buildGridElement(grid, position2));
      }
      double actualEndDegree = position.getDirection().getAngle();
      double actualEndDegree2 = position2.getDirection().getAngle();

      // Then
      assertThat(actualEndDegree, is(expectedEndDegree));
      assertThat(actualEndDegree2, is(expectedEndDegree));
   }

   private static GridElement buildGridElement(Grid grid, Position position) {
      return new SimpleGridElement(grid, position);
   }

   @Test
   public void testMirror2Quadrant_45_X() {

      // Given
      Grid grid = MirrorGridBuilder.builder(15, 20)
            .build();
      Position position = Positions.of(Directions.N, 0, 0);
      Position position2 = Positions.of(Directions.N, 4, 0);
      position.rotate(45);
      position2.rotate(45);
      double expectedEndDegree = 45;

      testMirrorInternal(grid, position, position2, expectedEndDegree);
   }

   @Test
   public void testMirror3Quadrant_45_X() {

      // Given
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(20)
            .withMaxY(15)
            .withMinX(0)
            .withMinY(-20)
            .build();
      Position position = Positions.of(Directions.W, 0, 0);
      Position position2 = Positions.of(Directions.W, 6.5, 0);
      position.rotate(45);
      position2.rotate(45);
      double expectedEndDegree = 315;

      testMirrorInternal(grid, position, position2, expectedEndDegree);
   }

   @Test
   public void testMirror4Quadrant_45_X() {

      // Given
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(20)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(-20)
            .build();
      Position position = Positions.of(Directions.S, 0, 0);
      Position position2 = Positions.of(Directions.S, 7.4, 0);
      position.rotate(45);
      position2.rotate(45);
      double expectedEndDegree = 225;

      testMirrorInternal(grid, position, position2, expectedEndDegree);
   }

   @Test
   public void testMirror1Quadrant_45_Y() {

      // Given
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(200)
            .withMinX(-200)
            .withMinY(0)
            .build();
      Position position = Positions.of(Directions.N, 0, 0);
      Position position2 = Positions.of(Directions.N, 7.4, 0);
      position.rotate(-45);
      position2.rotate(-45);
      double expectedEndDegree = 315;

      testMirrorInternal(grid, position, position2, expectedEndDegree);
   }

   @Test
   public void testMirror2Quadrant_45_Y() {

      // Given
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(200)
            .withMinX(-200)
            .withMinY(0)
            .build();
      Position position = Positions.of(Directions.N, 0, 0);
      Position position2 = Positions.of(Directions.N, 4, 0);
      position.rotate(45);
      position2.rotate(45);
      double expectedEndDegree = 225;

      testMirrorInternal(grid, position, position2, expectedEndDegree);
   }

   @Test
   public void testMirror3Quadrant_45_Y() {

      // Given
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(15)
            .withMaxY(200)
            .withMinX(-200)
            .withMinY(0)
            .build();
      Position position = Positions.of(Directions.W, 0, 0);
      Position position2 = Positions.of(Directions.W, 6.5, 0);
      position.rotate(45);
      position2.rotate(45);
      double expectedEndDegree = 135;

      testMirrorInternal(grid, position, position2, expectedEndDegree);
   }

   @Test
   public void testMirror4Quadrant_45_Y() {

      // Given
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(2010)
            .withMaxY(200)
            .withMinX(-200)
            .withMinY(0)
            .build();
      Position position = Positions.of(Directions.S, 0, 0);
      Position position2 = Positions.of(Directions.S, 7.4, 0);
      position.rotate(45);
      position2.rotate(45);
      double expectedEndDegree = 45;

      testMirrorInternal(grid, position, position2, expectedEndDegree);
   }

   private CircleImpl buildCircle(Position pos, int radius) {
      return CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(4)
            .withCenter(pos)
            .build();
   }

   private static GridElement buildGridElement(Grid grid, Position position, Shape shape) {
      return new SimpleGridElement(grid, position, shape);
   }

}

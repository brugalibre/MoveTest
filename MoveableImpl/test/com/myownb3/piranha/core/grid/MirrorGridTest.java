/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;

/**
 * @author Dominic
 *
 */
class MirrorGridTest {

   @Test
   public void testCollisionWithProjectileOnGridWall() {

      // Given
      double forwardX = 0.0d;
      double forwardY = 1d;
      int radius = 5;
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(0)
            .build();
      Direction direction = mockDirection(forwardX, forwardY);
      Position position = Positions.of(direction, 9.5, 8.5);

      ProjectileGridElement projectile = spy(ProjectileGridElementBuilder.builder()
            .withGrid(grid)
            .withPosition(position)
            .withShape(buildCircle(position, radius))
            .withVelocity(10)
            .build());

      // When
      grid.moveForward(projectile);

      // Then
      verify(projectile).onCollision(any());
   }

   @Test
   public void testMirror1Quadrant_Circle() {

      // Given
      int radius = 5;
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(0)
            .build();
      Position position = Positions.of(9.5, 8.5);
      double expectedRotationDeg = 180;

      Position expectedPosition = Positions.of(9.5, 8.5).rotate(expectedRotationDeg);
      GridElement gridElement = buildGridElement(grid, position, buildCircle(position, radius));

      // When
      Position createdPosition = grid.moveForward(gridElement);

      // Then
      assertThat(createdPosition, is(expectedPosition));
   }

   private static Direction mockDirection(double forwardX, double forwardY) {
      Direction direction = spy(Directions.N);
      when(direction.getAngle()).thenReturn(90d);
      when(direction.getForwardX()).thenReturn(forwardX);
      when(direction.getForwardY()).thenReturn(forwardY);
      doReturn(direction).when(direction).rotate(Mockito.anyDouble());
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
      position = position.rotate(-45);
      position2 = position2.rotate(-45);
      double expectedEndDegree = 225;
      double expectedEndDegreeBackwards = 135;

      testMirrorInternal(grid, position, position2, expectedEndDegree, expectedEndDegreeBackwards);
   }

   private void testMirrorInternal(Grid grid, Position position, Position position2, double expectedEndDegree1, double expectedEndDegreeBackwards) {
      // When
      GridElement gridElementBackwards = buildGridElement(grid, position2.rotate(-180));
      GridElement gridElement = buildGridElement(grid, position);
      for (int i = 0; i < 150; i++) {
         position = grid.moveForward(gridElement);
         position2 = grid.moveBackward(gridElementBackwards);
      }
      double actualEndDegree = position.getDirection().getAngle();
      double actualEndDegree2 = position2.getDirection().getAngle();

      // Then
      assertThat(actualEndDegree, is(expectedEndDegree1));
      assertThat(actualEndDegree2, is(expectedEndDegreeBackwards));
   }

   private static GridElement buildGridElement(Grid grid, Position position) {
      return SimpleGridElementBuilder.builder()
            .withGrid(grid)
            .withPosition(position)
            .build();
   }

   @Test
   public void testMirror2Quadrant_45_X() {

      // Given
      Grid grid = MirrorGridBuilder.builder(15, 20)
            .build();
      Position position = Positions.of(Directions.N, 0, 0);
      Position position2 = Positions.of(Directions.N, 4, 0);
      position = position.rotate(45);
      position2 = position2.rotate(45);
      double expectedEndDegree = 315;
      double expectedEndDegreeBackwards = 45;

      testMirrorInternal(grid, position, position2, expectedEndDegree, expectedEndDegreeBackwards);
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
      position = position.rotate(45);
      position2 = position2.rotate(45);
      double expectedEndDegree = 315;
      double expectedEndDegree2 = 45;

      testMirrorInternal(grid, position, position2, expectedEndDegree, expectedEndDegree2);
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
      position = position.rotate(45);
      position2 = position2.rotate(45);
      double expectedEndDegree = 225;
      double expectedEndDegree2 = 135;

      testMirrorInternal(grid, position, position2, expectedEndDegree, expectedEndDegree2);
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
      position = position.rotate(-45);
      position2 = position2.rotate(-45);
      double expectedEndDegree = 315;
      double expectedEndDegreeBackwards = 135;

      testMirrorInternal(grid, position, position2, expectedEndDegree, expectedEndDegreeBackwards);
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
      position = position.rotate(45);
      position2 = position2.rotate(45);
      double expectedEndDegree = 225;
      double expectedEndDegreeBackwards = 45;

      testMirrorInternal(grid, position, position2, expectedEndDegree, expectedEndDegreeBackwards);
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
      position = position.rotate(45);
      position2 = position2.rotate(45);
      double expectedEndDegree = 135;
      double expectedEndDegreeBackwards = 315;

      testMirrorInternal(grid, position, position2, expectedEndDegree, expectedEndDegreeBackwards);
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
      position = position.rotate(45);
      position2 = position2.rotate(45);
      double expectedEndDegree = 45;
      double expectedEndDegreeBackwards = 225;

      testMirrorInternal(grid, position, position2, expectedEndDegree, expectedEndDegreeBackwards);
   }

   private CircleImpl buildCircle(Position pos, int radius) {
      return CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(4)
            .withCenter(pos)
            .build();
   }

   private static GridElement buildGridElement(Grid grid, Position position, Shape shape) {
      return SimpleGridElementBuilder.builder()
            .withGrid(grid)
            .withPosition(position)
            .withShape(shape)
            .build();
   }

}

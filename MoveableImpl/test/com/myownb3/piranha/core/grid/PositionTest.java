/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.mockStatic;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
@PrepareForTest(Positions.class)
class PositionTest {

   @Test
   public void testRandomPosition() {

      // Given

      // When
      Position pos = Positions.getRandomPosition(new DimensionImpl(0, 0, 5, 5), 5, 5);

      boolean lowerXThanExpected = pos.getX() <= 5;
      boolean lowerYThanExpected = pos.getY() <= 5;

      // Then
      Assert.assertTrue(lowerYThanExpected);
      Assert.assertTrue(lowerXThanExpected);
   }

   @Test
   public void testAngleCalculationFirstQuadrant() {

      // Given
      Position pos = Positions.of(5, 5);
      double expectedAngle = 45;

      // When
      double effectAngle = pos.calcAbsolutAngle();

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testNANAngleDirectionNorth() {

      // Given
      Position pos = Positions.of(0, 0);
      double actualAngle = 90;

      // When
      double expectedAngle = pos.calcAbsolutAngle();

      // Then
      assertThat(actualAngle, is(expectedAngle));
   }

   @Test
   public void testNANAngleDirectionSouth() {

      // Given
      Position pos = Positions.of(Directions.S, 0, 0, 0);
      double actualAngle = 270;

      // When
      double expectedAngle = pos.calcAbsolutAngle();

      // Then
      assertThat(actualAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationSecondQuadrant1() {

      // Given
      Position pos = Positions.of(-5, 5);
      double expectedAngle = 135;

      // When
      double effectAngle = pos.calcAbsolutAngle();

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationSecondQuadrant2() {

      // Given
      Position pos = Positions.of(-1, 7);
      double expectedAngle = 98.13;

      // When
      double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsolutAngle());

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationSecondQuadrant3_YIsZero() {

      // Given
      Position pos = Positions.of(-5, 0);
      double expectedAngle = 180;

      // When
      double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsolutAngle());

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationThirdQuadrant() {

      // Given
      Position pos = Positions.of(-5, -5);
      double expectedAngle = 225;

      // When
      double effectAngle = pos.calcAbsolutAngle();

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationForthQuadrant1() {

      // Given
      Position pos = Positions.of(5, -5);
      double expectedAngle = 315;

      // When
      double effectAngle = pos.calcAbsolutAngle();

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationForthQuadrant2() {

      // Given
      Position pos = Positions.of(1, -7);
      double expectedAngle = 278.13;

      // When
      double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsolutAngle());

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculation_ZeroZeroNorthRelative2Position() {

      // Given
      Position testPos1 = Positions.of(Directions.N, 0, 0, 0);
      Position testPos2 = Positions.of(0, 0).rotate(-45);
      Position pos2ComparteWith = Positions.of(5, 5);
      testAngeCalculationRelative2PositionInternal(testPos1, -45, testPos2, 0, pos2ComparteWith);
   }

   @Test
   public void testAngleCalculation_ZeroZeroWestRelative2Position() {

      // Given
      Position testPos1 = Positions.of(Directions.W, 0, 0, 0);
      Position testPos2 = Positions.of(0, 0).rotate(45);
      Position pos2ComparteWith = Positions.of(-5, 5);
      testAngeCalculationRelative2PositionInternal(testPos1, -45, testPos2, 0, pos2ComparteWith);
   }

   private void testAngeCalculationRelative2PositionInternal(Position testPos1, double expectedAngle2Turn1,
         Position testPos2, double expectedAngle2Turn2, Position pos2ComparteWith) {

      // When
      double effectDeltaAngle1 = MathUtil.roundThreePlaces(testPos1.calcAngleRelativeTo(pos2ComparteWith));
      double effectDeltaAngle2 = MathUtil.roundThreePlaces(testPos2.calcAngleRelativeTo(pos2ComparteWith));

      // Then
      MatcherAssert.assertThat(effectDeltaAngle1, is(expectedAngle2Turn1));
      MatcherAssert.assertThat(effectDeltaAngle2, is(expectedAngle2Turn2));
   }

   @Test
   public void testAngleCalculationRelative2Position_1Quadrant() {

      // Given
      Position testPos1 = Positions.of(Directions.N, 0, 10, 0);
      Position testPos2 = Positions.of(0, 10).rotate(-45);
      Position pos2ComparteWith = Positions.of(5, 15);
      testAngeCalculationRelative2PositionInternal(testPos1, -45, testPos2, 0, pos2ComparteWith);
   }

   @Test
   public void testAngleCalculationRelative2Position_2Quadrant() {

      // Given
      Position testPos1 = getTestPos1();// 243.435

      Position pos2ComparteWith = Positions.of(-10, -5); // 206.565
      double expectedAngle2Turn1 = -108.435;

      // When
      double effectDeltaAngle1 = MathUtil.roundThreePlaces(testPos1.calcAngleRelativeTo(pos2ComparteWith));

      // Then
      MatcherAssert.assertThat(effectDeltaAngle1, is(expectedAngle2Turn1));
   }

   /**
    * Builds a Moveable at S:0:0 and moves to -5;-10. Additionally the moveable is
    * turned, so that it looks into the direction of the angle at -5:-10
    * 
    * @return the Position at the moved coordinates
    */
   private Position getTestPos1() {
      Grid grid = GridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinY(-20)
            .withMinY(-20)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(Directions.S, 0, 0, 0))
                  .build())
            .build();
      moveable.moveForward(100);
      moveable.turnRight();
      moveable.moveForward(50);
      Position position = moveable.getPosition();
      moveable.makeTurn(position.calcAbsolutAngle() - position.getDirection().getAngle());
      return moveable.getPosition();
   }

   @Test
   public void testSubstraction() {

      // Given
      Position startPoint = Positions.of(3, 7);
      Position endPoint = Positions.of(7, 4);
      double expectedDistance = 5;

      // When
      double effectDistance = endPoint.calcDistanceTo(startPoint);
      // Then
      MatcherAssert.assertThat(effectDistance, is(expectedDistance));
   }

   @Test
   public void testSubstraction2() {

      // Given
      Position startPoint = Positions.of(1, 2);
      Position endPoint = Positions.of(6, 3);
      double expectedDistance = 5.099;

      // When
      double effectDistance = endPoint.calcDistanceTo(startPoint);
      // Then
      MatcherAssert.assertThat(MathUtil.roundThreePlaces(effectDistance), is(expectedDistance));
   }

   @Test
   void testHashCode() {

      // Given
      Position pos = Positions.of(Directions.N, 0, 0, 0);

      // When
      Position anotherPos = Positions.of(Directions.N, 0, 0, 0);

      // Then
      MatcherAssert.assertThat(anotherPos.hashCode(), is(pos.hashCode()));
   }

   @Test
   void testEquals() {

      // Given
      Position pos = Positions.of(Directions.N, 0, 0, 5);

      // When
      Position anotherPos = Positions.of(Directions.N, 0, 0, 5);

      // Then
      MatcherAssert.assertThat(pos, is(anotherPos));
      Assert.assertTrue(pos.equals(anotherPos));
      Assert.assertTrue(pos.equals(pos));
   }

   @Test
   void testNotEquals() {

      // Given
      Position pos = Positions.of(Directions.N, 0, 0, 0);

      // When
      Position anotherNotExactlySamePos = Positions.of(Directions.N, 1, 0, 0);
      Position anotherNotExactlySamePos2 = Positions.of(Directions.S, 0, 1, 0);

      // Then
      Assert.assertFalse(pos.equals(null));
      Assert.assertFalse(pos.equals(new Object()));
      Assert.assertFalse(pos.equals(anotherNotExactlySamePos));
      Assert.assertFalse(pos.equals(anotherNotExactlySamePos2));
   }

   @Test
   void testNotEqualsWithZAxis() {

      // Given
      Position pos = Positions.of(Directions.N, 1, 0, 6);

      // When
      Position anotherNotExactlySamePos = Positions.of(Directions.N, 1, 0, 0);

      // Then
      Assert.assertFalse(pos.equals(anotherNotExactlySamePos));
   }

   // @Test
   void testRandomPos() {

      // Given
      int maxWidth = 200;
      int maxHeight = 200;
      mockStatic(MathUtil.class);
      Grid grid = GridBuilder.builder(maxWidth, maxHeight)
            .build();
      int height = 5;
      int width = 5;

      // When
      when(MathUtil.getRandom(Mockito.anyInt())).thenReturn((double) maxWidth);
      Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);

      double expectedXCoordinates = maxWidth;
      double effectXCoordindates = randomPosition.getX();
      // Then
      assertThat(effectXCoordindates, is(expectedXCoordinates));
   }

   @Test
   void testToString() {

      // Given
      Position pos = Positions.of(Directions.N, 0, 0, 0);
      // When
      Position anotherPos = Positions.of(Directions.N, 0, 0, 0);

      // Then
      assertThat(pos.toString(), is(anotherPos.toString()));
   }
}

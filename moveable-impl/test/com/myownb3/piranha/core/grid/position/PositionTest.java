/**
 * 
 */
package com.myownb3.piranha.core.grid.position;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Positions.PositionImpl;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
class PositionTest {


   @Test
   void testRaise() {

      // Given
      Position gridElementPos = Positions.of(-2, 7, 5);
      double newZ = 5;
      double expectedZ = gridElementPos.getZ() + newZ;

      // When
      Position newPos = gridElementPos.raise(newZ);

      // Then
      assertThat(newPos.getZ(), is(expectedZ));
   }

   @Test
   void testCalcAngle_SecondSector() {

      // Given
      double expectedCalcAngleBetweenVectors = 26.565;

      Position gridElementPos = Positions.of(-2, 7);
      Position moveablePosition = Positions.of(1, 1);

      // When
      double actualCalcAngleBetweenVectors = round(moveablePosition.calcAngleBetweenPositions(gridElementPos), 3);

      // Then
      assertThat(actualCalcAngleBetweenVectors, is(expectedCalcAngleBetweenVectors));
   }

   @Test
   void testCalcAngle_FirstAndForthSector() {

      // Given
      double expectedCalcAngleBetweenVectors = 54.462;

      Position gridElementPos = Positions.of(8, -4);
      Position moveablePosition = Positions.of(Directions.S, 1, 1, 0);

      // When
      double actualCalcAngleBetweenVectors = round(moveablePosition.calcAngleBetweenPositions(gridElementPos), 3);

      // Then
      assertThat(actualCalcAngleBetweenVectors, is(expectedCalcAngleBetweenVectors));
   }

   @Test
   void testCalcAngle_ThirdAndForthSector() {

      // Given
      double expectedCalcAngleBetweenVectors = 52.12;

      Position gridElementPos = Positions.of(8, -4);
      Position moveablePosition = Positions.of(Directions.S, -2, -2, 0);
      moveablePosition = moveablePosition.rotate(26.57);// rotate to simulate the direction of a moveable which leads into the 4. sector

      // When
      double actualCalcAngleBetweenVectors = round(moveablePosition.calcAngleBetweenPositions(gridElementPos), 3);

      // Then
      assertThat(actualCalcAngleBetweenVectors, is(expectedCalcAngleBetweenVectors));
   }

   @Test
   public void testAngleCalculationFirstQuadrant() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(5, 5);
      double expectedAngle = 45;

      // When
      double effectAngle = pos.calcAbsoluteAngle();

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testNANAngleDirectionNorth() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(0, 0);
      double actualAngle = 90;

      // When
      double expectedAngle = pos.calcAbsoluteAngle();

      // Then
      assertThat(actualAngle, is(expectedAngle));
   }

   @Test
   public void testNANAngleDirectionSouth() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(Directions.S, 0, 0, 0);
      double actualAngle = 270;

      // When
      double expectedAngle = pos.calcAbsoluteAngle();

      // Then
      assertThat(actualAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationSecondQuadrant1() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(-5, 5);
      double expectedAngle = 135;

      // When
      double effectAngle = pos.calcAbsoluteAngle();

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationSecondQuadrant2() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(-1, 7);
      double expectedAngle = 98.13;

      // When
      double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsoluteAngle());

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationSecondQuadrant3_YIsZero() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(-5, 0);
      double expectedAngle = 180;

      // When
      double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsoluteAngle());

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationThirdQuadrant() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(-5, -5);
      double expectedAngle = 225;

      // When
      double effectAngle = pos.calcAbsoluteAngle();

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationForthQuadrant1() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(5, -5);
      double expectedAngle = 315;

      // When
      double effectAngle = pos.calcAbsoluteAngle();

      // Then
      MatcherAssert.assertThat(effectAngle, is(expectedAngle));
   }

   @Test
   public void testAngleCalculationForthQuadrant2() {

      // Given
      PositionImpl pos = (PositionImpl) Positions.of(1, -7);
      double expectedAngle = 278.13;

      // When
      double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsoluteAngle());

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
            .withVelocity(100)
            .build();
      moveable.moveForward();
      moveable.turnRight();
      moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(moveable.getPosition())
                  .build())
            .withVelocity(50)
            .build();
      moveable.moveForward();
      PositionImpl position = (PositionImpl) moveable.getPosition();
      moveable.makeTurn(position.calcAbsoluteAngle() - position.getDirection().getAngle());
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

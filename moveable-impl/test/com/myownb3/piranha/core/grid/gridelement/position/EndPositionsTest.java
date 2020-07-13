package com.myownb3.piranha.core.grid.gridelement.position;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;

class EndPositionsTest {

   @Test
   void testHashCodeEquals() {

      // Given
      EndPosition endPos1 = EndPositions.of(5, 5);
      EndPosition endPos2 = EndPositions.of(5, 5);

      // When
      int hashCode1 = endPos1.hashCode();
      int hashCode2 = endPos2.hashCode();

      // Then
      assertThat(hashCode1, is(hashCode2));
   }

   @Test
   void testHashCodeNotEquals() {

      // Given
      EndPosition endPos1 = EndPositions.of(5, 5, true);
      EndPosition endPos2 = EndPositions.of(5, 5);

      // When
      int hashCode1 = endPos1.hashCode();
      int hashCode2 = endPos2.hashCode();

      // Then
      assertThat(hashCode1, is(not(hashCode2)));
   }

   @Test
   void testNotEqualsOtherObject() {

      // Given
      EndPosition endPos1 = EndPositions.of(5, 5);
      Position pos = Positions.of(5, 5);

      // When
      boolean isActualEquals = endPos1.equals(pos);

      // Then
      assertThat(isActualEquals, is(false));
   }

   @Test
   void testNotEquals() {

      // Given
      EndPosition endPos1 = EndPositions.of(5, 5);
      EndPosition endPos2 = EndPositions.of(5, 5, true);

      // When
      boolean isActualEquals = endPos1.equals(endPos2);

      // Then
      assertThat(isActualEquals, is(false));
   }

   @Test
   void testNotEqualsDifferentPrecision() {

      // Given
      Position pos = Positions.of(5, 5);
      EndPosition endPos1 = EndPositions.of(pos, 6);
      EndPosition endPos2 = EndPositions.of(pos, 1);

      // When
      boolean isActualEquals = endPos1.equals(endPos2);

      // Then
      assertThat(isActualEquals, is(false));
   }

   @Test
   void testEqualsSameObject() {

      // Given
      EndPosition endPos1 = EndPositions.of(5, 5);

      // When
      boolean isActualEquals = endPos1.equals(endPos1);

      // Then
      assertThat(isActualEquals, is(true));
   }

   @Test
   void testEquals() {

      // Given
      EndPosition endPos1 = EndPositions.of(5, 5);
      EndPosition endPos2 = EndPositions.of(5, 5);

      // When
      boolean isActualEquals = endPos1.equals(endPos2);

      // Then
      assertThat(isActualEquals, is(true));
   }

   @Test
   void test_HasNotReached_NotInMap() {

      // Given
      EndPosition endPos = EndPositions.of(5, 5);
      Position pos = Positions.of(5, 5);
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withVelocity(1)
            .build();

      // When
      boolean actualHasReached = endPos.hasReached(moveable);

      // Then
      assertThat(actualHasReached, is(false));
   }

   @Test
   void test_HasNotReached_InMapButNotReached() {

      // Given
      EndPosition endPos = EndPositions.of(50, 50);
      Position pos = Positions.of(5, 5);
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withVelocity(1)
            .build();

      // When
      endPos.checkIfHasReached(moveable);
      boolean actualHasReached = endPos.hasReached(moveable);

      // Then
      assertThat(actualHasReached, is(false));
   }

   @Test
   void test_HasReached_InMapAndReached() {

      // Given
      EndPosition endPos = EndPositions.of(5, 5);
      Position pos = Positions.of(5, 5);
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withVelocity(1)
            .build();

      // When
      endPos.checkIfHasReached(moveable);
      boolean actualHasReached = endPos.hasReached(moveable);

      // Then
      assertThat(actualHasReached, is(true));
   }

}

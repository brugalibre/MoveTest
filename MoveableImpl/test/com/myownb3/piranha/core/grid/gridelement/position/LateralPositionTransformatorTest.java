package com.myownb3.piranha.core.grid.gridelement.position;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.position.Position;

class VectorPositionTransformatorTest {

   @Test
   void testTransform_FirstQuadrant() {

      // Given
      Position sourcePos = Positions.of(0, 0);
      Position dependingPos = Positions.of(2, 2).rotate(-45);
      Position expectedTransformedDepPos = Positions.of(2, 5);
      VectorPositionTransformator lateralPositionTransformator = VectorPositionTransformator.of(sourcePos, dependingPos);

      // When
      Position movedSourcePos = Positions.movePositionForward4Distance(sourcePos, 3);
      Position actualTransformedDepPos = lateralPositionTransformator.transform(movedSourcePos);

      // Then
      assertThat(actualTransformedDepPos, is(expectedTransformedDepPos));
      assertThat(actualTransformedDepPos.getDirection(), is(expectedTransformedDepPos.getDirection()));
   }

   @Test
   void testTransform_FourthQuadrant() {

      // Given
      Position sourcePos = Positions.of(1, -1).rotate(281.25 - 90);
      Position dependingPos = Positions.of(3, -2).rotate(-22.5);
      Position expectedTransformedDepPos = Positions.of(4, -5);
      VectorPositionTransformator lateralPositionTransformator = VectorPositionTransformator.of(sourcePos, dependingPos);

      // When
      Position movedSourcePos = Positions.of(2, -4).rotate(281.25 - 90);
      Position actualTransformedDepPos = lateralPositionTransformator.transform(movedSourcePos);

      // Then
      assertThat(actualTransformedDepPos, is(expectedTransformedDepPos));
      assertThat(actualTransformedDepPos.getDirection(), is(expectedTransformedDepPos.getDirection()));
   }
}

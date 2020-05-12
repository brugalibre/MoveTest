package com.myownb3.piranha.core.grid.collision.bounce.impl;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;

class BouncedPositionEvaluatorImplTest {

   @Test
   void testGetBouncedPosition_1() {

      // Given
      double expectedAngle = 206.56505105053;
      double moveablePosDirectionAngle = 63.43494894947;
      Position gridElemnPos = Positions.of(14, 2);
      Position expectedBouncedPosition = Positions.of(gridElemnPos);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElemPosition(gridElemnPos.rotate(moveablePosDirectionAngle))
            .withPathSegmentStartPos(Positions.of(16, 6))
            .withPathSegmentEndPos(Positions.of(8, 6))
            .build();

      // When
      Position actualBouncedPos =
            tcb.simpleBouncePositionEvaluator.calculateBouncedPosition(tcb.pathSegment, tcb.gridElemPosition);

      // Then
      double actualAngle = round(actualBouncedPos.getDirection().getAngle(), 11);
      assertThat(actualAngle, is(expectedAngle));
      assertThat(actualBouncedPos.getX(), is(expectedBouncedPosition.getX()));
      assertThat(actualBouncedPos.getY(), is(expectedBouncedPosition.getY()));
   }

   @Test
   void testGetBouncedPosition_2() {

      // Given
      double veloCityAngle = 81.86989752053;
      double expectedAngle = 360 - (90 - veloCityAngle);
      double moveablePosDirectionAngle = 180 - veloCityAngle;
      Position gridElemnPos = Positions.of(22, 12);
      Position expectedBouncedPosition = Positions.of(gridElemnPos);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElemPosition(gridElemnPos.rotate(moveablePosDirectionAngle))
            .withPathSegmentStartPos(Positions.of(8, 14))
            .withPathSegmentEndPos(Positions.of(8, 6))
            .build();

      // When
      Position actualBouncedPos =
            tcb.simpleBouncePositionEvaluator.calculateBouncedPosition(tcb.pathSegment, tcb.gridElemPosition);

      // Then
      double actualAngle = round(actualBouncedPos.getDirection().getAngle(), 11);
      assertThat(actualAngle, is(expectedAngle));
      assertThat(actualBouncedPos.getX(), is(expectedBouncedPosition.getX()));
      assertThat(actualBouncedPos.getY(), is(expectedBouncedPosition.getY()));
   }

   private static class TestCaseBuilder {
      private PathSegment pathSegment;
      private BouncedPositionEvaluatorImpl simpleBouncePositionEvaluator;
      private Position pathSegmentEndPos;
      private Position pathSegmentStartPos;
      private Position gridElemPosition;

      private TestCaseBuilder withPathSegmentEndPos(Position pathSegmentEndPos) {
         this.pathSegmentEndPos = pathSegmentEndPos;
         return this;
      }

      private TestCaseBuilder withPathSegmentStartPos(Position pathSegmentStartPos) {
         this.pathSegmentStartPos = pathSegmentStartPos;
         return this;
      }

      private TestCaseBuilder withGridElemPosition(Position gridElemPosition) {
         this.gridElemPosition = gridElemPosition;
         return this;
      }

      private TestCaseBuilder build() {
         pathSegment = new PathSegmentImpl(pathSegmentStartPos, pathSegmentEndPos);
         simpleBouncePositionEvaluator = new BouncedPositionEvaluatorImpl();
         return this;
      }
   }

}

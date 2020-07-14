package com.myownb3.piranha.core.battle.weapon.countermeasure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;

class DecoyFlareDeployPosEvaluatorTest {

   @Test
   void testGetDeployFromPosition_WithoutMatchingPathSegment() {

      // Given
      Position detectedProjectilePosition = Positions.of(-3, 8);
      Position detectorPos = Positions.of(0, 1);
      Rectangle hull = Mockito.spy(RectangleBuilder.builder()
            .withCenter(detectorPos)
            .withWidth(4)
            .withHeight(8)
            .withOrientation(Orientation.HORIZONTAL)
            .build());
      when(hull.getPath()).thenReturn(Collections.emptyList());
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetectedGridElementPos(detectedProjectilePosition)
            .withDetectorPos(detectorPos)
            .withTankHull(hull)
            .build();

      // When
      Executable ex = () -> {
         tcb.decoyFlareDeployPosEvaluator.getDeployFromPosition(tcb.detectedProjectilePosition, tcb.detectorPos, tcb.tankHull);
      };
      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   @Test
   void testGetDeployFromPosition() {

      // Given
      int heightFromGround = 77;
      Position detectedProjectilePosition = Positions.of(-3, 8);
      Position expectedDecoyDeployPos = Positions.of(0, -10, heightFromGround);
      Position detectorPos = Positions.of(0, 1);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetectedGridElementPos(detectedProjectilePosition)
            .withDetectorPos(detectorPos)
            .withHeightFromGround(heightFromGround)
            .withTankHull(RectangleBuilder.builder()
                  .withCenter(detectorPos)
                  .withWidth(4)
                  .withHeight(8)
                  .withOrientation(Orientation.HORIZONTAL)
                  .build())
            .build();

      // When
      Position actualDecoyDeployPos =
            tcb.decoyFlareDeployPosEvaluator.getDeployFromPosition(tcb.detectedProjectilePosition, tcb.detectorPos, tcb.tankHull);

      // Then
      assertThat(actualDecoyDeployPos, is(expectedDecoyDeployPos));
   }

   private static class TestCaseBuilder {
      private Position detectedProjectilePosition;
      private Position detectorPos;
      private Shape tankHull;
      private DecoyFlareDeployPosEvaluator decoyFlareDeployPosEvaluator;
      private double heightFromGround;

      private TestCaseBuilder() {
         // privatni
      }

      private TestCaseBuilder withTankHull(Shape tankHull) {
         this.tankHull = tankHull;
         return this;
      }

      private TestCaseBuilder withDetectorPos(Position detectorPos) {
         this.detectorPos = detectorPos;
         return this;
      }

      private TestCaseBuilder withHeightFromGround(double heightFromGround) {
         this.heightFromGround = heightFromGround;
         return this;
      }

      private TestCaseBuilder withDetectedGridElementPos(Position detectedProjectilePosition) {
         this.detectedProjectilePosition = detectedProjectilePosition;
         return this;
      }

      private TestCaseBuilder build() {
         this.decoyFlareDeployPosEvaluator = new DecoyFlareDeployPosEvaluator(DimensionInfoBuilder.builder()
               .withHeightFromBottom(heightFromGround)
               .withDimensionRadius(1)
               .build());
         return this;
      }
   }
}

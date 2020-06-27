package com.myownb3.piranha.core.weapon.gun.projectile.strategy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.target.TargetGridElement;
import com.myownb3.piranha.core.weapon.target.TargetGridElementEvaluator;
import com.myownb3.piranha.core.weapon.target.TargetGridElementImpl;

class TorpedoProjectileStrategyHandlerTest {

   @Test
   void testHandleProjectileStrategy_WithoutDetectedGridElement() {

      // Given
      Position torpedoPos = spy(Positions.of(5, 5));
      Position expectedTorpedoPos = torpedoPos;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetectedGridElement(null)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(torpedoPos)
                  .build())
            .build();

      // When
      tcb.torpedoProjectileStrategyHandler.handleProjectileStrategy();

      // Then
      assertThat(tcb.shape.getCenter(), is(expectedTorpedoPos));
      verify(tcb.shape.getCenter(), never()).rotate(Mockito.anyDouble());
   }

   @Test
   void testHandleProjectileStrategy_WithDetectedGridElement() {

      // Given
      Position detectedGridElemenPos = Positions.of(0, 0);
      Position torpedoPos = Positions.of(5, 5);
      Position expectedTorpedoPos = Positions.of(5, 5).rotate(100);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetectedGridElement(SimpleGridElementBuilder.builder()
                  .withGrid(mock(Grid.class))
                  .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
                  .withPosition(detectedGridElemenPos)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(torpedoPos)
                  .build())
            .build();

      // When
      tcb.torpedoProjectileStrategyHandler.handleProjectileStrategy();

      // Then
      assertThat(tcb.shape.getCenter(), is(expectedTorpedoPos));
   }

   private class TestCaseBuilder {

      private GridElement detectedGridElement;
      private TargetGridElementEvaluator targetGridElementEvaluator;
      private Shape shape;
      private TorpedoProjectileStrategyHandler torpedoProjectileStrategyHandler;

      private TestCaseBuilder() {
         targetGridElementEvaluator = mock(TargetGridElementEvaluator.class);
      }

      private TestCaseBuilder withDetectedGridElement(GridElement detectedGridElement) {
         this.detectedGridElement = detectedGridElement;
         return this;
      }

      private TestCaseBuilder withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      private TestCaseBuilder build() {
         prepareTargetGridElementEvaluator();
         this.torpedoProjectileStrategyHandler = TorpedoProjectileStrategyHandler.of(targetGridElementEvaluator, shape);
         return this;
      }

      private void prepareTargetGridElementEvaluator() {
         TargetGridElement targetGridElement = null;
         if (detectedGridElement != null) {
            targetGridElement = TargetGridElementImpl.of(detectedGridElement);
         }
         Optional<TargetGridElement> value = Optional.ofNullable(targetGridElement);
         when(targetGridElementEvaluator.getNearestTargetGridElement(eq(shape.getForemostPosition()))).thenReturn(value);
      }

   }

}

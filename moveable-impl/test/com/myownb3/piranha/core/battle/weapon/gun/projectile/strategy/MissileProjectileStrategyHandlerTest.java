package com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.battle.weapon.target.TargetGridElement;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluator;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementImpl;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;

class MissileProjectileStrategyHandlerTest {

   @Test
   void testHandleProjectileStrategy_WithoutDetectedGridElement() {

      // Given
      Position missilePos = spy(Positions.of(5, 5));
      Position expectedMissilePos = missilePos;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetectedGridElement(null)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(missilePos)
                  .build())
            .build();

      // When
      tcb.missileProjectileStrategyHandler.handleProjectileStrategy();

      // Then
      assertThat(tcb.shape.getCenter(), is(expectedMissilePos));
      verify(tcb.shape.getCenter(), never()).rotate(Mockito.anyDouble());
   }

   @Test
   void testHandleProjectileStrategy_WithDetectedGridElement_FirstTime() {

      // Given
      Position detectedGridElemenPos = Positions.of(0, 0);
      Position missilePos = Positions.of(5, 5);
      Position expectedMissilePos = Positions.of(5, 5).rotate(100);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetectedGridElement(SimpleGridElementBuilder.builder()
                  .withGrid(mock(Grid.class))
                  .withShape(PositionShapeBuilder.builder()
                        .withPosition(detectedGridElemenPos)
                        .build())
                  .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(missilePos)
                  .build())
            .build();

      // When
      tcb.missileProjectileStrategyHandler.handleProjectileStrategy();

      // Then
      assertThat(tcb.shape.getCenter(), is(expectedMissilePos));
   }

   @Test
   void testHandleProjectileStrategy_WithDetectedGridElement_SecondTime() {

      // Given
      Position detectedGridElemenPos = Positions.of(0, 0);
      Position movedDetectedGridElemenPos = Positions.of(4, 4);
      Position missilePos = Positions.of(5, 5);
      Position expectedMissilePos = Positions.of(5, 5).rotate(100);
      Moveable detectedGridElem = spy(MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(detectedGridElemenPos)
                  .build())
            .withVelocity(5)
            .build());
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDetectedGridElement(detectedGridElem)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(missilePos)
                  .build())
            .build();

      // When
      tcb.missileProjectileStrategyHandler.handleProjectileStrategy();// First time

      when(detectedGridElem.getPosition()).thenReturn(movedDetectedGridElemenPos);// The targed is moving. This has to be like this, because the GridEelemEvaluator is mocked..
      doReturn(Optional.of(TargetGridElementImpl.of(detectedGridElem)))
            .when(tcb.targetGridElementEvaluator).getNearestTargetGridElement(eq(tcb.shape.getForemostPosition()));
      tcb.missileProjectileStrategyHandler.handleProjectileStrategy();// Second time

      // Then
      assertThat(tcb.shape.getCenter(), is(expectedMissilePos));
   }

   private class TestCaseBuilder {

      private GridElement detectedGridElement;
      private TargetGridElementEvaluator targetGridElementEvaluator;
      private Shape shape;
      private MissileProjectileStrategyHandler missileProjectileStrategyHandler;

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
         this.missileProjectileStrategyHandler = MissileProjectileStrategyHandler.of(targetGridElementEvaluator, shape, 10);
         return this;
      }

      private void prepareTargetGridElementEvaluator() {
         TargetGridElement targetGridElement = null;
         if (detectedGridElement != null) {
            targetGridElement = TargetGridElementImpl.of(detectedGridElement);
         }
         mockTargetGridElementEvaluator(targetGridElement);
      }

      private void mockTargetGridElementEvaluator(TargetGridElement targetGridElement) {
         Optional<TargetGridElement> targetGridElementOpt = Optional.ofNullable(targetGridElement);
         when(targetGridElementEvaluator.getNearestTargetGridElement(eq(shape.getForemostPosition()))).thenReturn(targetGridElementOpt);
      }
   }
}

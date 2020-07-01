package com.myownb3.piranha.core.moveables;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AutoMoveable.AutoMoveableBuilder;

class AutoMoveableTest {

   //   @Test
   void testAutoMoveable() {

      // Given
      Position expectedPos = Positions.of(0, 0.1);
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withHealth(HealthImpl.of(1))
                  .withDamage(DamageImpl.of(1))
                  .build())
            .withDimensionInfo(getDefaultDimensionInfo(3))
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // When
      autoMoveable.autodetect();

      // Then
      assertThat(autoMoveable.getPosition(), is(expectedPos));
   }

   @Test
   void testAutodetect() {
      // Given
      Position expectedPos = Positions.of(0, 0.1);
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withDimensionInfo(getDefaultDimensionInfo(3))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withHealth(HealthImpl.of(1))
                  .withDamage(DamageImpl.of(1))
                  .build())
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // When
      autoMoveable.autodetect();

      // Then
      assertThat(autoMoveable.getPosition(), is(expectedPos));
   }

}

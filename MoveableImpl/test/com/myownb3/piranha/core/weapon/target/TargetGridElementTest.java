package com.myownb3.piranha.core.weapon.target;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

class TargetGridElementTest {

   @Test
   void testIsMoving_XAxisChanged() {

      // Given
      Position position = Positions.of(1, 1);
      Position gridElementPos = Positions.of(0, 1);
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withPosition(gridElementPos)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElementPos)
                  .build())
            .withGrid(mock(Grid.class))
            .build();
      TargetGridElement targetGridElement = TargetGridElementImpl.of(gridElement);
      targetGridElement.setPrevAcquiredPos(position);

      // When
      boolean actualIsMoving = targetGridElement.isMoving();

      // Then
      assertThat(actualIsMoving, is(true));
   }

   @Test
   void testIsMoving_YAxisChanged() {

      // Given
      Position position = Positions.of(1, 1);
      Position gridElementPos = Positions.of(1, 0);
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withPosition(gridElementPos)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElementPos)
                  .build())
            .withGrid(mock(Grid.class))
            .build();
      TargetGridElement targetGridElement = TargetGridElementImpl.of(gridElement);
      targetGridElement.setPrevAcquiredPos(position);

      // When
      boolean actualIsMoving = targetGridElement.isMoving();

      // Then
      assertThat(actualIsMoving, is(true));
   }

   @Test
   void testIsMoving_NotMoving() {

      // Given
      Position position = Positions.of(1, 1);
      Position gridElementPos = Positions.of(1, 1);
      GridElement gridElement = SimpleGridElementBuilder.builder()
            .withPosition(gridElementPos)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElementPos)
                  .build())
            .withGrid(mock(Grid.class))
            .build();
      TargetGridElement targetGridElement = TargetGridElementImpl.of(gridElement);
      targetGridElement.setPrevAcquiredPos(position);

      // When
      boolean actualIsMoving = targetGridElement.isMoving();

      // Then
      assertThat(actualIsMoving, is(false));
   }

}

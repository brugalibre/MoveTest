/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.SwappingGrid.SwappingGridBuilder;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.test.Assert;

/**
 * @author Dominic
 *
 */
class SwappingGridTest {

   @Test
   public void testDefaultGridBoundsMoveForward() {

      // Given
      Grid grid = SwappingGridBuilder.builder(10, 10)
            .build();
      Position expectedPosition = Positions.of(Directions.N, 10, 0.1, 0);
      Position expectedPosition2 = Positions.of(Directions.O, 0.1, 10, 0);

      // When
      Position createdPosition = grid.moveForward(buildGridElement(grid, Positions.of(Directions.N, 10, 10, 0)));
      Position createdPosition2 = grid.moveForward(buildGridElement(grid, Positions.of(Directions.O, 10, 10, 0)));

      // Then
      Assert.assertThatPosition(createdPosition, is(expectedPosition), 3);
      Assert.assertThatPosition(createdPosition2, is(expectedPosition2), 3);
   }

   @Test
   public void testDefaultGridBoundsMoveBackward() {

      // Given
      Grid grid = SwappingGridBuilder.builder(10, 10)
            .build();
      Position expectedPosition = Positions.of(Directions.N, 4, 9.9, 0);
      Position expectedPosition2 = Positions.of(Directions.O, 9.9, 4, 0);

      // When
      Position createdPosition = grid.moveBackward(buildGridElement(grid, Positions.of(Directions.N, 4, 0, 0)));
      Position createdPosition2 = grid.moveBackward(buildGridElement(grid, Positions.of(Directions.O, 0, 4, 0)));

      // Then
      assertThat(createdPosition, is(expectedPosition));
      assertThat(createdPosition2, is(expectedPosition2));
   }

   @Test
   public void testOffsetGridBoundsMoveForward() {

      // Given
      Grid grid = SwappingGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(5)
            .withMinY(5)
            .build();
      Position expectedPosition = Positions.of(Directions.N, 10, 5.1, 0);
      Position expectedPosition2 = Positions.of(Directions.O, 5.1, 10, 0);
      Position expectedPosition3 = Positions.of(Directions.S, 10, 9.9, 0);

      // When
      Position createdPosition = grid.moveForward(buildGridElement(grid, Positions.of(Directions.N, 10, 10, 0)));
      Position createdPosition2 = grid.moveForward(buildGridElement(grid, Positions.of(Directions.O, 10, 10, 0)));
      Position createdPosition3 = grid.moveForward(buildGridElement(grid, Positions.of(Directions.S, 10, 5, 0)));

      // Then
      assertThat(createdPosition, is(expectedPosition));
      assertThat(createdPosition2, is(expectedPosition2));
      assertThat(createdPosition3, is(expectedPosition3));
   }

   @Test
   public void testOffsetGridBoundsMoveBackward() {

      // Given
      Grid grid = SwappingGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(5)
            .withMinY(5)
            .build();
      Position expectedPosition = Positions.of(Directions.N, 9, 9.9, 0);
      Position expectedPosition2 = Positions.of(Directions.O, 9.9, 9, 0);
      Position expectedPosition3 = Positions.of(Directions.S, 5, 5.1, 0);

      // When
      Position createdPosition = grid.moveBackward(buildGridElement(grid, Positions.of(Directions.N, 9, 5, 0)));
      Position createdPosition2 = grid.moveBackward(buildGridElement(grid, Positions.of(Directions.O, 5, 9, 0)));
      Position createdPosition3 = grid.moveBackward(buildGridElement(grid, Positions.of(Directions.S, 5, 10, 0)));

      // Then
      assertThat(createdPosition, is(expectedPosition));
      assertThat(createdPosition2, is(expectedPosition2));
      assertThat(createdPosition3, is(expectedPosition3));
   }

   private static GridElement buildGridElement(Grid grid, Position position) {
      return SimpleGridElementBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(position)
                  .build())
            .build();
   }
}

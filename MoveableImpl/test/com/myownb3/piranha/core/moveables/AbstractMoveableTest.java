/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;

/**
 * @author Dominic
 *
 */
class AbstractMoveableTest {

   @Test
   void test_BuildWithShape() {

      // Given
      Position startPos = Positions.of(0, 0);
      PositionShape shape = PositionShapeBuilder.builder()
            .withPosition(startPos)
            .build();

      // When
      Moveable moveable = MoveableBuilder.builder()
            .withShape(shape)
            .withPosition(Positions.of(0, 0))
            .withGrid(GridBuilder.builder()
                  .build())
            .build();
      // Then
      MatcherAssert.assertThat(((AbstractGridElement) moveable).getShape(), is(shape));
   }

}

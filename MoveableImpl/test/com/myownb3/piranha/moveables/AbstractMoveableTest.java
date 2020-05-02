/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;

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
            .build();
      // Then
      MatcherAssert.assertThat(((AbstractGridElement) moveable).getShape(), is(shape));
   }

}

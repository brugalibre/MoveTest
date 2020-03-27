/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.grid.gridelement.AbstractGridElement;

/**
 * @author Dominic
 *
 */
class AbstractMoveableTest {

   @Test
   void test_BuildWithShape() {

      // Given
      Position startPos = Positions.of(0, 0);
      PositionShape shape = new PositionShape(startPos);

      // When
      Moveable moveable = MoveableBuilder.builder()
            .withShape(shape)
            .build();
      // Then
      assertThat(((AbstractGridElement) moveable).getShape(), is(shape));
   }

}

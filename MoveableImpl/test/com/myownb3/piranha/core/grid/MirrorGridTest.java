/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;

/**
 * @author Dominic
 *
 */
class MirrorGridTest {

   @Test
   public void testCollisionWithProjectileOnGridWall() {

      // Given
      Grid grid = MirrorGridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(0)
            .build();

      // When
      int actualAmountOfGridElements = grid.getAllGridElements(null).size();

      // Then
      assertThat(actualAmountOfGridElements, is(4));
   }
}

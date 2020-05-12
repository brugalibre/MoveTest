package com.myownb3.piranha.core.grid.collision;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;

class CollisionDetectionResultImplTest {

   @Test
   void testCollisionDetectionResultWithCollision() {

      // Given
      boolean expectedIsCollision = true;

      // When
      CollisionDetectionResult result = new CollisionDetectionResultImpl(expectedIsCollision, Positions.of(5, 4));

      // Then
      assertThat(result.isCollision(), is(expectedIsCollision));
   }

}

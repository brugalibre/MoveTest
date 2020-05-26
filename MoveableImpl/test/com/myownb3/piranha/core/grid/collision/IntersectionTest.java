package com.myownb3.piranha.core.grid.collision;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;

class IntersectionTest {

   @Test
   void testBuildIntersection() {
      // Given
      Position position = Positions.of(1, 1);
      PathSegment pathSegment = new PathSegmentImpl(Positions.of(1, 1), Positions.of(1, 1));

      // When
      Intersection intersection = IntersectionImpl.of(pathSegment, position);

      // Then
      assertThat(intersection.getCollisionPosition(), is(position));
      assertThat(intersection.getPathSegment(), is(pathSegment));
   }

}

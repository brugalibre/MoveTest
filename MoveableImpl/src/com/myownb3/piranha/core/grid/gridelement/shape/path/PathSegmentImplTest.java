package com.myownb3.piranha.core.grid.gridelement.shape.path;

import static com.myownb3.piranha.util.vector.VectorUtil.getVector;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;

class PathSegmentImplTest {

   @Test
   void testCreatePathSegment() {

      // Given
      Position begPos = Positions.of(0, 0);
      Position endPos = Positions.of(5, 5);

      // When
      PathSegmentImpl pathSegmentImpl = new PathSegmentImpl(begPos, endPos);

      // Then
      assertThat(pathSegmentImpl.getBegin(), is(begPos));
      assertThat(pathSegmentImpl.getEnd(), is(endPos));
      assertThat(pathSegmentImpl.getVector(), is(getVector(endPos).minus(getVector(begPos))));
   }
}

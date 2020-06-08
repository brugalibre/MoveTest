package com.myownb3.piranha.core.grid.gridelement.shape.path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jscience.mathematics.vector.Float64Vector;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;

class PathSegmentImplTest {

   @Test
   void testCreatePathSegment_getNormalVectorBeginPos() {

      // Given
      Position begPos = Positions.of(0, 0);
      Position endPos = Positions.of(5, 5);
      Float64Vector expectedNormalVectorAtBegin = Float64Vector.valueOf(0.07071067809999999, 0.07071067809999999, 0.0);
      Float64Vector expectedNormalVectorAtEnd = Float64Vector.valueOf(0.07071067809999999, 0.07071067809999999, 0.0);

      // When
      PathSegmentImpl pathSegmentImpl = new PathSegmentImpl(begPos, endPos);
      pathSegmentImpl.getNormalVectorAtEndPos();
      pathSegmentImpl.getNormalVectorAtBeginPos();

      // Then
      assertThat(pathSegmentImpl.getNormalVectorAtEndPos(), is(expectedNormalVectorAtEnd));
      assertThat(pathSegmentImpl.getNormalVectorAtBeginPos(), is(expectedNormalVectorAtBegin));
   }

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
      assertThat(pathSegmentImpl.getVector(), is(endPos.getVector().minus(begPos.getVector())));
   }

   @Test
   void testToString() {

      // Given
      Position begPos = Positions.of(0, 0);
      Position endPos = Positions.of(5, 5);
      PathSegmentImpl pathSegmentImpl = new PathSegmentImpl(begPos, endPos);
      String expectedString =
            "Begin-Position: 'Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '0.0', Y-Axis: '0.0''; End-Position: 'Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '5.0', Y-Axis: '5.0''; Vector '{5.0, 5.0, 0.0}";

      // When
      String actual2String = pathSegmentImpl.toString();

      // Then
      assertThat(actual2String, is(expectedString));
   }
}
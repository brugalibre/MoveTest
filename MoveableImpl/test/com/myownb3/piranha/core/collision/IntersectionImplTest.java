package com.myownb3.piranha.core.collision;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;

class IntersectionImplTest {

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

   @Test
   void testToString() {

      // Given
      Position position = Positions.of(1, 1);
      PathSegment pathSegment = new PathSegmentImpl(Positions.of(1, 1), Positions.of(1, 1));

      // When
      Intersection intersection = IntersectionImpl.of(pathSegment, position);
      String expectedString =
            "CollisionPosition: 'Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '1.0', Y-Axis: '1.0'';"
                  + " PathSegment: 'Begin-Position: 'Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '1.0', "
                  + "Y-Axis: '1.0''; End-Position: 'Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '1.0', Y-Axis: '1.0''; Vector '{0.0, 0.0, 0.0}'";

      // When
      String actual2String = intersection.toString();

      // Then
      assertThat(actual2String, is(expectedString));
   }

}

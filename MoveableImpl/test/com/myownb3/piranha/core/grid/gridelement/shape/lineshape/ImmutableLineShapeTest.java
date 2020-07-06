package com.myownb3.piranha.core.grid.gridelement.shape.lineshape;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.lineshape.ImmutableLineShape.ImmutableLineShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;

class ImmutableLineShapeTest {

   @Test
   void testBuildPath4Detection() {
      // Given
      Position beginPos = Positions.of(5, 5);
      Position endPos = Positions.of(0, 5);
      ImmutableLineShape immutableLineShape = ImmutableLineShapeBuilder.builder()
            .withBeginPosition(beginPos)
            .withEndPosition(endPos)
            .build();

      // When
      List<Position> actualPath4Detection = immutableLineShape.buildPath4Detection();

      // Then
      assertThat(actualPath4Detection, is(Arrays.asList(beginPos, endPos)));
   }

   @Test
   void testTransform() {

      // Given
      Position beginPos = Positions.of(5, 5);
      Position endPos = Positions.of(0, 5);
      ImmutableLineShape immutableLineShape = ImmutableLineShapeBuilder.builder()
            .withBeginPosition(beginPos)
            .withEndPosition(endPos)
            .build();

      // When
      immutableLineShape.transform(Positions.of(50, 50));

      // Then
      assertThat(immutableLineShape.getForemostPosition(), is(endPos));
      assertThat(immutableLineShape.getRearmostPosition(), is(beginPos));
   }

   @Test
   void testGetPath() {
      // Given
      Position beginPos = Positions.of(5, 5);
      Position endPos = Positions.of(0, 5);
      ImmutableLineShape immutableLineShape = ImmutableLineShapeBuilder.builder()
            .withBeginPosition(beginPos)
            .withEndPosition(endPos)
            .build();

      // When
      List<PathSegment> actualPath = immutableLineShape.getPath();

      // Then
      assertThat(actualPath.size(), is(1));
      PathSegment pathSegment = actualPath.get(0);
      assertThat(pathSegment.getBegin(), is(beginPos));
      assertThat(pathSegment.getEnd(), is(endPos));
   }

   @Test
   void testCheck4Collision() { // Given
      Position beginPos = Positions.of(5, 5);
      Position endPos = Positions.of(0, 5);
      ImmutableLineShape immutableLineShape = ImmutableLineShapeBuilder.builder()
            .withBeginPosition(beginPos)
            .withEndPosition(endPos)
            .build();

      // When
      CollisionDetectionHandler collisionDetectionHandler = mock(CollisionDetectionHandler.class);
      immutableLineShape.check4Collision(collisionDetectionHandler, beginPos, Collections.emptyList());

      // Then
      verify(collisionDetectionHandler, never()).handleCollision(any(), any(), any());
   }

   @Test
   void testGetDimensionRadius() {
      // Given
      Position beginPos = Positions.of(5, 5);
      Position endPos = Positions.of(0, 5);
      ImmutableLineShape immutableLineShape = ImmutableLineShapeBuilder.builder()
            .withBeginPosition(beginPos)
            .withEndPosition(endPos)
            .build();
      PathSegmentImpl pathSegmentImpl = new PathSegmentImpl(beginPos, endPos);

      // When
      double actualDimensionRadius = immutableLineShape.getDimensionRadius();

      // Then
      assertThat(actualDimensionRadius, is(pathSegmentImpl.getLenght()));
   }


}

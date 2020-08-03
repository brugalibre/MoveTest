package com.myownb3.piranha.core.grid.gridelement.wall;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class WallBuilderTest {

   @Test
   void testBuildWall() {

      // Given
      MirrorGrid grid = MirrorGridBuilder.builder()
            .withMaxX(800)
            .withMaxY(900)
            .withMinX(0)
            .withMinY(0)
            .build();

      Position wallSegmentPos = Positions.of(390, 700);
      int wallSegmentLength = 40;
      int wallSegmentWidth = 10;
      double wallHealth = 800.0;

      int expectedAmountOfWallSegments = 9;
      Position expectedWallSegmentPos = Positions.of(503.252891595, 420.03045953, 0).rotate(80);

      // When
      List<WallGridElement> wallSegments = WallBuilder.builder()
            .withGrid(grid)
            .withWallHealth(wallHealth)
            .withWallStartPos(wallSegmentPos)
            .withWallSegmentWidth(wallSegmentWidth)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .rotate(80.0)
            .addWallSegment(wallSegmentLength)
            .addGap(wallSegmentLength / 2d)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .build();

      // Then
      assertThat(wallSegments.size(), is(expectedAmountOfWallSegments));
      WallGridElement lastWallGridElement = wallSegments.get(wallSegments.size() - 1);
      assertThat(lastWallGridElement.getPosition(), is(expectedWallSegmentPos));
   }
}

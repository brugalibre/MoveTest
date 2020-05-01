package com.myownb3.piranha.grid.maze;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.grid.maze.MazeImpl.MazeBuilder;

class MazeImplTest {

   @Test
   void testMazeTest_WithGridElementInLastCoridor() {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(500)
            .withMaxY(500)
            .withMinY(-500)
            .withMinX(-500)
            .build();
      int wallThickness = 10;
      int coridorWidth = 50;
      int segmentLength = 60;

      Position center = Positions.of(0, 0);
      // When
      Maze maze = MazeBuilder.builder()
            .withGrid(grid)
            .withStartPos(center)
            .withCorridor(wallThickness)
            .withCorridorWidth(coridorWidth)
            .withSegmentLenth(segmentLength)
            .appendCorridorSegment()
            .appendCorridorSegment()
            .appendCorridorLeftAngleBend()
            .appendCorridorSegment()
            .appendCorridorRightAngleBend()
            .appendCorridorSegment()
            .appendCorridorSegment()
            .withObstacle(CircleBuilder.builder()
                  .withRadius(4)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(0, 0))
                  .build(), 0, 0)
            .build()
            .build();
      // Then

      CorridorSegment lastCorridorSegment = maze.getMazeCorridorSegments().get(maze.getMazeCorridorSegments().size() - 1);
      Optional<Circle> circleOpt = getCircleOpt(maze);
      assertThat(circleOpt.isPresent(), is(true));
      Circle circle = circleOpt.get();
      assertThat(circle.getCenter(), is(lastCorridorSegment.getCorridorSegCenter()));
   }

   @Test
   void testMazeTest_GetAllGridElements() {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(500)
            .withMaxY(500)
            .withMinY(-500)
            .withMinX(-500)
            .build();
      int expectedAmountOfGridElements = 16;
      int wallThickness = 10;
      int coridorWidth = 50;
      int segmentLength = 60;

      Position center = Positions.of(0, 0);
      // When
      Maze maze = MazeBuilder.builder()
            .withGrid(grid)
            .withStartPos(center)
            .withCorridor(wallThickness)
            .withCorridorWidth(coridorWidth)
            .withSegmentLenth(segmentLength)
            .appendCorridorSegment()
            .appendCorridorSegment()
            .appendCorridorLeftAngleBend()
            .appendCorridorSegment()
            .appendCorridorRightAngleBend()
            .appendCorridorSegment()
            .build()
            .build();
      // Then

      assertThat(maze.getAllMazeGridElements().size(), is(expectedAmountOfGridElements));
   }

   private static Optional<Circle> getCircleOpt(Maze maze) {
      return maze.getAllMazeGridElements().stream()
            .map(GridElement::getShape)
            .filter(shape -> shape instanceof Circle)
            .map(Circle.class::cast)
            .findFirst();
   }
}

package com.myownb3.piranha.grid.maze;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.EndPositionGridElement;
import com.myownb3.piranha.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;

public class MazeImpl implements Maze {

   private List<GridElement> mazeGridElements;
   private List<CorridorSegment> corridorSegments;
   private List<EndPosition> endPositions;
   private Grid grid;

   private MazeImpl(Grid grid, List<GridElement> mazeGridElements, List<CorridorSegment> corridorSegments, List<EndPosition> endPositions) {
      this.mazeGridElements = mazeGridElements;
      this.corridorSegments = corridorSegments;
      this.endPositions = endPositions;
      this.grid = grid;
   }

   @Override
   public List<GridElement> getAllMazeGridElements() {
      return mazeGridElements;
   }

   @Override
   public List<CorridorSegment> getMazeCorridorSegments() {
      return corridorSegments;
   }

   @Override
   public List<EndPosition> getEndPositions() {
      return endPositions;
   }

   @Override
   public Grid getGrid() {
      return grid;
   }

   public static class MazeBuilder {
      private Grid grid;
      private Position mazeStartPos;
      private EndPosition endPosition; // Defines the final End-Position which may be located outside the Maze
      private List<EndPosition> endPositions;// Defines all the End-Positions which are located within the Maze
      private List<CorridorSegment> corridorSegments;
      private List<GridElement> mazeGridElements;
      private double endPositionPrecision;

      private MazeBuilder() {
         corridorSegments = new ArrayList<>();
         mazeGridElements = new ArrayList<>();
         endPositions = new LinkedList<>();
      }

      public MazeBuilder withStartPos(Position mazeStartPos) {
         this.mazeStartPos = mazeStartPos;
         return this;
      }

      public MazeBuilder withEndPosition(EndPosition endPosition) {
         this.endPosition = endPosition;
         return this;
      }

      public MazeBuilder withEndPositionPrecision(double endPositionPrecision) {
         this.endPositionPrecision = endPositionPrecision;
         return this;
      }

      public MazeBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public CorridorBuilder withCorridor(int wallThickness) {
         return new CorridorBuilder(this, grid, mazeStartPos, wallThickness, endPositionPrecision);
      }

      public Maze build() {
         corridorSegments.stream()
               .forEach(addCorridorSegmentRectangles());

         // First add the final End-Position - if present
         endPositions = Stream.concat(endPositions.stream(), Stream.of(endPosition))
               .filter(Objects::nonNull)
               .collect(Collectors.toList());
         // Then convert all End-Positions into GridElement
         endPositions.stream()
               .forEach(createAndAddEndPosGridElement());
         return new MazeImpl(grid, mazeGridElements, corridorSegments, endPositions);
      }

      public static MazeBuilder builder() {
         return new MazeBuilder();
      }

      private Consumer<EndPosition> createAndAddEndPosGridElement() {
         return endPos -> {
            mazeGridElements.add(new EndPositionGridElement(grid, endPos, CircleBuilder.builder()
                  .withRadius(4)
                  .withAmountOfPoints(4)
                  .withCenter(endPos)
                  .build()));
         };
      }

      private Consumer<? super CorridorSegment> addCorridorSegmentRectangles() {
         return corridorSegment -> {
            mazeGridElements.add(corridorSegment.getCorridorSegmentWallLeft());
            mazeGridElements.add(corridorSegment.getCorridorSegmentWallRight());
         };
      }

      public static class CorridorBuilder {
         private int wallThickness;
         private int corridorWidth;
         private int segmentLenth;

         private Grid grid;
         private List<GridElement> corridorGridElements;

         private Position mazeStartPos;
         private Position corridorSegmentCenter;
         private List<CorridorSegmentImpl> corridorSegments;
         private MazeBuilder mazeBuilder;
         private List<EndPosition> endPositions;
         private double endPositionPrecision;

         private CorridorBuilder(MazeBuilder mazeBuilder, Grid grid, Position mazeStartPos, int wallThickness, double endPositionPrecision) {
            this.corridorSegments = new ArrayList<>();
            this.corridorGridElements = new ArrayList<>();
            this.endPositions = new LinkedList<>();
            this.mazeBuilder = mazeBuilder;
            this.wallThickness = wallThickness;
            this.grid = grid;
            this.mazeStartPos = mazeStartPos;
            this.endPositionPrecision = endPositionPrecision;
         }

         public CorridorBuilder withCorridorWidth(int corridorWidth) {
            this.corridorWidth = corridorWidth;
            return this;
         }

         public CorridorBuilder withSegmentLenth(int segmentLenth) {
            this.segmentLenth = segmentLenth;
            return this;
         }

         public CorridorBuilder appendCorridorSegment() {

            corridorSegmentCenter = getNextSegmentCenter();
            Position rectangleCenter = getRectangleCenter(corridorSegmentCenter, 90);
            GridElement corridorSegmentWall1 = buildRectangleObstacle(grid, wallThickness, segmentLenth, rectangleCenter);

            rectangleCenter = getRectangleCenter(corridorSegmentCenter, -90);
            GridElement corridorSegmentWall2 = buildRectangleObstacle(grid, wallThickness, segmentLenth, rectangleCenter);
            corridorSegments.add(new CorridorSegmentImpl(corridorSegmentWall1, corridorSegmentWall2, corridorSegmentCenter));
            return this;
         }

         public CorridorBuilder appendCorridorLeftAngleBend() {
            appendCorridorAngleBend(1);
            return this;
         }

         public CorridorBuilder appendCorridorRightAngleBend() {
            appendCorridorAngleBend(-1);
            return this;
         }

         public CorridorBuilder withObstacle(Shape shape, int xOffset, int yOffset) {
            Position gridElementPos = Positions.of(corridorSegmentCenter.getX() + xOffset, corridorSegmentCenter.getY() + yOffset);
            GridElement gridElement = new ObstacleImpl(grid, gridElementPos, shape);
            this.corridorGridElements.add(gridElement);
            return this;
         }

         public CorridorBuilder withEndPosition(Shape shape) {
            EndPosition endPosition = EndPositions.of(corridorSegmentCenter, endPositionPrecision);
            this.endPositions.add(endPosition);
            return this;
         }

         private void appendCorridorAngleBend(int signum) {
            corridorSegmentCenter = getNextSegmentCenter();

            // First build the angle (bendSegment) itself. It consist of two Corridor elements, like this: _| Note that the vertical element is slightly greater
            Position rectangleCenter = getRectangleCenter(corridorSegmentCenter, signum * 90);
            GridElement bendSegment1 = buildRectangleObstacle(grid, wallThickness, segmentLenth, rectangleCenter);

            // Move it at the end of the rectangle above
            rectangleCenter = Positions.of(corridorSegmentCenter);
            rectangleCenter.rotate(signum * -90);
            rectangleCenter = Positions.movePositionForward4Distance(corridorSegmentCenter, halfThickness() + segmentLenth / 2);
            GridElement bendSegment2 = buildRectangleObstacle(grid, wallThickness, corridorWidth + 2 * wallThickness, rectangleCenter);
            corridorSegmentCenter.rotate(signum * -90);
            corridorSegments.add(new CorridorSegmentImpl(bendSegment1, bendSegment2, corridorSegmentCenter));

            // and now add another corridor segment to finish the angle-bend
            corridorSegmentCenter = Positions.movePositionForward4Distance(corridorSegmentCenter, (corridorWidth / 2) - 3.0 * wallThickness);
            appendCorridorSegment();
         }


         public MazeBuilder build() {
            mazeBuilder.corridorSegments.addAll(corridorSegments);
            mazeBuilder.mazeGridElements.addAll(corridorGridElements);
            mazeBuilder.endPositions.addAll(endPositions);
            return mazeBuilder;
         }

         private Position getNextSegmentCenter() {
            if (isNull(corridorSegmentCenter)) {
               corridorSegmentCenter = Positions.movePositionForward4Distance(mazeStartPos, segmentLenth / 2);
               return corridorSegmentCenter;
            }
            return Positions.movePositionForward4Distance(corridorSegmentCenter, segmentLenth);
         }

         private Position getRectangleCenter(Position corridorSegCenter, double angle2Rotate) {
            return getRectangleCenter(corridorSegCenter, (corridorWidth + wallThickness) / 2, angle2Rotate);
         }

         private Position getRectangleCenter(Position corridorSegCenter, double distance2Move, double angle2Rotate) {
            Position rectangleCenter = Positions.of(corridorSegCenter);
            rectangleCenter.rotate(angle2Rotate);
            rectangleCenter = Positions.movePositionForward4Distance(rectangleCenter, distance2Move);
            return rectangleCenter;
         }

         private int halfThickness() {
            return wallThickness / 2;
         }
      }
   }

   private static GridElement buildRectangleObstacle(Grid grid, double height, double width, Position center) {
      return new ObstacleImpl(grid, center, RectangleBuilder.builder()
            .withCenter(center)
            .withHeight(height)
            .withWidth(width)
            .withDistanceBetweenPosOnColDetectionPath(5)
            .withOrientation(Orientation.HORIZONTAL)
            .build());
   }
}
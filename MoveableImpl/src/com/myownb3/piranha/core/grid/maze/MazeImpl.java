package com.myownb3.piranha.core.grid.maze;

import static com.myownb3.piranha.core.grid.gridelement.position.Positions.movePositionBackward4Distance;
import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.PlacedDetectorImpl.PlacedDetectorBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositionGridElement;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;
import com.myownb3.piranha.core.grid.gridelement.wall.WallGridElement.WallGridElementBuilder;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSegment;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSegmentImpl;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSide;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement.TurretGridElementBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;

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
         private CorridorSegment currentCorridorSegment;
         private List<CorridorSegment> corridorSegments;
         private MazeBuilder mazeBuilder;

         private List<EndPosition> endPositions;
         private double endPositionPrecision;

         private CorridorBuilder(MazeBuilder mazeBuilder, Grid grid, Position mazeStartPos, int wallThickness, double endPositionPrecision) {
            this.corridorSegments = new LinkedList<>();
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
            appendCorridorSegment(false);
            return this;
         }

         private void appendCorridorSegment(boolean isAngleBend) {
            Position corridorSegmentCenter = getNextSegmentCenter();
            Position rectangleCenter = getRectangleCenter(corridorSegmentCenter, 90);
            Wall corridorSegmentWall1 = buildWallGridElemenet(grid, wallThickness, segmentLenth, rectangleCenter);

            rectangleCenter = getRectangleCenter(corridorSegmentCenter, -90);
            Wall corridorSegmentWall2 = buildWallGridElemenet(grid, wallThickness, segmentLenth, rectangleCenter);
            currentCorridorSegment = new CorridorSegmentImpl(corridorSegmentWall1, corridorSegmentWall2, corridorSegmentCenter, isAngleBend);
            corridorSegments.add(currentCorridorSegment);
         }

         public CorridorBuilder withTurret(IDetector detector, GunCarriage gunCarriage, CorridorSide corridorSide) {
            Position corridorSegmentCenter = getTurretPosition(gunCarriage, corridorSide);
            gunCarriage.getShape().transform(corridorSegmentCenter);
            corridorGridElements.add(TurretGridElementBuilder.builder()
                  .withGrid(grid)
                  .withTurret(TurretBuilder.builder()
                        .withDetector(detector)
                        .withGunCarriage(gunCarriage)
                        .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                        .build())
                  .build());
            return this;
         }

         private Position getTurretPosition(GunCarriage gunCarriage, CorridorSide corridorSide) {
            boolean isAngleBend = currentCorridorSegment.isAngleBend();
            if (!isAngleBend) {
               return getPositionOnWall(corridorSide, 0);
            }
            double offset2Corner = corridorWidth / 2 + gunCarriage.getShape().getDimensionRadius();
            return getPositionOnAngleBendWall(offset2Corner);
         }

         private Position getPositionOnAngleBendWall(double offset2Corner) {
            double angleBetweenLeftAndRightWall = calcAngleDiffBetweenWalls();
            double angleDiff = adjustAngleDiff4TurnWithin180(angleBetweenLeftAndRightWall / 2);
            return movePositionBackward4Distance(currentCorridorSegment.getCorridorSegCenter()
                  .rotate(angleDiff), offset2Corner);
         }

         private double calcAngleDiffBetweenWalls() {
            Wall corridorSegmentWallLeft = currentCorridorSegment.getCorridorSegmentWallLeft();
            Wall corridorSegmentWallRight = currentCorridorSegment.getCorridorSegmentWallRight();
            double angleRight = corridorSegmentWallRight.getPosition().getDirection().getAngle();
            double angleLeft = corridorSegmentWallLeft.getPosition().getDirection().getAngle();
            return angleLeft - angleRight;
         }

         private double adjustAngleDiff4TurnWithin180(double angleDiff) {
            if (angleDiff < 0 && angleDiff < -90) {
               angleDiff = angleDiff - 180;
            }
            return angleDiff;
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
            Position corridorSegmentCenter = currentCorridorSegment.getCorridorSegCenter();
            Position gridElementPos = Positions.of(corridorSegmentCenter.getX() + xOffset, corridorSegmentCenter.getY() + yOffset);
            GridElement gridElement = ObstacleBuilder.builder()
                  .withGrid(grid)
                  .withPosition(gridElementPos)
                  .withShape(shape)
                  .build();
            this.corridorGridElements.add(gridElement);
            return this;
         }

         public CorridorBuilder withMoveableObstacle(Shape shape, int xOffset, int yOffset, double angleOffset) {
            Position corridorSegmentCenter = currentCorridorSegment.getCorridorSegCenter();
            Position gridElementPos = Positions.of(corridorSegmentCenter.getX() + xOffset, corridorSegmentCenter.getY() + yOffset)
                  .rotate(angleOffset);
            GridElement gridElement = MoveableObstacleBuilder.builder()
                  .withGrid(grid)
                  .withPosition(gridElementPos)
                  .withShape(shape)
                  .build();
            this.corridorGridElements.add(gridElement);
            return this;
         }

         public CorridorBuilder withDetector(DetectorConfig detectorConfig, CorridorSide corridorSide) {
            Position detectorCenter = getPositionOnWall(corridorSide, segmentLenth / 2);

            currentCorridorSegment.setDetector(PlacedDetectorBuilder.builder()
                  .withIDetector(DetectorBuilder.builder()
                        .withAngleInc(detectorConfig.getEvasionAngleInc())
                        .withDetectorAngle(detectorConfig.getDetectorAngle())
                        .withDetectorReach(detectorConfig.getDetectorReach())
                        .withEvasionAngle(detectorConfig.getDetectorAngle())
                        .withEvasionDistance(detectorConfig.getEvasionDistance())
                        .build())
                  .withPosition(detectorCenter)
                  .build());
            return this;
         }

         private Position getPositionOnWall(CorridorSide corridorSide, double offsetFromCenterAlongTheWall) {
            Position corridorSegmentCenter = currentCorridorSegment.getCorridorSegCenter();
            Position wallPosition = Positions.movePositionForward4Distance(corridorSegmentCenter, offsetFromCenterAlongTheWall);
            switch (corridorSide) {
               case LEFT:
                  wallPosition = wallPosition.rotate(-90);
                  wallPosition = Positions.movePositionForward4Distance(wallPosition, corridorWidth / 2);
                  wallPosition = wallPosition.rotate(-180);
                  break;
               case RIGHT:
                  wallPosition = wallPosition.rotate(90);
                  wallPosition = Positions.movePositionForward4Distance(wallPosition, corridorWidth / 2);
                  wallPosition = wallPosition.rotate(180);
                  break;
               default:
                  throw new IllegalStateException("Unknown corridor side'" + corridorSegmentCenter + "'");
            }
            return wallPosition;
         }

         public CorridorBuilder withEndPosition(Shape shape) {
            Position corridorSegmentCenter = currentCorridorSegment.getCorridorSegCenter();
            EndPosition endPosition = EndPositions.of(corridorSegmentCenter, endPositionPrecision);
            this.endPositions.add(endPosition);
            return this;
         }

         private void appendCorridorAngleBend(int signum) {
            Position corridorSegmentCenter = getNextSegmentCenter();

            // First build the angle (bendSegment) itself. It consist of two Corridor elements, like this: _| Note that the vertical element is slightly greater
            Position rectangleCenter = getRectangleCenter(corridorSegmentCenter, signum * 90);
            Wall bendSegment1 = buildWallGridElemenet(grid, wallThickness, segmentLenth, rectangleCenter);

            // Move it at the end of the rectangle above
            rectangleCenter = corridorSegmentCenter.rotate(signum * -90);
            rectangleCenter = Positions.movePositionForward4Distance(corridorSegmentCenter, halfThickness() + segmentLenth / 2);
            Wall bendSegment2 = buildWallGridElemenet(grid, wallThickness, corridorWidth + 2 * wallThickness, rectangleCenter);
            corridorSegmentCenter = corridorSegmentCenter.rotate(signum * -90);
            currentCorridorSegment = new CorridorSegmentImpl(bendSegment1, bendSegment2, corridorSegmentCenter, true);
            corridorSegments.add(currentCorridorSegment);

            // and now add another corridor segment to finish the angle-bend
            corridorSegmentCenter = Positions.movePositionForward4Distance(corridorSegmentCenter, (corridorWidth / 2) - 3.0 * wallThickness);
         }

         public MazeBuilder build() {
            mazeBuilder.corridorSegments.addAll(corridorSegments);
            mazeBuilder.mazeGridElements.addAll(corridorGridElements);
            mazeBuilder.endPositions.addAll(endPositions);
            return mazeBuilder;
         }

         private Position getNextSegmentCenter() {
            if (isNull(currentCorridorSegment)) {
               return Positions.movePositionForward4Distance(mazeStartPos, segmentLenth / 2);
            }
            return Positions.movePositionForward4Distance(currentCorridorSegment.getCorridorSegCenter(), segmentLenth);
         }

         private Position getRectangleCenter(Position corridorSegCenter, double angle2Rotate) {
            return getRectangleCenter(corridorSegCenter, (corridorWidth + wallThickness) / 2, angle2Rotate);
         }

         private Position getRectangleCenter(Position corridorSegCenter, double distance2Move, double angle2Rotate) {
            Position rectangleCenter = corridorSegCenter.rotate(angle2Rotate);
            return Positions.movePositionForward4Distance(rectangleCenter, distance2Move);
         }

         private int halfThickness() {
            return wallThickness / 2;
         }
      }
   }

   private static Wall buildWallGridElemenet(Grid grid, double height, double width, Position center) {
      return WallGridElementBuilder.builder()
            .withGrid(grid)
            .withPosition(center)
            .withShape(RectangleBuilder.builder()
                  .withCenter(center)
                  .withHeight(height)
                  .withWidth(width)
                  .withDistanceBetweenPosOnColDetectionPath(5)
                  .withOrientation(Orientation.HORIZONTAL)
                  .build())
            .build();
   }
}

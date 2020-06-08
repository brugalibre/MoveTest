package com.myownb3.piranha.core.grid.maze;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.application.maze.MazeRunner;
import com.myownb3.piranha.application.maze.MazeRunner.MazeRunnerBuilder;
import com.myownb3.piranha.core.collision.detection.handler.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.PlacedDetector;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.maze.MazeImpl.MazeBuilder;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSegment;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSide;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

class MazeImplTest {

   @Test
   void testBuildMazeRunWithCustomMaze_TurretAndObstacle() {
      // Given
      Position startPos = Positions.of(130, 330);
      int wallThickness = 10;
      int coridorWidth = 80;
      int segmentLength = 80;

      Maze maze = MazeBuilder.builder()
            .withGrid(MirrorGridBuilder.builder()
                  .withMaxX(1000)
                  .withMaxY(1000)
                  .withMinX(30)
                  .withMinY(30)
                  .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
                  .build())
            .withStartPos(startPos)
            .withCorridor(wallThickness)
            .withCorridorWidth(coridorWidth)
            .withSegmentLenth(segmentLength)
            .appendCorridorSegment()
            .withObstacle(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(20)
                  .withCenter(Positions.of(0, 0))
                  .build(), -10, 0)
            .appendCorridorSegment()
            .withTurret(DetectorBuilder.builder()
                  .withAngleInc(1)
                  .withDetectorAngle(180)
                  .withDetectorReach(200)
                  .withEvasionAngle(180)
                  .withEvasionDistance(22)
                  .build(),
                  SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(2)
                        .withGun(BulletGunBuilder.builder()
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(20)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimension(new DimensionImpl(0, 0, 3, 3))
                                          .build())
                                    .withVelocity(4)
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(10)
                                          .withWidth(5)
                                          .withCenter(Positions.of(5, 5))
                                          .withOrientation(Orientation.VERTICAL)
                                          .build())
                                    .withMuzzleBreak(RectangleBuilder.builder()
                                          .withHeight(5 * 1.5)
                                          .withWidth(5 * 1.5)
                                          .withCenter(Positions.of(5, 5))
                                          .withOrientation(Orientation.VERTICAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(5)
                              .withAmountOfPoints(5)
                              .withCenter(Positions.of(5, 5))
                              .build())
                        .build(),
                  CorridorSide.LEFT)
            .build()
            .build();

      // When
      Turret turret = maze.getAllMazeGridElements().stream()
            .filter(Turret.class::isInstance)
            .map(Turret.class::cast)
            .findFirst()
            .get();

      turret.autodetect(); // scanning
      turret.autodetect(); // scanning
      turret.autodetect(); // acquiring

      // Then
      assertThat(turret.getTurretStatus(), is(TurretState.ACQUIRING));
   }

   @Test
   void testGetTurretPosition_TurretIsLefOnWall() {

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
      Position gunCarriagePos = Positions.of(5, 1);
      Position expectedTurretPos = Positions.of(25, 30).rotate(90);

      // When
      Maze maze = MazeBuilder.builder()
            .withGrid(grid)
            .withStartPos(center)
            .withCorridor(wallThickness)
            .withCorridorWidth(coridorWidth)
            .withSegmentLenth(segmentLength)
            .appendCorridorSegment()
            .withTurret(mock(IDetector.class), SimpleGunCarriageBuilder.builder()
                  .withGun(mockGun())
                  .withRotationSpeed(2)
                  .withShape(CircleBuilder.builder()
                        .withAmountOfPoints(5)
                        .withCenter(gunCarriagePos)
                        .withRadius(5)
                        .build())
                  .build(), CorridorSide.LEFT)
            .build()
            .build();

      // Then
      Optional<TurretGridElement> turretGEOptional = maze.getAllMazeGridElements()
            .stream()
            .filter(TurretGridElement.class::isInstance)
            .map(TurretGridElement.class::cast)
            .findFirst();

      assertThat(turretGEOptional.isPresent(), is(true));
      TurretGridElement turretGridElement = turretGEOptional.get();
      assertThat(turretGridElement.getPosition(), is(expectedTurretPos));
   }

   @Test
   void testGetTurretPosition_TurretAngleDiffMinus45() {

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
      Position gunCarriagePos = Positions.of(5, 1);
      double expectedTurretPosAngle = 315;

      // When
      Maze maze = MazeBuilder.builder()
            .withGrid(grid)
            .withStartPos(center)
            .withCorridor(wallThickness)
            .withCorridorWidth(coridorWidth)
            .withSegmentLenth(segmentLength)
            .appendCorridorLeftAngleBend()
            .withTurret(mock(IDetector.class), SimpleGunCarriageBuilder.builder()
                  .withGun(mockGun())
                  .withRotationSpeed(2)
                  .withShape(CircleBuilder.builder()
                        .withAmountOfPoints(5)
                        .withCenter(gunCarriagePos)
                        .withRadius(5)
                        .build())
                  .build(), CorridorSide.LEFT)
            .appendCorridorLeftAngleBend()
            .build()
            .build();

      // Then
      Optional<TurretGridElement> turretGEOptional = maze.getAllMazeGridElements()
            .stream()
            .filter(TurretGridElement.class::isInstance)
            .map(TurretGridElement.class::cast)
            .findFirst();

      assertThat(turretGEOptional.isPresent(), is(true));
      TurretGridElement turretGridElement = turretGEOptional.get();
      assertThat(turretGridElement.getPosition().getDirection().getAngle(), is(expectedTurretPosAngle));
   }

   @Test
   void testGetTurretPosition_TurretAngleLessThenDiffMinus90() {

      // Given
      int wallThickness = 10;
      int coridorWidth = 80;
      int segmentLength = 80;

      Position gunCarriagePos = Positions.of(5, 1);
      double expectedTurretPosAngle = 135;

      Position startPos = Positions.of(100 + 30, 100 + 30);
      Position center = Positions.of(startPos);
      startPos = startPos.rotate(-45);

      // When
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withMovingIncrement(4)
            .withMaze(MazeBuilder.builder()
                  .withEndPositionPrecision(5)
                  .withGrid(MirrorGridBuilder.builder()
                        .withMaxX(1000)
                        .withMaxY(1000)
                        .withMinX(30)
                        .withMinY(30)
                        .build())
                  .withStartPos(center)
                  .withCorridor(wallThickness)
                  .withCorridorWidth(coridorWidth)
                  .withSegmentLenth(segmentLength)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorRightAngleBend()
                  .withTurret(mock(IDetector.class), SimpleGunCarriageBuilder.builder()
                        .withGun(mockGun())
                        .withRotationSpeed(2)
                        .withShape(CircleBuilder.builder()
                              .withAmountOfPoints(5)
                              .withCenter(gunCarriagePos)
                              .withRadius(5)
                              .build())
                        .build(), CorridorSide.LEFT)
                  .appendCorridorSegment()
                  .withDetector(DetectorConfigBuilder.builder()
                        .withDetectorAngle(1)
                        .withDetectorReach(coridorWidth)
                        .withEvasionAngle(0)
                        .withEvasionDistance(0)
                        .build(), CorridorSide.RIGHT)
                  .build()
                  .build())
            .withStartPos(startPos)
            .build();

      // Then
      Optional<TurretGridElement> turretGEOptional = mazeRunner.getMaze().getAllMazeGridElements()
            .stream()
            .filter(TurretGridElement.class::isInstance)
            .map(TurretGridElement.class::cast)
            .findFirst();

      assertThat(turretGEOptional.isPresent(), is(true));
      TurretGridElement turretGridElement = turretGEOptional.get();
      assertThat(turretGridElement.getPosition().getDirection().getAngle(), is(expectedTurretPosAngle));
   }

   private Gun mockGun() {
      Gun gun = mock(Gun.class);
      when(gun.getGunConfig()).thenReturn(mock(GunConfig.class));
      GunShape gunShape = mock(GunShapeImpl.class);
      when(gunShape.getPath()).thenReturn(Collections.emptyList());
      when(gun.getShape()).thenReturn(gunShape);
      return gun;
   }

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
            .appendCorridorSegment()
            .appendCorridorRightAngleBend()
            .appendCorridorSegment()
            .appendCorridorSegment()
            .appendCorridorSegment()
            .withMoveableObstacle(CircleBuilder.builder()
                  .withRadius(4)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(0, 0))
                  .build(), 0, 0, 0)
            .withObstacle(CircleBuilder.builder()
                  .withRadius(4)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(0, 0))
                  .build(), 0, 0)
            .build()
            .build();

      // Then
      boolean hasMoveableObstacle = maze.getAllMazeGridElements()
            .stream()
            .anyMatch(gridElem -> gridElem instanceof Moveable);
      CorridorSegment lastCorridorSegment = maze.getMazeCorridorSegments().get(maze.getMazeCorridorSegments().size() - 1);
      Optional<Circle> circleOpt = getCircleOpt(maze);
      assertThat(circleOpt.isPresent(), is(true));
      Circle circle = circleOpt.get();
      assertThat(circle.getCenter(), is(lastCorridorSegment.getCorridorSegCenter()));
      assertThat(hasMoveableObstacle, is(true));
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
            .appendCorridorSegment()
            .appendCorridorRightAngleBend()
            .appendCorridorSegment()
            .appendCorridorSegment()
            .build()
            .build();
      // Then

      assertThat(maze.getAllMazeGridElements().size(), is(expectedAmountOfGridElements));
   }

   @Test
   void testMazeTest_WithDetectorToTheLeft() {

      // Given
      int wallThickness = 10;
      int coridorWidth = 50;
      int segmentLength = 60;
      Position center = Positions.of(0, 0);
      Position expectedDetectorPos = Positions.of(Directions.W, 25, 60);

      // When
      Maze maze = MazeBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(500)
                  .withMaxY(500)
                  .withMinY(-500)
                  .withMinX(-500)
                  .build())
            .withStartPos(center)
            .withCorridor(wallThickness)
            .withCorridorWidth(coridorWidth)
            .withSegmentLenth(segmentLength)
            .appendCorridorSegment()
            .withDetector(DetectorConfigBuilder.builder()
                  .withDetectorAngle(1)
                  .withDetectorReach(wallThickness)
                  .build(), CorridorSide.LEFT)
            .build()
            .build();

      // Then
      CorridorSegment lastCorridorSegment = maze.getMazeCorridorSegments().get(maze.getMazeCorridorSegments().size() - 1);

      Optional<PlacedDetector> detectorOpt = lastCorridorSegment.getDetector();
      assertThat(detectorOpt.isPresent(), is(true));
      PlacedDetector placedDetector = detectorOpt.get();
      assertThat(placedDetector.getPosition(), is(expectedDetectorPos));
      assertThat(placedDetector.getPosition().getDirection(), is(expectedDetectorPos.getDirection()));
   }

   @Test
   void testMazeTest_WithDetectorToTheRight() {

      // Given
      int wallThickness = 10;
      int coridorWidth = 50;
      int segmentLength = 60;
      Position center = Positions.of(0, 0);
      Position expectedDetectorPos = Positions.of(Directions.O, -25, 60);

      // When
      Maze maze = MazeBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(500)
                  .withMaxY(500)
                  .withMinY(-500)
                  .withMinX(-500)
                  .build())
            .withStartPos(center)
            .withCorridor(wallThickness)
            .withCorridorWidth(coridorWidth)
            .withSegmentLenth(segmentLength)
            .appendCorridorSegment()
            .withDetector(DetectorConfigBuilder.builder()
                  .withDetectorAngle(1)
                  .withDetectorReach(wallThickness)
                  .build(), CorridorSide.RIGHT)
            .build()
            .build();

      // Then
      CorridorSegment lastCorridorSegment = maze.getMazeCorridorSegments().get(maze.getMazeCorridorSegments().size() - 1);

      Optional<PlacedDetector> detectorOpt = lastCorridorSegment.getDetector();
      assertThat(detectorOpt.isPresent(), is(true));
      PlacedDetector placedDetector = detectorOpt.get();
      assertThat(placedDetector.getPosition(), is(expectedDetectorPos));
      assertThat(placedDetector.getPosition().getDirection(), is(expectedDetectorPos.getDirection()));
   }

   @Test
   void testMazeTest_WithDetectorToUnknownSide() {

      // Given
      int wallThickness = 10;
      int coridorWidth = 50;
      int segmentLength = 60;
      Position center = Positions.of(0, 0);

      // When
      Executable exec = () -> {
         MazeBuilder.builder()
               .withGrid(GridBuilder.builder()
                     .withMaxX(500)
                     .withMaxY(500)
                     .withMinY(-500)
                     .withMinX(-500)
                     .build())
               .withStartPos(center)
               .withCorridor(wallThickness)
               .withCorridorWidth(coridorWidth)
               .withSegmentLenth(segmentLength)
               .appendCorridorSegment()
               .withDetector(DetectorConfigBuilder.builder()
                     .withDetectorAngle(1)
                     .withDetectorReach(wallThickness)
                     .build(), CorridorSide.NONE)
               .build()
               .build();
      };
      // Then
      assertThrows(IllegalStateException.class, exec);
   }

   private static Optional<Circle> getCircleOpt(Maze maze) {
      return maze.getAllMazeGridElements().stream()
            .map(GridElement::getShape)
            .filter(shape -> shape instanceof Circle)
            .map(Circle.class::cast)
            .findFirst();
   }
}

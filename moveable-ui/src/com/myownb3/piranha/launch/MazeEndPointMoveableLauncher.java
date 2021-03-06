/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getPositionListColor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.maze.MazeRunner;
import com.myownb3.piranha.application.maze.MazeRunner.MazeRunnerBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.DefaultGunCarriageImpl;
import com.myownb3.piranha.core.battle.weapon.guncarriage.DefaultGunCarriageImpl.DefaultGunCarriageBuilder;
import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.PlacedDetector;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierImpl.LightBarrierBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.maze.Maze;
import com.myownb3.piranha.core.grid.maze.MazeImpl.MazeBuilder;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSegment;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSide;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.impl.UILogicUtil;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;
import com.myownb3.piranha.ui.render.impl.detector.DetectorPainter;
import com.myownb3.piranha.ui.render.impl.detector.DetectorPainterConfig;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainterConfig;
import com.myownb3.piranha.worker.WorkerThreadFactory;

/**
 * @author Dominic
 *
 */
public class MazeEndPointMoveableLauncher {
   private static int padding = 30;

   public static void main(String[] args) throws InterruptedException {

      MazeEndPointMoveableLauncher launcher = new MazeEndPointMoveableLauncher();
      launcher.launch();
   }

   @SuppressWarnings({"unchecked", "rawtypes"})
   private void launch() {

      int wallThickness = 10;
      int coridorWidth = 80;
      int segmentLength = 80;
      int circleRadius = 4;

      int evasionAngle = 60;
      int detectorAngle = 60;

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(120)
            .withDetectorAngle(180)
            .build();

      Position startPos = Positions.of(100 + padding, 100 + padding);
      Position center = Positions.of(startPos);
      startPos = startPos.rotate(-45);
      PostMoveForwardHandlerCtx ctx = new PostMoveForwardHandlerCtx();
      MazePostMoveForwardHandler postMoveFowardHandler = new MazePostMoveForwardHandler(ctx);
      double gunHeight = 5;
      double gunWidth = 10;
      int gunCarriageRadius = 8;
      DetectorImpl turretScanner = DetectorBuilder.builder()
            .withAngleInc(detectorConfig.getEvasionAngleInc())
            .withDetectorAngle(detectorConfig.getDetectorAngle())
            .withDetectorReach(detectorConfig.getDetectorReach())
            .withEvasionAngle(detectorConfig.getDetectorAngle())
            .withEvasionDistance(detectorConfig.getEvasionDistance())
            .build();
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withMovingIncrement(4)
            .withMaze(MazeBuilder.builder()
                  .withEndPositionPrecision(5)
                  .withGrid(MirrorGridBuilder.builder()
                        .withMaxX(1000)
                        .withMaxY(1000)
                        .withMinX(padding)
                        .withMinY(padding)
                        .withCollisionDetectionHandler(BouncingCollisionDetectionHandlerBuilder.builder()
                              .withBouncedPositionEvaluator(new BouncedPositionEvaluatorImpl())
                              .build())
                        .build())
                  .withStartPos(center)
                  .withCorridor(wallThickness)
                  .withCorridorWidth(coridorWidth)
                  .withSegmentLenth(segmentLength)
                  /// Maze Bsp. 2
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .withObstacle(CircleBuilder.builder()
                        .withRadius(circleRadius)
                        .withAmountOfPoints(circleRadius)
                        .withCenter(Positions.of(0, 0))
                        .build(), 0, 0)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .withTurret(turretScanner, buildGunCarriage(gunHeight, gunWidth, gunCarriageRadius), CorridorSide.RIGHT)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorRightAngleBend()
                  .withTurret(turretScanner, buildGunCarriage(gunHeight, gunWidth, gunCarriageRadius), CorridorSide.LEFT)
                  .appendCorridorSegment()
                  .appendCorridorRightAngleBend()
                  .withTurret(turretScanner, buildGunCarriage(gunHeight, gunWidth, gunCarriageRadius), CorridorSide.RIGHT)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .withDetector(DetectorConfigBuilder.builder()
                        .withDetectorAngle(1)
                        .withDetectorReach(coridorWidth)
                        .withEvasionAngle(0)
                        .withEvasionDistance(0)
                        .build(), CorridorSide.RIGHT)

                  //// Maze Bsp. 1
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorSegment()
                  //                  .withObstacle(CircleBuilder.builder()
                  //                        .withRadius(circleRadius)
                  //                        .withAmountOfPoints(circleRadius)
                  //                        .withCenter(Positions.of(0, 0))
                  //                        .build(), 0, 0)
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorLeftAngleBend()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorLeftAngleBend()
                  //                  .appendCorridorLeftAngleBend()
                  //                  .appendCorridorRightAngleBend()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorRightAngleBend()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorRightAngleBend()
                  //                  .appendCorridorSegment()
                  //                  .appendCorridorSegment()
                  .build()
                  .build())

            // With this detector settings, everything runs without End-Positions
            .withEvasionStateMachineConfig(90, 50)
            .withTrippleDetectorCluster(DetectorConfigBuilder.builder()
                  .withDetectorReach(90)
                  .withEvasionDistance(55)
                  .withDetectorAngle(detectorAngle)
                  .withEvasionAngle(evasionAngle)
                  .withEvasionAngleInc(1)
                  .build(),
                  DetectorConfigBuilder.builder()
                        .withDetectorReach(60)
                        .withEvasionDistance(25)
                        .withDetectorAngle(detectorAngle)
                        .withEvasionAngle(evasionAngle)
                        .withEvasionAngleInc(1)
                        .build())
            .withStartPos(startPos)
            .withMovingIncrement(600)
            .withMoveableController(postMoveFowardHandler)
            .build();

      Grid grid = mazeRunner.getGrid();
      grid.prepare();
      WorkerThreadFactory.INSTANCE.restart();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, 10);
      ctx.setMainWindow(mainWindow);
      ctx.setGrid(grid);
      postMoveFowardHandler.setLightBarrier(LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(postMoveFowardHandler)
            .withPlacedDetector(evalDetector(mazeRunner.getMaze()))
            .build());

      MoveableController moveableController = mazeRunner.getMoveableController();
      ctx.getGridElements().addAll(mazeRunner.getAllGridElements());
      ctx.getGridElements().add(moveableController.getMoveable());
      ctx.setMoveableController(moveableController);
      List<PlacedDetector> detectors = mazeRunner.getMaze().getMazeCorridorSegments()
            .stream()
            .map(CorridorSegment::getDetector)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
      ctx.getRenderers().addAll(getRenderers(grid, ctx.getGridElements(), moveableController.getMoveable(), mazeRunner.getDetectorCluster(),
            mazeRunner.getConfig(), detectors));
      ctx.getEndPositionRenderers().add(new PositionListPainter(Collections.emptyList(), getPositionListColor(), 4, 4));
      ctx.addPostMoveForwardLogicHandler();

      mainWindow.addSpielfeld((List) ctx.getRenderers(), ctx.getEndPositionRenderers(), grid);
      showGuiAndStartPainter(mainWindow);
      List<Position> positions = mazeRunner.run();
      preparePositionListPainter(ctx.getEndPositionRenderers(), positions);
   }


   private DefaultGunCarriageImpl buildGunCarriage(double gunHeight, double gunWidth, int gunCarriageRadius) {
      return DefaultGunCarriageBuilder.builder()
            .withRotationSpeed(2)
            .withGun(DefaultGunBuilder.builder()
                  .withGunProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withSalveSize(1)
                        .withRoundsPerMinute(70)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                              .withVelocity(3)
                              .build())
                        .build())
                  .withGunShape(GunShapeBuilder.builder()
                        .withBarrel(RectangleBuilder.builder()
                              .withHeight(gunHeight)
                              .withWidth(gunWidth)
                              .withCenter(Positions.of(5, 5))
                              .withOrientation(Orientation.VERTICAL)
                              .build())
                        .withMuzzleBrake(RectangleBuilder.builder()
                              .withHeight(gunHeight * 1.5)
                              .withWidth(gunHeight * 1.5)
                              .withCenter(Positions.of(5, 5))
                              .withOrientation(Orientation.VERTICAL)
                              .build())
                        .build())
                  .build())
            .withShape(CircleBuilder.builder()
                  .withRadius(gunCarriageRadius)
                  .withAmountOfPoints(gunCarriageRadius)
                  .withCenter(Positions.of(5, 5))
                  .build())
            .build();
   }

   private PlacedDetector evalDetector(Maze maze) {
      return maze.getMazeCorridorSegments()
            .stream()
            .map(CorridorSegment::getDetector)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .get();
   }

   private static void preparePositionListPainter(List<PositionListPainter> renderers, List<Position> positions) {
      renderers.stream()
            .filter(PositionListPainter.class::isInstance)
            .map(PositionListPainter.class::cast)
            .forEach(renderer -> renderer.setPositions(positions));
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow) {
      SwingUtilities.invokeLater(() -> mainWindow.show());
      UILogicUtil.startUIRefresher(mainWindow, 25);
   }

   private static List<Renderer<? extends GridElement>> getRenderers(Grid grid, List<GridElement> gridElements, Moveable moveable,
         TrippleDetectorCluster detectorCluster,
         EvasionStateMachineConfig config, List<PlacedDetector> corridorDetectors) {
      List<Renderer<? extends GridElement>> renderers = gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement)))
            .collect(Collectors.toList());
      MoveablePainterConfig moveablePainterConfig = MoveablePainterConfig.of(detectorCluster, config, false, false);
      renderers.add(new MoveablePainter(moveable, getColor(moveable), moveablePainterConfig));
      renderers.addAll(corridorDetectors.stream()
            .map(buildDetectorPainter(grid))
            .collect(Collectors.toList()));
      return renderers;
   }

   private static Function<? super PlacedDetector, ? extends DetectorPainter> buildDetectorPainter(Grid grid) {
      return corridorDetector -> {
         IDetector detector = corridorDetector.getDetector();
         return new DetectorPainter(SimpleGridElementBuilder.builder()
               .withGrid(grid)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(corridorDetector.getPosition())
                     .build())
               .build(), getPositionListColor(),
               DetectorPainterConfig.of(Optional.empty(), DetectorConfigBuilder.builder()
                     .withDetectorAngle(detector.getDetectorAngle())
                     .withDetectorReach(detector.getDetectorRange())
                     .withEvasionAngle(detector.getEvasionAngle())
                     .withEvasionDistance(detector.getEvasionRange())
                     .build()));
      };
   }
}

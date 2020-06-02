/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getPositionListColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.maze.MazeRunner;
import com.myownb3.piranha.application.maze.MazeRunner.MazeRunnerBuilder;
import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.PlacedDetector;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierImpl.LightBarrierBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.maze.Maze;
import com.myownb3.piranha.core.grid.maze.MazeImpl.MazeBuilder;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSegment;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSide;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.launch.DefaultPostMoveForwardHandler.MainWindowHolder;
import com.myownb3.piranha.launch.DefaultPostMoveForwardHandler.MoveableControllerHolder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;
import com.myownb3.piranha.ui.render.impl.detector.DetectorPainter;
import com.myownb3.piranha.ui.render.impl.detector.DetectorPainterConfig;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainterConfig;

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

   private void launch() throws InterruptedException {

      int wallThickness = 10;
      int coridorWidth = 80;
      int segmentLength = 80;
      int circleRadius = 4;

      int evasionAngle = 60;
      int detectorAngle = 60;

      Position startPos = Positions.of(100 + padding, 100 + padding);
      Position center = Positions.of(startPos);
      startPos = startPos.rotate(-45);
      List<MainWindow> mainWindows = new ArrayList<>();
      List<Renderer> renderers = new ArrayList<>();
      MainWindowHolder mainWindowHolder = new MainWindowHolder();
      MoveableControllerHolder moveableControllerHolder = new MoveableControllerHolder();
      List<GridElement> gridElements = new ArrayList<>();
      MazePostMoveForwardHandler postMoveFowardHandler =
            new MazePostMoveForwardHandler(mainWindowHolder, moveableControllerHolder, gridElements, renderers);
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
                  .withMoveableObstacle(CircleBuilder.builder()
                        .withRadius(circleRadius)
                        .withAmountOfPoints(circleRadius)
                        .withCenter(Positions.of(0, 0))
                        .build(), 0, 0, -25)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorRightAngleBend()
                  .appendCorridorRightAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  //                  .withObstacle(CircleBuilder.builder()
                  //                        .withRadius(circleRadius)
                  //                        .withAmountOfPoints(circleRadius)
                  //                        .withCenter(Positions.of(0, 0))
                  //                        .build(), -10, 0)
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorRightAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorRightAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .withDetector(DetectorConfigBuilder.builder()
                        .withDetectorAngle(1)
                        .withDetectorReach(coridorWidth)
                        .withEvasionAngle(0)
                        .withEvasionDistance(0)
                        .build(), CorridorSide.LEFT)

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
            .withMovingIncrement(2)
            .withMoveableController(postMoveFowardHandler)
            .build();

      Grid grid = mazeRunner.getGrid();
      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, 10);
      mainWindowHolder.setMainWindow(mainWindow);
      mainWindows.add(mainWindow);
      postMoveFowardHandler.setLightBarrier(LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(postMoveFowardHandler)
            .withPlacedDetector(evalDetector(mazeRunner.getMaze()))
            .build());

      gridElements.addAll(mazeRunner.getAllGridElements());

      MoveableController moveableController = mazeRunner.getMoveableController();
      moveableControllerHolder.setMoveableController(moveableController);
      List<PlacedDetector> detectors = mazeRunner.getMaze().getMazeCorridorSegments()
            .stream()
            .map(CorridorSegment::getDetector)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
      renderers.addAll(getRenderers(grid, circleRadius, gridElements, moveableController.getMoveable(), mazeRunner.getDetectorCluster(),
            mazeRunner.getConfig(), detectors));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow);
      List<Position> positions = mazeRunner.run();
      preparePositionListPainter(renderers, positions);
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

   private static void preparePositionListPainter(List<Renderer> renderers, List<Position> positions) {
      renderers.stream()
            .filter(PositionListPainter.class::isInstance)
            .map(PositionListPainter.class::cast)
            .forEach(renderer -> renderer.setPositions(positions));
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow) {
      SwingUtilities.invokeLater(() -> mainWindow.show());
      new Thread(() -> {
         while (true) {
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            try {
               Thread.sleep(1);
            } catch (InterruptedException e) {
            }
         }
      }).start();
   }

   private static List<Renderer> getRenderers(Grid grid, int height, List<GridElement> gridElements, Moveable moveable,
         TrippleDetectorCluster detectorCluster, EvasionStateMachineConfig config, List<PlacedDetector> corridorDetectors) {
      List<Renderer> renderers = gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, height))
            .collect(Collectors.toList());
      renderers.add(new PositionListPainter(Collections.emptyList(), getPositionListColor(), height, height));
      MoveablePainterConfig moveablePainterConfig = MoveablePainterConfig.of(detectorCluster, config, true, false);
      renderers.add(new MoveablePainter(moveable, getColor(moveable), height, height, moveablePainterConfig));
      renderers.addAll(corridorDetectors.stream()
            .map(buildDetectorPainter(grid, height))
            .collect(Collectors.toList()));
      return renderers;
   }

   private static Function<? super PlacedDetector, ? extends DetectorPainter> buildDetectorPainter(Grid grid, int height) {
      return corridorDetector -> {
         IDetector detector = corridorDetector.getDetector();
         return new DetectorPainter(SimpleGridElementBuilder.builder()
               .withGrid(grid)
               .withPosition(corridorDetector.getPosition())
               .build(), getPositionListColor(), height, height,
               DetectorPainterConfig.of(Optional.empty(), DetectorConfigBuilder.builder()
                     .withDetectorAngle(detector.getDetectorAngle())
                     .withDetectorReach(detector.getDetectorRange())
                     .withEvasionAngle(detector.getEvasionAngle())
                     .withEvasionDistance(detector.getEvasionRange())
                     .build()));
      };
   }
}

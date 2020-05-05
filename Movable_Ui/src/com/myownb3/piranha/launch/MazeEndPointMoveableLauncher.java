/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getPositionListColor;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.maze.MazeRunner;
import com.myownb3.piranha.application.maze.MazeRunner.MazeRunnerBuilder;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.detector.collision.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.grid.maze.MazeImpl.MazeBuilder;
import com.myownb3.piranha.launch.DefaultPostMoveForwardHandler.MainWindowHolder;
import com.myownb3.piranha.launch.DefaultPostMoveForwardHandler.MoveableControllerHolder;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;
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
      EndPosition endPos = EndPositions.of(850, 900);
      Position startPos = Positions.of(100 + padding, 100 + padding);
      Position center = Positions.of(startPos);
      startPos.rotate(-45);
      List<MainWindow> mainWindows = new ArrayList<>();
      List<Renderer> renderers = new ArrayList<>();
      MainWindowHolder mainWindowHolder = new MainWindowHolder();
      MoveableControllerHolder moveableControllerHolder = new MoveableControllerHolder();
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withMovingIncrement(3)
            .withMaze(MazeBuilder.builder()
                  .withEndPositionPrecision(5)
                  .withGrid(MirrorGridBuilder.builder()
                        .withMaxX(1000)
                        .withMaxY(1000)
                        .withMinX(padding)
                        .withMinY(padding)
                        .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
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
                  .appendCorridorSegment()
                  .appendCorridorRightAngleBend()
                  .appendCorridorRightAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
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
            .withTrippleDetectorCluster(55, 35)
            .withStartPos(startPos)
            .withMovingIncrement(2)
            .withMoveableController(new MazePostMoveForwardHandler(endPos, mainWindowHolder, moveableControllerHolder, emptyList(), renderers))
            .build();

      Grid grid = mazeRunner.getGrid();
      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, 10);
      mainWindowHolder.setMainWindow(mainWindow);
      mainWindows.add(mainWindow);

      List<GridElement> gridElements = mazeRunner.getAllGridElements();

      MoveableController moveableController = mazeRunner.getMoveableController();
      moveableControllerHolder.setMoveableController(moveableController);
      renderers.addAll(getRenderers(grid, circleRadius, circleRadius, gridElements, moveableController.getMoveable(), mazeRunner.getDetectorCluster(),
            mazeRunner.getConfig()));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow);
      List<Position> positions = mazeRunner.run();
      preparePositionListPainter(renderers, positions);
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

   private static List<Renderer> getRenderers(Grid grid, int height, int width,
         List<GridElement> gridElements, Moveable moveable, TrippleDetectorCluster detectorCluster, EvasionStateMachineConfig config) {
      List<Renderer> renderers = gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))
            .collect(Collectors.toList());
      renderers.add(new PositionListPainter(Collections.emptyList(), getPositionListColor(), height, width));
      MoveablePainterConfig moveablePainterConfig = MoveablePainterConfig.of(detectorCluster, config, true, false);
      renderers.add(new MoveablePainter(moveable, getColor(moveable), height, width, moveablePainterConfig));
      return renderers;
   }
}

/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.awt.Toolkit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.random.RandomMoveableWithEndPositionRunner;
import com.myownb3.piranha.application.random.RandomMoveableWithEndPositionRunner.RandomRunnerWithEndPositionsBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositionGridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.AbstractGridElementPainter;
import com.myownb3.piranha.ui.render.impl.EndPositionGridElementPainter;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainterConfig;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class RandomMoveableLauncherWithEndPoint implements Stoppable {
   public boolean isRunning = true;

   private static int padding = 30;

   public static void main(String[] args) throws InterruptedException {

      int height = 4;
      int width = 4;
      int mainWindowWidth = 700;
      int mainWindowHeight = 700;

      RandomMoveableLauncherWithEndPoint randomMoveableLauncherWithEndPoint = new RandomMoveableLauncherWithEndPoint();
      randomMoveableLauncherWithEndPoint.launch(height, width, mainWindowWidth, mainWindowHeight);
   }

   private void launch(int height, int width, int mainWindowWidth, int mainWindowHeight) throws InterruptedException {
      MainWindow mainWindow = new MainWindow(mainWindowWidth, mainWindowHeight, padding, height);

      CollisionDetectionHandlerImpl collisionDetectionHandler = new CollisionDetectionHandlerImpl(this, mainWindow);
      Dimension dimension = new DimensionImpl(padding, padding, mainWindowWidth, mainWindowWidth);

      int detectorReach = 90;
      int evasionDistance = 80;
      Position startPos = Positions.getRandomPosition(dimension, height, width);

      // Helper variables for later access
      PostMoveForwardHandlerCtx ctx = new PostMoveForwardHandlerCtx();

      int amountOfEndPos = (int) MathUtil.getRandom(2) + (int) MathUtil.getRandom(20);

      RandomMoveableWithEndPositionRunner endPositionRunner = RandomRunnerWithEndPositionsBuilder.builder()
            .withGrid(MirrorGridBuilder.builder()
                  .withMaxX(dimension.getHeight())
                  .withMaxY(dimension.getWidth())
                  .withMinX(dimension.getX())
                  .withMinY(dimension.getY())
                  .withCollisionDetectionHandler(collisionDetectionHandler)
                  .build())
            .withStartPos(startPos)
            .withRandomEndPositions(amountOfEndPos)
            .withCircleRadius(width)
            .withRandomMoveableObstacles(20)
            .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                  .withReturningAngleIncMultiplier(1)
                  .withOrientationAngle(1)
                  .withReturningMinDistance(0.06)
                  .withReturningAngleMargin(0.7d)
                  .withPassingDistance(25)
                  .withPostEvasionReturnAngle(4)
                  .withDetectorConfig(DetectorConfigBuilder.builder()
                        .withDetectorReach(detectorReach)
                        .withEvasionDistance(evasionDistance)
                        .withDetectorAngle(180)
                        .withEvasionAngle(16)
                        .withEvasionAngleInc(1)
                        .build())
                  .build())
            .withSideDetectorConfig(DetectorConfigBuilder.builder()
                  .withDetectorReach(45)
                  .withEvasionDistance(25)
                  .withDetectorAngle(60)
                  .withEvasionAngle(45)
                  .withEvasionAngleInc(1)
                  .build())
            .withMoveableController(new DefaultPostMoveForwardHandler(ctx))
            .build();

      Grid grid = endPositionRunner.getGrid();
      grid.prepare();
      ctx.setGrid(grid);
      MoveableController moveableController = endPositionRunner.getMoveableController();
      ctx.getGridElements().addAll(endPositionRunner.getAllGridElements());
      ctx.setMoveableController(moveableController);
      collisionDetectionHandler.setMoveableController(moveableController);

      ctx.getRenderers().addAll(getRenderers(height, width, grid, ctx.getGridElements(), moveableController.getMoveable(),
            endPositionRunner.getConfig(), endPositionRunner.getDetectorCluster()));
      mainWindow.addSpielfeld(ctx.getRenderers(), grid);
      SwingUtilities.invokeLater(() -> mainWindow.show());
      ctx.setMainWindow(mainWindow);
      prepareAndMoveMoveables(endPositionRunner, mainWindow, ctx.getGridElements(), grid);
   }

   private void prepareAndMoveMoveables(RandomMoveableWithEndPositionRunner endPositionRunner, MainWindow mainWindow,
         List<GridElement> allGridElements, Grid grid) throws InterruptedException {
      grid.prepare();
      turnGridElements(allGridElements);
      while (isRunning) {
         endPositionRunner.run();
         Toolkit.getDefaultToolkit().beep();
         Thread.sleep(5);
      }
   }

   private static void turnGridElements(List<GridElement> allGridElements) {
      allGridElements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast)
            .forEach(obstacle -> obstacle.makeTurn(MathUtil.getRandom(360)));
   }

   private static List<Renderer> getRenderers(int height, int width, Grid grid, List<GridElement> gridElements,
         Moveable moveable, EvasionStateMachineConfig config, TrippleDetectorCluster detectorCluster) {
      List<Renderer> renderers = getRenderers(gridElements);
      MoveablePainterConfig painterConfig = MoveablePainterConfig.of(detectorCluster, config, true, false);
      renderers.add(new MoveablePainter(moveable, getColor(moveable), 1, 1, painterConfig));
      return renderers;
   }

   private static <T extends GridElement> List<Renderer> getRenderers(List<GridElement> gridElements) {
      return gridElements.stream()
            .map(toGridElementPainter())
            .collect(Collectors.toList());
   }

   private static Function<? super GridElement, ? extends AbstractGridElementPainter<?>> toGridElementPainter() {
      return gridElement -> {
         if (gridElement instanceof EndPositionGridElement) {
            return new EndPositionGridElementPainter(gridElement, getColor(gridElement), 1, 1);
         }
         return new GridElementPainter(gridElement, getColor(gridElement), 1, 1);
      };
   }

   @Override
   public boolean isRunning() {
      return isRunning;
   }

   @Override
   public void stop() {
      isRunning = false;
   }
}

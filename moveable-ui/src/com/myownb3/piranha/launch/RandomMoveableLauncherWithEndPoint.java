/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

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
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl;
import com.myownb3.piranha.core.grid.position.EndPositionGridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.impl.UILogicUtil;
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

   public static void main(String[] args) {

      int height = 4;
      int width = 4;
      int mainWindowWidth = 700;
      int mainWindowHeight = 700;

      RandomMoveableLauncherWithEndPoint randomMoveableLauncherWithEndPoint = new RandomMoveableLauncherWithEndPoint();
      randomMoveableLauncherWithEndPoint.launch(height, width, mainWindowWidth, mainWindowHeight);
   }

   @SuppressWarnings({"unchecked", "rawtypes"})
   private void launch(int height, int width, int mainWindowWidth, int mainWindowHeight) {
      MainWindow mainWindow = new MainWindow(mainWindowWidth, mainWindowHeight, padding, height);

      CollisionDetectionHandlerImpl collisionDetectionHandler = new CollisionDetectionHandlerImpl(this, mainWindow);
      Dimension dimension = new DimensionImpl(padding, padding, mainWindowWidth, mainWindowWidth);

      int detectorReach = 90;
      int evasionDistance = 80;

      // Helper variables for later access
      PostMoveForwardHandlerCtx ctx = new PostMoveForwardHandlerCtx();

      int amountOfEndPos = (int) MathUtil.getRandom(2) + (int) MathUtil.getRandom(20);

      DefaultPostMoveForwardHandler moveablePostActionHandler = new DefaultPostMoveForwardHandler(ctx);
      MirrorGrid grid = MirrorGridBuilder.builder()
            .withMaxX(dimension.getHeight())
            .withMaxY(dimension.getWidth())
            .withMinX(dimension.getX())
            .withMinY(dimension.getY())
            .withCollisionDetectionHandler(collisionDetectionHandler)
            .build();
      Position startPos = grid.getRandomPosition(width);
      RandomMoveableWithEndPositionRunner endPositionRunner = RandomRunnerWithEndPositionsBuilder.builder()
            .withGrid(grid)
            .withStartPos(startPos)
            .withRandomEndPositions(amountOfEndPos)
            .withCircleRadius(width)
            .withRandomMoveableObstacles(30)
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
                        .withDetectorAngle(60)
                        .withEvasionAngle(60)
                        .withEvasionAngleInc(1)
                        .build())
                  .build())
            .withSideDetectorConfig(DetectorConfigBuilder.builder()
                  .withDetectorReach(45)
                  .withEvasionDistance(25)
                  .withDetectorAngle(60)
                  .withEvasionAngle(60)
                  .withEvasionAngleInc(1)
                  .build())
            .withDefaultDetectorCluster()
            .withMovingIncrement(18)
            .withMoveableController(move -> true)
            .build();

      grid.prepare();
      ctx.setGrid(grid);
      MoveableController moveableController = endPositionRunner.getMoveableController();
      ctx.getGridElements().addAll(endPositionRunner.getAllGridElements());
      ctx.setMoveableController(moveableController);
      collisionDetectionHandler.setMoveableController(moveableController);

      ctx.getRenderers().addAll(getRenderers(ctx.getGridElements(), moveableController.getMoveable(),
            endPositionRunner.getConfig(), endPositionRunner.getDetectorCluster()));
      mainWindow.addSpielfeld(((List) ctx.getRenderers()), grid);
      ctx.setMainWindow(mainWindow);
      ctx.addPostMoveForwardLogicHandler();
      showGuiAndStartPainter(mainWindow);
      prepareAndMoveMoveables(endPositionRunner, moveablePostActionHandler, ctx.getGridElements(), grid);
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow) {
      SwingUtilities.invokeLater(() -> mainWindow.show());
      UILogicUtil.startUIRefresher(mainWindow, 25);
   }

   private void prepareAndMoveMoveables(RandomMoveableWithEndPositionRunner endPositionRunner,
         DefaultPostMoveForwardHandler moveablePostActionHandler, List<GridElement> allGridElements, Grid grid) {
      grid.prepare();
      turnGridElements(allGridElements);
      new Thread(() -> {
         long cycleTime = 15;
         while (isRunning) {
            endPositionRunner.run();
            moveablePostActionHandler.handlePostConditions(endPositionRunner.getMoveableController().getMoveable());

            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
         }
      }, "LogicHandler").start();
   }

   private static void turnGridElements(List<GridElement> allGridElements) {
      allGridElements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast)
            .forEach(obstacle -> obstacle.makeTurn(MathUtil.getRandom(360)));
   }

   private static List<Renderer<? extends GridElement>> getRenderers(List<GridElement> gridElements,
         Moveable moveable, EvasionStateMachineConfig config, TrippleDetectorCluster detectorCluster) {
      List<Renderer<? extends GridElement>> renderers = getRenderers(gridElements);
      MoveablePainterConfig painterConfig = MoveablePainterConfig.of(detectorCluster, config, true, false);
      renderers.add(new MoveablePainter(moveable, getColor(moveable), painterConfig));
      return renderers;
   }

   private static <T extends GridElement> List<Renderer<? extends GridElement>> getRenderers(List<GridElement> gridElements) {
      return gridElements.stream()
            .map(toGridElementPainter())
            .collect(Collectors.toList());
   }

   private static Function<? super GridElement, ? extends AbstractGridElementPainter<?>> toGridElementPainter() {
      return gridElement -> {
         if (gridElement instanceof EndPositionGridElement) {
            return new EndPositionGridElementPainter(gridElement, getColor(gridElement));
         }
         return new GridElementPainter(gridElement, getColor(gridElement));
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

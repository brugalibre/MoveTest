/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.launch.DefaultPostMoveForwardHandler.getPostMoveFowardHandler;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.moveables.MovingStrategie;
import com.myownb3.piranha.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.grid.gridelement.EndPositionGridElement;
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
   public static class RandomMoveableLauncherWithEndPointData {
      public boolean isRunning;

      public RandomMoveableLauncherWithEndPointData(boolean isRunning) {
         this.isRunning = isRunning;
      }
   }

   private static int padding = 30;
   private RandomMoveableLauncherWithEndPointData data = new RandomMoveableLauncherWithEndPointData(true);

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

      MirrorGrid grid = buildMirrorGrid(mainWindow, mainWindowWidth, collisionDetectionHandler);
      Position startPos = Positions.getRandomPosition(grid.getDimension(), height, width);
      List<EndPosition> endPosList = getEndPosList(height, width, grid);
      List<GridElement> gridElements = getAllGridElements(grid, endPosList, height, width);
      List<Renderer> renderers = new ArrayList<>();
      List<MoveableController> moveableControllerList = new ArrayList<>();
      int detectorReach = 40;
      int evasionDistance = 2 * detectorReach / 3;
      EvasionStateMachineConfig config = buildEvasionStateMachineConfig(detectorReach, evasionDistance);
      TrippleDetectorCluster detectorCluster = buildDefaultDetectorCluster(config);
      MoveableController moveableController = buildMoveableController(grid, startPos, endPosList,
            getPostMoveFowardHandler(mainWindow, moveableControllerList, gridElements, renderers), width, config, detectorCluster);
      moveableControllerList.add(moveableController);
      collisionDetectionHandler.setMoveableController(moveableController);
      renderers.addAll(getRenderers(height, width, grid, gridElements, moveableController.getMoveable(), config, detectorCluster));

      mainWindow.addSpielfeld(renderers, grid);
      SwingUtilities.invokeLater(() -> mainWindow.show());

      prepareAndMoveMoveables(moveableController, mainWindow, gridElements, grid);
   }

   private static EvasionStateMachineConfig buildEvasionStateMachineConfig(int detectorReach, int evasionDistance) {
      return EvasionStateMachineConfigBuilder.builder()
            .withReturningAngleIncMultiplier(1)
            .withOrientationAngle(1)
            .withReturningMinDistance(0.06)
            .withReturningAngleMargin(0.7d)
            .withDetectorReach(detectorReach)
            .withEvasionDistance(evasionDistance)
            .withPassingDistance(25)
            .withDetectorAngle(60)
            .withEvasionAngle(45)
            .withEvasionAngleInc(1)
            .withPostEvasionReturnAngle(4)
            .build();
   }

   private static MirrorGrid buildMirrorGrid(MainWindow mainWindow, int mainWindowWidth, CollisionDetectionHandler collisionDetector) {
      return MirrorGridBuilder.builder()
            .withMaxX(mainWindowWidth - padding)
            .withMaxY(mainWindowWidth - padding)
            .withMinY(padding)
            .withMinX(padding)
            .withCollisionDetectionHandler(collisionDetector)
            .build();
   }

   private TrippleDetectorCluster buildDefaultDetectorCluster(EvasionStateMachineConfig centerDetectorConfig) {

      EvasionStateMachineConfig sideDetectorConfig = buildEvasionStateMachineConfig(25, 10);
      return TrippleDetectorClusterBuilder.buildDefaultDetectorCluster(centerDetectorConfig, sideDetectorConfig);
   }

   private static MoveableController buildMoveableController(MirrorGrid grid, Position startPos,
         List<EndPosition> endPosList, PostMoveForwardHandler postMoveFowardHandler, int width, EvasionStateMachineConfig config, Detector detector) {
      return MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategie.FORWARD)
            .withEndPositions(endPosList)
            .withPostMoveForwardHandler(postMoveFowardHandler)
            .withEndPointMoveable()
            .withGrid(grid)
            .withStartPosition(startPos)
            .withHandler(new EvasionStateMachine(detector, config))
            .withShape(buildCircle(width, startPos))
            .withMovingIncrement(2)
            .buildAndReturnParentBuilder()
            .build();//
   }

   private static Shape buildCircle(int width, Position pos) {
      return CircleBuilder.builder()
            .withRadius(width)
            .withAmountOfPoints(5)
            .withCenter(pos)
            .build();//
   }

   private static List<EndPosition> getEndPosList(int height, int width, MirrorGrid grid) {
      int amountOfEndPos = (int) MathUtil.getRandom(2) + (int) MathUtil.getRandom(20);
      List<EndPosition> endPosList = new ArrayList<>(amountOfEndPos);
      for (int i = 0; i < amountOfEndPos; i++) {
         endPosList.add(EndPositions.of(Positions.getRandomPosition(grid.getDimension(), height, width)));
      }
      return endPosList;
   }

   private static List<GridElement> getAllGridElements(DefaultGrid grid, List<EndPosition> endPosList, int height,
         int width) {
      List<GridElement> allGridElement = endPosList.stream()
            .map(endPos -> new EndPositionGridElement(grid, endPos, buildCircle(width, endPos)))
            .collect(Collectors.toList());

      int amount = 20;
      //      int amount = 80;
      for (int i = 0; i < amount; i++) {
         Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
         Obstacle obstacle = new MoveableObstacleImpl(grid, randomPosition, buildCircle(width, randomPosition));

         allGridElement.add(obstacle);
      }

      return allGridElement;
   }

   private void prepareAndMoveMoveables(MoveableController moveableController, MainWindow mainWindow,
         List<GridElement> allGridElements, Grid grid) throws InterruptedException {
      grid.prepare();
      turnGridElements(allGridElements);
      while (data.isRunning) {
         moveableController.leadMoveable();
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

   private static List<Renderer> getRenderers(int height, int width, MirrorGrid grid, List<GridElement> gridElements,
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
      return gridElement -> gridElement instanceof EndPositionGridElement
            ? new EndPositionGridElementPainter(gridElement, getColor(gridElement), 1, 1)
            : new GridElementPainter(gridElement, getColor(gridElement), 1, 1);
   }


   @Override
   public boolean isRunning() {
      return data.isRunning;
   }

   @Override
   public void stop() {
      data.isRunning = false;
   }
}

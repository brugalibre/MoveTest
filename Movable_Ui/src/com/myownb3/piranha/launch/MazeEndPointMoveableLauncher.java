/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.launch.DefaultPostMoveForwardHandler.getPostMoveFowardHandler;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getPositionListColor;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.moveables.MovingStrategie;
import com.myownb3.piranha.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigBuilder;
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
      DefaultGrid grid = buildGrid(new DefaultCollisionDetectionHandlerImpl());
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding,
            10);

      List<MoveableController> moveableControllerList = new ArrayList<>();
      List<Renderer> renderers = new ArrayList<>();
      EndPosition endPosition = EndPositions.of(400 + padding, 400 + padding);
      List<GridElement> gridElements = getAllGridElements(grid, endPosition);
      EvasionStateMachineConfig config = buildEvasionStateMachineConfig(60, 50);
      Position startPos = Positions.of(165 + padding, 155 + padding);
      startPos.rotate(-45);

      TrippleDetectorCluster detectorCluster = buildDefaultDetectorCluster(config);
      MoveableController controller = buildMoveableController(grid, startPos, singletonList(endPosition),
            getPostMoveFowardHandler(mainWindow, moveableControllerList, emptyList(), renderers), config, detectorCluster);
      moveableControllerList.add(controller);
      renderers.addAll(getRenderers(grid, 4, 4, gridElements, controller.getMoveable(), detectorCluster, config));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow);
      List<Position> positions = prepareAndMoveMoveable(controller, mainWindow);
      preparePositionListPainter(renderers, positions);
   }

   private TrippleDetectorCluster buildDefaultDetectorCluster(EvasionStateMachineConfig centerDetectorConfig) {

      EvasionStateMachineConfig sideDetectorConfig = buildEvasionStateMachineConfig(10, 10);
      return TrippleDetectorClusterBuilder.buildDefaultDetectorCluster(centerDetectorConfig, sideDetectorConfig);
   }

   private static void preparePositionListPainter(List<Renderer> renderers, List<Position> positions) {
      renderers.stream()
            .filter(PositionListPainter.class::isInstance)
            .map(PositionListPainter.class::cast)
            .forEach(renderer -> renderer.setPositions(positions));
   }

   private static MoveableController buildMoveableController(Grid grid, Position startPos,
         List<EndPosition> endPosList, PostMoveForwardHandler postMoveFowardHandler, EvasionStateMachineConfig config, Detector detectorCluster) {
      int circleRadius = 4;
      return MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategie.FORWARD)
            .withEndPositions(endPosList)
            .withPostMoveForwardHandler(postMoveFowardHandler)
            .withEndPointMoveable()
            .withGrid(grid)
            .withStartPosition(startPos)
            .withHandler(new EvasionStateMachine(detectorCluster, config))
            .withShape(buildCircle(circleRadius, startPos))
            .withMovingIncrement(1)
            .buildAndReturnParentBuilder()
            .build();//
   }

   private static DefaultGrid buildGrid(CollisionDetectionHandler collisionDetectionHandler) {
      return MirrorGridBuilder.builder()
            .withMaxX(510)
            .withMaxY(510)
            .withMinX(padding)
            .withMinY(padding)
            .withCollisionDetectionHandler(collisionDetectionHandler)
            .build();
   }

   private List<Position> prepareAndMoveMoveable(MoveableController moveableController, MainWindow mainWindow) throws InterruptedException {
      moveableController.leadMoveable();
      return moveableController.getMoveable().getPositionHistory();
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow) {
      SwingUtilities.invokeLater(() -> mainWindow.show());
      new Thread(() -> {
         while (true) {
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            try {
               Thread.sleep(2);
            } catch (InterruptedException e) {
            }
         }
      }).start();
   }

   private static List<GridElement> getAllGridElements(DefaultGrid grid, Position endPos) {

      List<GridElement> allGridElement = new ArrayList<>();
      allGridElement.addAll(buildWall1(grid));
      allGridElement.addAll(buildWall2(grid));
      allGridElement.addAll(buildGridElements(grid));

      return allGridElement;
   }

   private static List<GridElement> buildGridElements(DefaultGrid grid) {
      Position startPos = Positions.of(215 + padding, 205 + padding);
      Obstacle simpleGridElem = new ObstacleImpl(grid, startPos, buildCircle(4, startPos));
      return Arrays.asList(simpleGridElem);
   }

   private static List<GridElement> buildWall2(DefaultGrid grid) {
      int height = 10;
      int width = 100;

      Position center1 = Positions.of(280 + padding, 280 + padding);
      center1.rotate(-45);
      GridElement rectangleGridElem = buildRectangleObstacle(grid, height, width, center1);

      Position center2 = Positions.of(288 + padding - (width / 2), 180 + padding - (height / 2));
      center2.rotate(-135);
      GridElement rectangleGridElem2 = buildRectangleObstacle(grid, height, width * 2, center2);
      return Arrays.asList(rectangleGridElem, rectangleGridElem2);
   }

   private static List<GridElement> buildWall1(DefaultGrid grid) {
      int height = 10;
      int width = 100;
      Position center1 = Positions.of(295 + padding, 370 + padding);
      center1.rotate(-45);
      GridElement rectangleGridElem1 = buildRectangleObstacle(grid, height, 70, center1);

      Position center2 = Positions.of(267 + padding - (width / 2), 342 + padding - (height / 2));
      center2.rotate(-135);
      GridElement rectangleGridElem2 = buildRectangleObstacle(grid, height, 80 * 2, center2);

      Position center3 = Positions.of(200 + padding - (width / 2), 197 + padding - (height / 2));
      center3.rotate(-135);
      GridElement rectangleGridElem3 = buildRectangleObstacle(grid, height, 70 * 2, center3);

      Position center4 = Positions.of(180 + padding, 260 + padding);
      center4.rotate(-45);
      GridElement rectangleGridElem4 = buildRectangleObstacle(grid, height, 65, center4);
      return Arrays.asList(rectangleGridElem1, rectangleGridElem2, rectangleGridElem3, rectangleGridElem4);
   }

   private static GridElement buildRectangleObstacle(DefaultGrid grid, int height, int width, Position center) {
      Rectangle rectangle = new RectangleBuilder()
            .withCenter(center)
            .withHeight(height)
            .withWidth(width)
            .withOrientation(Orientation.HORIZONTAL)
            .build();
      GridElement rectangleGridElem = new ObstacleImpl(grid, center, rectangle);
      return rectangleGridElem;
   }

   private static List<Renderer> getRenderers(DefaultGrid grid, int height, int width,
         List<GridElement> gridElements, Moveable moveable, TrippleDetectorCluster detectorCluster, EvasionStateMachineConfig config) {
      List<Renderer> renderers = gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))
            .collect(Collectors.toList());
      renderers.add(new PositionListPainter(Collections.emptyList(), getPositionListColor(), height, width));
      MoveablePainterConfig moveablePainterConfig = MoveablePainterConfig.of(detectorCluster, config, true, false);
      renderers.add(new MoveablePainter(moveable, getColor(moveable), height, width, moveablePainterConfig));
      return renderers;
   }

   private static Shape buildCircle(int width, Position pos) {
      return new CircleBuilder(width)
            .withAmountOfPoints(30)
            .withCenter(pos)
            .build();//
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
}

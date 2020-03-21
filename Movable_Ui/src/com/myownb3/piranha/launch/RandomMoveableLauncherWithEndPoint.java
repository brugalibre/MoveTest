/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.grid.gridelement.shape.CircleImpl.CircleBuilder;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.moveables.MovingStrategie;
import com.myownb3.piranha.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.grid.gridelement.EndPositionGridElement;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.AbstractGridElementPainter;
import com.myownb3.piranha.ui.render.impl.EndPositionGridElementPainter;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class RandomMoveableLauncherWithEndPoint implements Stoppable {
   private static int padding = 30;
   private boolean isRunning = true;

   public static void main(String[] args) throws InterruptedException {

      int height = 6;
      int width = 6;
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
      List<Position> endPosList = getEndPosList(height, width, grid);
      List<GridElement> gridElements = getAllGridElements(grid, endPosList, height, width);
      List<Renderer> renderers = new ArrayList<>();
      List<MoveableController> moveableControllerList = new ArrayList<>();

      MoveableController moveableController = buildMoveableController(grid, startPos, endPosList,
            getPostMoveFowardHandler(mainWindow, moveableControllerList, gridElements, renderers), width);
      moveableControllerList.add(moveableController);
      collisionDetectionHandler.setMoveableController(moveableController);
      renderers.addAll(getRenderers(height, width, grid, gridElements, moveableController.getMoveable()));

      mainWindow.addSpielfeld(renderers, grid);
      SwingUtilities.invokeLater(() -> mainWindow.show());

      prepareAndMoveMoveables(moveableController, mainWindow, gridElements);
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

   private static MoveableController buildMoveableController(MirrorGrid grid, Position startPos,
         List<Position> endPosList, PostMoveForwardHandler postMoveFowardHandler, int width) {
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(1, 0.06, 0.7d, 60, 60, 70, 50, 15);
      Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
            config.getEvasionAngle(), config.getEvasionAngleInc());
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
      return new CircleBuilder(width)
            .withAmountOfPoints(5)
            .withCenter(pos)
            .build();//
   }

   private static List<Position> getEndPosList(int height, int width, MirrorGrid grid) {
      int amountOfEndPos = 3;
      List<Position> endPosList = new ArrayList<>(amountOfEndPos);
      for (int i = 0; i < amountOfEndPos; i++) {
         endPosList.add(Positions.getRandomPosition(grid.getDimension(), height, width));
      }
      return endPosList;
   }

   private static PostMoveForwardHandler getPostMoveFowardHandler(MainWindow mainWindow,
         List<MoveableController> moveableControllerList, List<GridElement> gridElements, List<Renderer> renderers) {
      return moveableRes -> {
         moveGridElementsForward(gridElements);

         setCurrentTargetPosition(moveableControllerList, renderers);

         SwingUtilities.invokeLater(() -> mainWindow.refresh());
         try {
            Thread.sleep(1);
         } catch (InterruptedException e) {
            // ignore
         }
      };
   }

   private static void setCurrentTargetPosition(List<MoveableController> moveableControllerList, List<Renderer> renderers) {
      MoveableController moveableController = moveableControllerList.get(0);
      renderers.stream()
            .filter(EndPositionGridElementPainter.class::isInstance)
            .map(EndPositionGridElementPainter.class::cast)
            .forEach(painter -> painter.setIsCurrentTargetPosition(moveableController.getCurrentEndPos()));
   }

   private static List<GridElement> getAllGridElements(DefaultGrid grid, List<Position> endPosList, int height,
         int width) {
      List<GridElement> allGridElement = endPosList.stream()
            .map(endPos -> new EndPositionGridElement(grid, endPos, buildCircle(width, endPos)))
            .collect(Collectors.toList());

      //	int amount = 0;
      int amount = 80;
      for (int i = 0; i < amount; i++) {
         Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
         Obstacle obstacle = new MoveableObstacleImpl(grid, randomPosition, buildCircle(width, randomPosition));

         allGridElement.add(obstacle);
      }

      return allGridElement;
   }

   private void prepareAndMoveMoveables(MoveableController moveableController, MainWindow mainWindow,
         List<GridElement> allGridElements) throws InterruptedException {
      turnGridElements(allGridElements);
      while (isRunning) {
         moveableController.leadMoveable();
         Thread.sleep(5);
      }
   }

   private static void turnGridElements(List<GridElement> allGridElements) {
      allGridElements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast)
            .forEach(obstacle -> obstacle.makeTurn(MathUtil.getRandom(360)));
   }

   private static void moveGridElementsForward(List<GridElement> allGridElements) {
      allGridElements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast)
            .forEach(obstacle -> obstacle.moveForward());
   }

   private static List<Renderer> getRenderers(int height, int width, MirrorGrid grid, List<GridElement> gridElements,
         Moveable moveable) {
      List<Renderer> renderers = getRenderers(gridElements);
      renderers.add(new GridElementPainter(moveable, getColor(moveable), 1, 1));
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

   private static Color getColor(GridElement gridElement) {
      return gridElement instanceof Obstacle ? Color.BLACK
            : gridElement instanceof Moveable ? Color.RED : Color.GREEN.darker();
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

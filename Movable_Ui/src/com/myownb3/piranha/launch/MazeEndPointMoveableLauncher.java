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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
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
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.evasionstatemachine.config.DefaultConfig;
import com.myownb3.piranha.ui.grid.gridelement.EndPositionGridElement;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainterConfig;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class MazeEndPointMoveableLauncher {
   private static int padding = 30;

   public static void main(String[] args) throws InterruptedException {

      int height = 5;
      int width = 5;
      MazeEndPointMoveableLauncher launcher = new MazeEndPointMoveableLauncher();
      launcher.launch(height, width);
   }

   private void launch(int height, int width) throws InterruptedException {
      CollisionDetectionHandler collisionDetectionHandler = (a, b, c) -> {
      };
      DefaultGrid grid = buildGrid(collisionDetectionHandler);
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding,
            height);

      List<MoveableController> moveableControllerList = new ArrayList<>();
      List<Renderer> renderers = new ArrayList<>();
      EndPosition endPosition = EndPositions.of(400 + padding, 400 + padding);
      List<GridElement> gridElements = getAllGridElements(grid, height, width, endPosition);
      EvasionStateMachineConfig config = DefaultConfig.INSTANCE.getDefaultEvasionStateMachineConfig();
      MoveableController controller = buildMoveableController(grid, Positions.of(200 + padding, 200 + padding), singletonList(endPosition),
            getPostMoveFowardHandler(mainWindow, moveableControllerList, emptyList(), renderers), width, config);
      moveableControllerList.add(controller);
      renderers.addAll(getRenderers(grid, height, width, gridElements, controller.getMoveable(), config));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow);
      List<Position> positions = prepareAndMoveMoveable(controller, mainWindow);
      preparePositionListPainter(renderers, positions);
   }

   private static void preparePositionListPainter(List<Renderer> renderers, List<Position> positions) {
      renderers.stream()
            .filter(PositionListPainter.class::isInstance)
            .map(PositionListPainter.class::cast)
            .forEach(renderer -> renderer.setPositions(positions));
   }

   private static MoveableController buildMoveableController(Grid grid, Position startPos,
         List<EndPosition> endPosList, PostMoveForwardHandler postMoveFowardHandler, int width, EvasionStateMachineConfig config) {
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

   private static List<GridElement> getAllGridElements(DefaultGrid grid, int height, int width, Position endPos) {

      List<GridElement> allGridElement = new ArrayList<>();
      int amount = 30;
      int angle = 45;
      for (int i = 0; i < amount; i++) {
         int signum = calcSignum();
         Position randomPosition = Positions.of(300 + padding, 300 + padding);
         MoveableObstacleImpl obstacle = new MoveableObstacleImpl(grid, randomPosition, buildCircle(width, randomPosition));
         if (signum < 0) {
            obstacle.makeTurn(angle);
         } else {
            obstacle.makeTurn(angle - 180);
         }
         int factor = Math.max(1, (int) MathUtil.getRandom(10));
         int randomNo = Math.max(1, (int) MathUtil.getRandom(50));
         obstacle.moveForward(randomNo * factor);

         allGridElement.add(obstacle);
      }
      allGridElement.add(new EndPositionGridElement(grid, endPos, buildCircle(width, endPos)));
      return allGridElement;
   }

   private static int calcSignum() {
      double random = Math.random();
      int signum = -1;
      if (random > 0.5) {
         signum = 1;
      }
      return signum;
   }

   private static List<Renderer> getRenderers(DefaultGrid grid, int height, int width,
         List<GridElement> gridElements, Moveable moveable, EvasionStateMachineConfig config) {
      List<Renderer> renderers = gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))
            .collect(Collectors.toList());
      renderers.add(new PositionListPainter(Collections.emptyList(), getPositionListColor(), height, width));
      MoveablePainterConfig moveablePainterConfig = MoveablePainterConfig.of(config, false, false);
      renderers.add(new MoveablePainter(moveable, getColor(moveable), height, width, moveablePainterConfig));
      return renderers;
   }

   private static Shape buildCircle(int width, Position pos) {
      return new CircleBuilder(width)
            .withAmountOfPoints(30)
            .withCenter(pos)
            .build();//
   }
}

/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
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
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder.EndPointMoveableBuilder;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class MazeEndPointMoveableLauncher {
   private static int padding = 30;

   public static void main(String[] args) throws InterruptedException {

      int height = 50;
      int width = 50;
      MazeEndPointMoveableLauncher launcher = new MazeEndPointMoveableLauncher();
      launcher.launch(height, width);
   }

   private void launch(int height, int width) throws InterruptedException {
      CollisionDetectionHandler collisionDetectionHandler = (a, b, c) -> {
      };
      DefaultGrid grid = buildGrid(collisionDetectionHandler);
      EndPosition endPosition = EndPositions.of(400 + padding, 400 + padding);
      List<GridElement> gridElements = getAllGridElements(grid, height, width, endPosition);
      EndPointMoveable moveable = getMoveable(endPosition, grid, 200, 200, width);
      List<Renderer> renderers = getRenderers(grid, height, width, gridElements, moveable);

      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding,
            height);
      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow);
      List<Position> positions = prepareAndMoveMoveables(moveable, mainWindow);
      preparePositionListPainter(renderers, positions);
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
               Thread.sleep(2);
            } catch (InterruptedException e) {
            }
         }
      }).start();
   }

   private static List<GridElement> getAllGridElements(DefaultGrid grid, int height, int width, Position endPos) {

      List<GridElement> allGridElement = new ArrayList<>();
      int amount = 0;
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
      return allGridElement;
   }

   private static List<Position> prepareAndMoveMoveables(EndPointMoveable endPointMoveable, MainWindow mainWindow) throws InterruptedException {
      //      while (false) {
      //         MoveResult moveResult = endPointMoveable.moveForward2EndPos();
      //         if (moveResult.isDone()) {
      //            break;
      //         }
      //         Thread.sleep(2);
      //      }
      return Collections.emptyList();//endPointMoveable.getPositionHistory();
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
         List<GridElement> gridElements, EndPointMoveable endPointMoveable) {
      List<Renderer> renderers = gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))
            .collect(Collectors.toList());
      renderers.add(new PositionListPainter(Collections.emptyList(), Color.GREEN, height, width));
      renderers.add(new GridElementPainter(endPointMoveable, getColor(endPointMoveable), height, width));
      return renderers;
   }

   private static Color getColor(GridElement gridElement) {
      return gridElement instanceof Obstacle ? Color.BLACK : gridElement instanceof Moveable ? Color.RED : Color.BLUE;
   }

   private static EndPointMoveable getMoveable(EndPosition endPos, Grid grid, int posX, int posY, int width) {
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(1, 10, 0.06, 0.7d, 60, 60, 70, 50, 15);
      Position pos = Positions.of(posX + padding, posY + padding);
      Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
            config.getEvasionAngle(), config.getEvasionAngleInc());
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(pos)
            .withHandler(new EvasionStateMachine(detector, endPos, config))
            .withShape(buildCircle(width, pos))
            .build();
      return moveable;
   }

   private static Shape buildCircle(int width, Position pos) {
      return new CircleBuilder(width)
            .withAmountOfPoints(30)
            .withCenter(pos)
            .build();//
   }
}

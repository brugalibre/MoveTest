/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Dimension;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableBuilder;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class RandomMoveableLauncher implements Stoppable {

   private boolean has2Run = true;
   private static int padding = 30;

   public static void main(String[] args) throws InterruptedException {

      int height = 5;
      int width = 5;
      RandomMoveableLauncher randomMoveableLauncher = new RandomMoveableLauncher();
      randomMoveableLauncher.launch(height, width);
   }

   private void launch(int height, int width) throws InterruptedException {
      MainWindow mainWindow = new MainWindow(700, 700, padding, height);
      CollisionDetectionHandler collisionDetector = new CollisionDetectionHandlerImpl(this, mainWindow);

      MirrorGrid grid = buildGrid(collisionDetector);
      Moveable moveable = getMoveable(grid, height, width);
      GridElementPainter moveablePainter = new GridElementPainter(moveable, getColor(moveable), 1, 1);
      List<GridElement> gridElements = getAllGridElements(grid, height, width);
      List<Renderer> renderers = getRenderers(gridElements);
      renderers.add(moveablePainter);

      mainWindow.addSpielfeld(renderers, grid);
      SwingUtilities.invokeLater(() -> mainWindow.show());

      prepareAndMoveMoveables(moveable, gridElements, mainWindow);
   }

   private static MirrorGrid buildGrid(CollisionDetectionHandler collisionDetector) {
      return MirrorGridBuilder.builder()
            .withMaxX(700)
            .withMaxY(700)
            .withMinX(padding)
            .withMinY(padding)
            .withCollisionDetectionHandler(collisionDetector)
            .build();
   }

   private static List<GridElement> getAllGridElements(DefaultGrid grid, int height, int width) {

      List<GridElement> gridelements = new ArrayList<>();

      int amount = 80;
      for (int i = 0; i < amount; i++) {
         Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
         Obstacle obstacle = new MoveableObstacleImpl(grid, randomPosition, buildCircle(height, randomPosition));

         gridelements.add(obstacle);
      }

      return gridelements;
   }

   private void prepareAndMoveMoveables(Moveable moveable, List<GridElement> gridelements, MainWindow mainWindow) throws InterruptedException {

      moveable.makeTurn(MathUtil.getRandom(360));
      turnGridElements(gridelements);

      int counter = 0;
      while (has2Run) {
         moveable.moveForward();

         if (counter % 5 == 0) {
            moveGridElementsForward(gridelements);
         }
         counter++;
         mainWindow.refresh();
         Thread.sleep(1);
      }
   }

   private static void turnGridElements(List<GridElement> gridelements) {
      gridelements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast)
            .forEach(obstacle -> obstacle.makeTurn(MathUtil.getRandom(360)));
   }

   private static void moveGridElementsForward(List<GridElement> gridelements) {
      gridelements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast)
            .forEach(obstacle -> obstacle.moveForward());
   }

   private static <T extends GridElement> List<Renderer> getRenderers(List<GridElement> gridElements) {
      return gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), 1, 1))
            .collect(Collectors.toList());
   }

   private static Moveable getMoveable(Grid grid, int height, int width) {

      Dimension dimension = grid.getDimension();
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 40, 80, 70, 5);
      Position pos = Positions.getRandomPosition(dimension, height, width);
      Detector detector =
            new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(), config.getEvasionAngle(), config.getEvasionAngleInc() + height);
      Shape circleShape = buildCircle(width, pos);
      return MoveableBuilder.builder(grid, pos)
            .withHandler(new EvasionStateMachine(detector, config))
            .withShape(circleShape)
            .build();
   }

   private static Shape buildCircle(int width, Position pos) {
      return new CircleBuilder(width)
            .withAmountOfPoints(10)
            .withCenter(pos)
            .build();//
   }

   @Override
   public boolean isRunning() {
      return has2Run;
   }

   @Override
   public void stop() {
      has2Run = false;
   }
}

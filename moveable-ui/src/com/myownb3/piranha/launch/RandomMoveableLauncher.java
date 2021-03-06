/**
 * 
 */
package com.myownb3.piranha.launch;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
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
      MainWindow mainWindow = new MainWindow(400, 400, padding, height);
      CollisionDetectionHandler collisionDetector = new CollisionDetectionHandlerImpl(this, mainWindow);

      MirrorGrid grid = buildGrid(collisionDetector);
      Moveable moveable = getMoveable(grid, height, width, 30);
      GridElementPainter moveablePainter = new GridElementPainter(moveable, getColor(moveable));
      List<GridElement> gridElements = getAllGridElements(grid, height, width);
      List<Renderer<?>> renderers = getRenderers(gridElements);
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

      int amount = 25;
      for (int i = 0; i < amount; i++) {
         Position randomPosition = grid.getRandomPosition(width);
         Obstacle obstacle = MoveableObstacleBuilder.builder()
               .withGrid(grid)
               .withHealth(Double.MAX_VALUE)
               .withShape(CircleBuilder.builder()
                     .withRadius(width)
                     .withAmountOfPoints(20)
                     .withCenter(randomPosition)
                     .build())
               .withVelocity(80)
               .build();

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

   private static List<Renderer<?>> getRenderers(List<GridElement> gridElements) {
      return gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement)))
            .collect(Collectors.toList());
   }

   private static Moveable getMoveable(Grid grid, int height, int width, int velocity) {
      EvasionStateMachineConfig config = buildEvasionStateMachineConfig();
      Position pos = grid.getRandomPosition(width);
      return MoveableBuilder.builder()
            .withGrid(grid)
            .withVelocity(velocity)
            .withHandler(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .withDetectorReach(config.getDetectorReach())
                        .withEvasionDistance(config.getEvasionDistance())
                        .withDetectorAngle(config.getDetectorAngle())
                        .withEvasionAngle(config.getEvasionAngle())
                        .withAngleInc(config.getEvasionAngleInc() + height)
                        .build())
                  .withEvasionStateMachineConfig(config)
                  .build())
            .withShape(CircleBuilder.builder()
                  .withRadius(width)
                  .withAmountOfPoints(15)
                  .withCenter(pos)
                  .build())
            .build();
   }

   private static EvasionStateMachineConfig buildEvasionStateMachineConfig() {
      int detectorReach = 40;
      return EvasionStateMachineConfigBuilder.builder()
            .withReturningAngleIncMultiplier(1)
            .withOrientationAngle(10)
            .withReturningMinDistance(0.05)
            .withReturningAngleMargin(0.7d)
            .withPassingDistance(80)
            .withPassingDistance(2 * detectorReach / 3)
            .withPostEvasionReturnAngle(4)
            .withDetectorConfig(DetectorConfigBuilder.builder()
                  .withDetectorReach(detectorReach)
                  .withEvasionDistance(2 * detectorReach / 3)
                  .withDetectorAngle(80)
                  .withEvasionAngle(70)
                  .withEvasionAngleInc(5)
                  .build())
            .build();
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

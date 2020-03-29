/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.MoveResult;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder.EndPointMoveableBuilder;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;

/**
 * @author Dominic
 *
 */
public class EndPointMoveableLauncher {
   private static int padding = 30;

   public static void main(String[] args) throws InterruptedException {

      int height = 4;
      int width = 4;
      EndPointMoveableLauncher launcher = new EndPointMoveableLauncher();
      launcher.launch(height, width);
   }

   private void launch(int height, int width) throws InterruptedException {
      DefaultGrid grid = buildGrid();
      List<EndPosition> endPositions = getEndPositions();
      List<GridElement> gridElements = getAllGridElements(grid, height, width, endPositions);
      List<EndPointMoveable> moveables = getMoveables(grid, endPositions, width);
      List<Renderer> renderers = getRenderers(grid, height, width, gridElements, moveables);

      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding,
            height);
      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow);
      List<Position> positions = prepareAndMoveMoveables(moveables, mainWindow);
      preparePositionListPainter(renderers, positions);
   }

   private static DefaultGrid buildGrid() {
      return MirrorGridBuilder.builder()
            .withMaxX(510)
            .withMaxY(510)
            .withMinX(padding)
            .withMinY(padding)
            .build();
   }

   private static List<EndPointMoveable> getMoveables(DefaultGrid grid, List<EndPosition> endPositions, int width) {
      return endPositions.stream()
            .map(endPos -> getMoveable(endPos, grid, 200, 200, width))
            .collect(Collectors.toList());
   }

   private static List<EndPosition> getEndPositions() {
      List<EndPosition> endPositions = new ArrayList<>();
      //      endPositions.add(EndPositions.of(400 + padding, 10 + padding));
      endPositions.add(EndPositions.of(400 + padding, 400 + padding));
      //      endPositions.add(EndPositions.of(10 + padding, 10 + padding));
      //      endPositions.add(EndPositions.of(10 + padding, 400 + padding));
      return endPositions;
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

   private static List<GridElement> getAllGridElements(DefaultGrid grid, int height, int width, List<EndPosition> endPositions) {

      List<GridElement> allGridElement = new ArrayList<>();

      int angle = 45;
      int signum = calcSignum();
      Position randomPosition = Positions.of(300 + padding, 110 + padding);
      Position randomPosition2 = Positions.of(300 + padding, 300 + padding);
      Position randomPosition3 = Positions.of(110 + padding, 110 + padding);
      Position randomPosition4 = Positions.of(110 + padding, 300 + padding);
      MoveableObstacleImpl obstacle = new MoveableObstacleImpl(grid, randomPosition, buildCircle(width, randomPosition));
      MoveableObstacleImpl obstacle2 = new MoveableObstacleImpl(grid, randomPosition2, buildCircle(width, randomPosition2));
      MoveableObstacleImpl obstacle3 = new MoveableObstacleImpl(grid, randomPosition3, buildCircle(width, randomPosition3));
      MoveableObstacleImpl obstacle4 = new MoveableObstacleImpl(grid, randomPosition4, buildCircle(width, randomPosition4));
      if (signum < 0) {
         obstacle.makeTurn(angle);
         obstacle2.makeTurn(angle);
         obstacle3.makeTurn(angle);
         obstacle4.makeTurn(angle);
      } else {
         obstacle.makeTurn(angle - 180);
         obstacle2.makeTurn(angle - 180);
         obstacle3.makeTurn(angle - 180);
         obstacle4.makeTurn(angle - 180);
      }

      allGridElement.add(obstacle);
      allGridElement.add(obstacle2);
      allGridElement.add(obstacle3);
      allGridElement.add(obstacle4);

      for (EndPosition endPos : endPositions) {
         allGridElement.add(new SimpleGridElement(grid, endPos, buildCircle(width, endPos)));
      }
      return allGridElement;
   }

   private static List<Position> prepareAndMoveMoveables(List<EndPointMoveable> endPointMoveables,
         MainWindow mainWindow) throws InterruptedException {
      Map<Moveable, Boolean> isMoveableDoneMap = new HashMap<>();
      for (EndPointMoveable endPointMoveable : endPointMoveables) {
         isMoveableDoneMap.put(endPointMoveable, false);
      }
      while (true) {
         List<EndPointMoveable> filterDoneMoveables = filterDoneMoveables(endPointMoveables, isMoveableDoneMap);
         for (EndPointMoveable endPointMoveable : filterDoneMoveables) {
            MoveResult moveResult = endPointMoveable.moveForward2EndPos();
            isMoveableDoneMap.put(endPointMoveable, moveResult.isDone());
            Thread.sleep(2);
         }
         if (filterDoneMoveables.isEmpty()) {
            break;
         }
      }
      return endPointMoveables.stream()
            .map(Moveable::getPositionHistory)
            .flatMap(List::stream)
            .collect(Collectors.toList());
   }

   private static List<EndPointMoveable> filterDoneMoveables(List<EndPointMoveable> endPointMoveables,
         Map<Moveable, Boolean> isMoveableDoneMap) {
      List<EndPointMoveable> filteredPointMoveables = new ArrayList<EndPointMoveable>();
      for (EndPointMoveable endPointMoveable : endPointMoveables) {
         if (!isMoveableDoneMap.get(endPointMoveable)) {
            filteredPointMoveables.add(endPointMoveable);
         }
      }
      return filteredPointMoveables;
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
         List<GridElement> gridElements, List<EndPointMoveable> moveables) {
      List<Renderer> renderers = gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))
            .collect(Collectors.toList());
      renderers.add(new PositionListPainter(Collections.emptyList(), Color.GREEN, height, width));

      for (EndPointMoveable endPointMoveable : moveables) {
         renderers.add(new GridElementPainter(endPointMoveable, getColor(endPointMoveable), height, width));
      }
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
      moveable.setEndPosition(endPos);
      return moveable;
   }

   private static Shape buildCircle(int width, Position pos) {
      return new CircleBuilder(width)
            .withAmountOfPoints(5)
            .withCenter(pos)
            .build();//
   }
}

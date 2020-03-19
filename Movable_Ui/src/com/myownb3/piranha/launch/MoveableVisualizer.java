/**
 * 
 */
package com.myownb3.piranha.launch;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.grid.SwappingGrid;
import com.myownb3.piranha.grid.SwappingGrid.SwappingGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.ui.application.MainWindow;

/**
 * @author Dominic
 *
 */
public class MoveableVisualizer {

   public static void visualizePositionsWithJFreeChart(List<Position> posList, Obstacle obstacle)
         throws InterruptedException {
      visualizePositionsWithJFreeChart(posList, Collections.singletonList(obstacle));
   }

   public static void visualizePositionsWithJFreeChart(List<Position> posList, List<Obstacle> obstacles)
         throws InterruptedException {
      SwappingGrid grid = SwappingGridBuilder.builder()
            .withMaxX(500)
            .withMaxY(500)
            .withMinX(-5)
            .withMinY(-5)
            .build();

      List<GridElement> gridElements = posList.stream()
            .map(pos -> new ObstacleImpl(grid, pos))
            .collect(Collectors.toList());//

      MainWindow mainWindow = new MainWindow(obstacles, gridElements, grid.getDimension().getWidth(),
            grid.getDimension().getHeight());
      mainWindow.show();
      Thread.sleep(Integer.MAX_VALUE);
   }
}

package com.myownb3.piranha.launch;

import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.EndPositionGridElementPainter;

public class DefaultPostMoveForwardHandler {

   public static PostMoveForwardHandler getPostMoveFowardHandler(MainWindow mainWindow,
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


   private static void moveGridElementsForward(List<GridElement> allGridElements) {
      allGridElements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast)
            .forEach(obstacle -> obstacle.moveForward());
   }

   private static void setCurrentTargetPosition(List<MoveableController> moveableControllerList, List<Renderer> renderers) {
      MoveableController moveableController = moveableControllerList.get(0);
      renderers.stream()
            .filter(EndPositionGridElementPainter.class::isInstance)
            .map(EndPositionGridElementPainter.class::cast)
            .forEach(painter -> painter.setIsCurrentTargetPosition(moveableController.getCurrentEndPos()));
   }
}

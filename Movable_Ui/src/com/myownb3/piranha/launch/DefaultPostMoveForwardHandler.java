package com.myownb3.piranha.launch;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.launch.weapon.ProjectilePaintUtil;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.EndPositionGridElementPainter;

public class DefaultPostMoveForwardHandler implements PostMoveForwardHandler {

   private MainWindowHolder windowHolder;
   private MoveableControllerHolder moveableControllerHolder;
   private List<Renderer> renderers;
   private List<GridElement> gridElements;
   private Set<String> existingProjectiles = new HashSet<>();

   public DefaultPostMoveForwardHandler(MainWindowHolder windowHolder, MoveableControllerHolder moveableControllerHolder,
         List<GridElement> gridElements, List<Renderer> renderers) {
      this.windowHolder = windowHolder;
      this.moveableControllerHolder = moveableControllerHolder;
      this.windowHolder = windowHolder;
      this.renderers = renderers;
      this.gridElements = gridElements;
   }

   public static PostMoveForwardHandler getPostMoveFowardHandler(MainWindowHolder windowHolder,
         MoveableControllerHolder moveableControllerHolder, List<GridElement> gridElements, List<Renderer> renderers) {
      return new DefaultPostMoveForwardHandler(windowHolder, moveableControllerHolder, gridElements, renderers);
   }

   @Override
   public void handlePostMoveForward(MoveResult moveResult) {
      List<Moveable> moveables = getAllMoveables();
      setCurrentTargetPosition(moveableControllerHolder.moveableController, renderers);
      moveGridElementsForward(moveables);
      //      WorkerThreadFactory.INSTANCE.executeAsync(() -> moveGridElementsForward(moveables));
      checkTurret(gridElements);
      Grid grid = moveables.get(0).getGrid();
      ProjectilePaintUtil.addNewProjectilePainters(grid, renderers, existingProjectiles, moveables);
      ProjectilePaintUtil.removeDestroyedPainters(renderers);

      SwingUtilities.invokeLater(() -> windowHolder.mainWindow.refresh());
      try {
         Thread.sleep(1);
      } catch (InterruptedException e) {
         // ignore
      }
   }

   private List<Moveable> getAllMoveables() {
      return gridElements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast).collect(Collectors.toList());
   }

   private void checkTurret(List<GridElement> allGridElements) {
      allGridElements.stream()
            .filter(Turret.class::isInstance)
            .map(Turret.class::cast)
            .forEach(Turret::autodetect);
   }

   private static void moveGridElementsForward(List<Moveable> moveables) {
      synchronized (moveables) {
         moveables.forEach(obstacle -> obstacle.moveForward());
      }
   }

   private static void setCurrentTargetPosition(MoveableController moveableController, List<Renderer> renderers) {
      renderers.stream()
            .filter(EndPositionGridElementPainter.class::isInstance)
            .map(EndPositionGridElementPainter.class::cast)
            .forEach(painter -> painter.setIsCurrentTargetPosition(moveableController.getCurrentEndPos()));
   }

   protected MoveableController getMoveableController() {
      return moveableControllerHolder.moveableController;
   }

   public static class MoveableControllerHolder {
      private MoveableController moveableController;

      public void setMoveableController(MoveableController moveableController) {
         this.moveableController = moveableController;
      }
   }

   public static class MainWindowHolder {

      private MainWindow mainWindow;

      public MainWindowHolder(MainWindow mainWindow) {
         this.mainWindow = mainWindow;
      }

      public MainWindowHolder() {
         // empty, window is set late
      }

      public void setMainWindow(MainWindow mainWindow) {
         this.mainWindow = mainWindow;
      }
   }
}

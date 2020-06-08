package com.myownb3.piranha.launch;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.launch.weapon.ProjectilePaintUtil;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.EndPositionGridElementPainter;

public class DefaultPostMoveForwardHandler implements PostMoveForwardHandler {

   private PostMoveForwardHandlerCtx ctx;

   public DefaultPostMoveForwardHandler(PostMoveForwardHandlerCtx ctx) {
      this.ctx = ctx;
   }

   @Override
   public void handlePostMoveForward(MoveResult moveResult) {
      List<Moveable> moveables = getAllMoveables();
      setCurrentTargetPosition(ctx.getMoveableController(), ctx.getRenderers());
      moveGridElementsForward(moveables);
      callAutoDetect(ctx.getGridElements());
      //      WorkerThreadFactory.INSTANCE.executeAsync(() -> moveGridElementsForward(moveables));
      ProjectilePaintUtil.addNewProjectilePainters(ctx.getGrid(), ctx.getRenderers(), ctx.getExistingProjectiles(), ctx.getGridElements());
      ProjectilePaintUtil.removeDestroyedPainters(ctx.getRenderers());

      SwingUtilities.invokeLater(() -> ctx.getMainWindow().refresh());
      try {
         Thread.sleep(1);
      } catch (InterruptedException e) {
         // ignore
      }
   }

   private void callAutoDetect(List<GridElement> gridElements) {
      gridElements.stream()
            .filter(AutoDetectable.class::isInstance)
            .map(AutoDetectable.class::cast)
            .forEach(AutoDetectable::autodetect);
   }

   private List<Moveable> getAllMoveables() {
      return ctx.getGridElements().stream()
            .filter(Moveable.class::isInstance)
            .map(Moveable.class::cast)
            .collect(Collectors.toList());
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
      return ctx.getMoveableController();
   }

}

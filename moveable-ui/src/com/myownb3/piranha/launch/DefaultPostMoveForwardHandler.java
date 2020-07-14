package com.myownb3.piranha.launch;

import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.launch.weapon.ProjectilePaintUtil;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.EndPositionGridElementPainter;

public class DefaultPostMoveForwardHandler implements MoveablePostActionHandler {

   private PostMoveForwardHandlerCtx ctx;

   public DefaultPostMoveForwardHandler(PostMoveForwardHandlerCtx ctx) {
      this.ctx = ctx;
   }

   @Override
   public boolean handlePostConditions(Moveable moveable) {
      setCurrentTargetPosition(ctx.getMoveableController(), ctx.getRenderers());
      callAutoDetect(ctx.getGrid().getAllGridElements(moveable));
      //            WorkerThreadFactory.INSTANCE.executeAsync(() -> moveGridElementsForward(moveables));
      ProjectilePaintUtil.addNewAutoDetectablePainters(ctx.getGrid(), ctx.getRenderers(), ctx.getExistingProjectiles());
      ProjectilePaintUtil.removeDestroyedPainters(ctx.getRenderers());

      SwingUtilities.invokeLater(() -> ctx.getMainWindow().refresh());
      try {
         Thread.sleep(1);
      } catch (InterruptedException e) {
         // ignore
      }
      return true;
   }

   private void callAutoDetect(List<GridElement> gridElements) {
      gridElements.parallelStream()
            .filter(AutoDetectable.class::isInstance)
            .map(AutoDetectable.class::cast)
            .forEach(AutoDetectable::autodetect);
   }

   private static void setCurrentTargetPosition(MoveableController moveableController, List<Renderer<? extends GridElement>> renderers) {
      renderers.stream()
            .filter(EndPositionGridElementPainter.class::isInstance)
            .map(EndPositionGridElementPainter.class::cast)
            .forEach(painter -> painter.setIsCurrentTargetPosition(moveableController.getCurrentEndPos()));
   }

   protected MoveableController getMoveableController() {
      return ctx.getMoveableController();
   }

}

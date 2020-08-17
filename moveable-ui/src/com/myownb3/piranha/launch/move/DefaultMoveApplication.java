package com.myownb3.piranha.launch.move;

import com.myownb3.piranha.application.Application;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.launch.PostMoveForwardHandlerCtx;
import com.myownb3.piranha.ui.render.impl.EndPositionGridElementPainter;

public class DefaultMoveApplication implements Application {

   private Grid grid;
   private PostMoveForwardHandlerCtx ctx;
   private MoveableController moveableController;

   public DefaultMoveApplication(Grid grid, PostMoveForwardHandlerCtx ctx, MoveableController moveableController) {
      this.grid = grid;
      this.ctx = ctx;
      this.moveableController = moveableController;
   }

   @Override
   public void run() {
      grid.getAllGridElements(null).parallelStream()
            .filter(AutoDetectable.class::isInstance)
            .map(AutoDetectable.class::cast)
            .forEach(AutoDetectable::autodetect);
      ctx.getRenderers().stream()
            .filter(EndPositionGridElementPainter.class::isInstance)
            .map(EndPositionGridElementPainter.class::cast)
            .forEach(painter -> painter.setIsCurrentTargetPosition(moveableController.getCurrentEndPos()));

   }

   @Override
   public void prepare() {
      // We do nothing here
   }
}

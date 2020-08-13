package com.myownb3.piranha.launch;

import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;

public class DefaultPostMoveForwardHandler implements MoveablePostActionHandler {

   private PostMoveForwardHandlerCtx ctx;

   public DefaultPostMoveForwardHandler(PostMoveForwardHandlerCtx ctx) {
      this.ctx = ctx;
   }

   @Override
   public boolean handlePostConditions(Moveable moveable) {
      ctx.getLogicHandler().onCylce();
      try {
         Thread.sleep(1);
      } catch (InterruptedException e) {
         // ignore
      }
      return true;
   }

   public DefaultPostMoveForwardHandler() {}

   protected MoveableController getMoveableController() {
      return ctx.getMoveableController();
   }

}

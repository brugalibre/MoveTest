package com.myownb3.piranha.core.moveables.controller.forwardstrategy.impl;

import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.MoveResultImpl;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.ForwardStrategyHandler;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.MoveForwardRequest;

public class WithoutEndPosForwardStrategyHandler implements ForwardStrategyHandler {

   private MoveableController moveableController;

   public WithoutEndPosForwardStrategyHandler(MoveableController moveableController) {
      this.moveableController = moveableController;
   }

   @Override
   public void moveMoveableForward(MoveForwardRequest moveForwardRequest) {
      leadMoveableForward(moveForwardRequest.getEndPointMoveable(), moveForwardRequest.getPostMoveForwardHandler());
   }

   private void leadMoveableForward(Moveable moveable, PostMoveForwardHandler postMoveForwardHandler) {
      while (moveableController.isRunning()) {
         moveable.moveForward();
         MoveResultImpl moveResult = new MoveResultImpl(moveable.getPosition());
         postMoveForwardHandler.handlePostMoveForward(moveResult);
      }
   }
}

package com.myownb3.piranha.core.moveables.controller.forwardstrategy.impl;

import com.myownb3.piranha.core.moveables.Moveable;
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
      leadMoveableForward(moveForwardRequest.getEndPointMoveable());
   }

   private void leadMoveableForward(Moveable moveable) {
      while (moveableController.isRunning()) {
         moveable.moveForward();
      }
   }
}

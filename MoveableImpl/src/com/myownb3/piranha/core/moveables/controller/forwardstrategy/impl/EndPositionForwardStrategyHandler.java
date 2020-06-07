package com.myownb3.piranha.core.moveables.controller.forwardstrategy.impl;

import java.util.List;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.ForwardStrategyHandler;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.MoveForwardRequest;

public class EndPositionForwardStrategyHandler implements ForwardStrategyHandler {

   private MoveableController moveableController;

   public EndPositionForwardStrategyHandler(MoveableController moveableController) {
      this.moveableController = moveableController;
   }

   @Override
   public void moveMoveableForward(MoveForwardRequest moveForwardRequest) {

      leadMoveableWithEndPoints(moveForwardRequest.getEndPointMoveable(), moveForwardRequest.getEndPositions(),
            moveForwardRequest.getPostMoveForwardHandler());
   }

   private void leadMoveableWithEndPoints(EndPointMoveable endPointMoveable, List<EndPosition> endPosList,
         PostMoveForwardHandler postMoveForwardHandler) {
      for (EndPosition endPos : endPosList) {
         leadMoveable2EndPos(endPointMoveable, endPos, postMoveForwardHandler);
      }
   }

   /*
    * First turn the moveable in the right direction then move forward until we
    * reach our end position.
    */
   private void leadMoveable2EndPos(EndPointMoveable endPointMoveable, EndPosition endPos, PostMoveForwardHandler postMoveForwardHandler) {
      endPointMoveable.setEndPosition(endPos);
      while (moveableController.isRunning()) {
         MoveResult moveResult = endPointMoveable.moveForward2EndPos();
         postMoveForwardHandler.handlePostMoveForward(moveResult);
         if (moveResult.isDone()) {
            break;// We are done
         }
      }
   }
}

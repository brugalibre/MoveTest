package com.myownb3.piranha.core.moveables.controller.forwardstrategy.impl;

import java.util.Iterator;
import java.util.List;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.ForwardStrategyHandler;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.MoveForwardRequest;

public class IncrementalForwardStrategyHandler implements ForwardStrategyHandler {

   private IncrementalState state;

   public IncrementalForwardStrategyHandler() {
      state = IncrementalState.INIT;
   }

   @Override
   public void moveMoveableForward(MoveForwardRequest moveForwardRequest) {
      List<EndPosition> endPositions = moveForwardRequest.getEndPositions();
      EndPointMoveable endPointMoveable = moveForwardRequest.getEndPointMoveable();
      switch (state) {
         case INIT:
            endPointMoveable.setEndPosition(endPositions.get(0));
            state = IncrementalState.MOVING;
            // Fall through
         case MOVING:
            moveForwardIncremental(endPointMoveable, endPositions, moveForwardRequest.getPostMoveForwardHandler());
            break;
         default:
            break;
      }
   }

   private void moveForwardIncremental(EndPointMoveable moveable, List<EndPosition> endPositions, PostMoveForwardHandler postMoveForwardHandler) {
      MoveResult moveResult = moveable.moveForward2EndPos();
      postMoveForwardHandler.handlePostMoveForward(moveResult);
      if (moveResult.isDone()) {
         EndPosition nextEndPos = evalNextEndPos(endPositions, moveable.getCurrentEndPos());
         moveable.setEndPosition(nextEndPos);
      }
   }

   @Visible4Testing
   static EndPosition evalNextEndPos(List<EndPosition> endPosList, EndPosition currentEndPos) {
      for (Iterator<EndPosition> iterator = endPosList.iterator(); iterator.hasNext();) {
         EndPosition endPosition = iterator.next();
         if (endPosition.equals(currentEndPos)) {
            if (iterator.hasNext()) {
               return iterator.next();
            } else {
               return endPosList.get(0);
            }
         }
      }
      throw new IllegalArgumentException("No next EndPosition found!");
   }

   private static enum IncrementalState {
      /** The {@link EndPointMoveable} has to be initialized first with the first {@link EndPosition} */
      INIT,
      /** The {@link EndPointMoveable} is ready to be moved forward */
      MOVING,
   }

}

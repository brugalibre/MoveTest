/**
 * 
 */
package com.myownb3.piranha.core.moveables.controller;

import java.util.List;

import com.myownb3.piranha.core.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.exception.NotImplementedException;

/**
 * @author Dominic
 *
 */
public class MoveableController {

   private MovingStrategy strategie;
   private EndPointMoveable moveable;
   private List<EndPosition> endPosList;
   private PostMoveForwardHandler handler;
   private boolean isRunning;

   /**
    * @param moveable
    */
   public MoveableController(EndPointMoveable moveable, List<EndPosition> endPosList) {
      this(moveable, MovingStrategy.FORWARD, endPosList);
   }

   /**
    * @param moveable
    */
   public MoveableController(EndPointMoveable moveable, MovingStrategy strategie, List<EndPosition> endPosList) {
      isRunning = true;
      this.moveable = moveable;
      this.strategie = strategie;
      this.endPosList = endPosList;
      handler = result -> {
      };

   }

   public void leadMoveable() {
      switch (strategie) {
         case FORWARD:
            leadMoveableWithEndPoints();
            break;

         case FORWARD_WITHOUT_END_POS:
            leadMoveableForward();
            break;

         default:
            throw new NotImplementedException("Not supported Strategie '" + strategie + "'");
      }
   }

   private void leadMoveableForward() {
      while (isRunning) {
         moveable.moveForward();
         MoveResultImpl moveResult = new MoveResultImpl(moveable.getPosition());
         handler.handlePostMoveForward(moveResult);
      }
   }

   private void leadMoveableWithEndPoints() {
      for (EndPosition endPos : endPosList) {
         leadMoveable2EndPos(endPos);
      }
   }

   /*
    * First turn the moveable in the right direction then move forward until we
    * reach our end position.
    */
   private void leadMoveable2EndPos(EndPosition endPos) {
      moveable.setEndPosition(endPos);
      while (isRunning) {
         MoveResult moveResult = moveable.moveForward2EndPos();
         handler.handlePostMoveForward(moveResult);
         if (moveResult.isDone()) {
            break;// We are done
         }
      }
   }


   /**
    * Stops this {@link MoveableController}
    */
   public void stop() {
      isRunning = false;
   }

   public static final class MoveableControllerBuilder {

      private List<EndPosition> endPosList;
      private MovingStrategy movingStrategie;
      private PostMoveForwardHandler postMoveForwardHandler;
      private EndPointMoveable endPointMoveable;

      private MoveableControllerBuilder() {
         // private
      }

      public static MoveableControllerBuilder builder() {
         return new MoveableControllerBuilder();
      }

      public MoveableControllerBuilder withMoveable(EndPointMoveable endPointMoveable) {
         this.endPointMoveable = endPointMoveable;
         return this;
      }

      public EndPointMoveableBuilder withEndPointMoveable() {
         return EndPointMoveableBuilder.builder(this);
      }

      public MoveableControllerBuilder withEndPositions(List<EndPosition> endPosList) {
         this.endPosList = endPosList;
         return this;
      }

      public MoveableControllerBuilder withStrategie(MovingStrategy movingStrategie) {
         this.movingStrategie = movingStrategie;
         return this;
      }

      public MoveableControllerBuilder withPostMoveForwardHandler(PostMoveForwardHandler postMoveForwardHandler) {
         this.postMoveForwardHandler = postMoveForwardHandler;
         return this;
      }

      public MoveableController build() {
         MoveableController moveableController = new MoveableController(endPointMoveable, endPosList);
         moveableController.endPosList = endPosList;
         moveableController.strategie = movingStrategie;
         moveableController.handler = postMoveForwardHandler;
         return moveableController;
      }
   }

   public final Position getCurrentEndPos() {
      return this.moveable.getCurrentEndPos();
   }

   public Moveable getMoveable() {
      return moveable;
   }
}

/**
 * 
 */
package com.myownb3.piranha.moveables;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Objects;

import com.myownb3.piranha.exception.NotImplementedException;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.moveables.postaction.MoveablePostActionHandler;

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

         default:
            throw new NotImplementedException("Not supported Strategie '" + strategie + "'");
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
         return new EndPointMoveableBuilder(this);
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

      public static final class EndPointMoveableBuilder {

         private MoveableControllerBuilder controllerBuilder;
         private EndPointMoveable moveable;
         private MoveablePostActionHandler handler;
         private Position startPosition;
         private Grid grid;
         private int movingIncrement;
         private Shape shape;

         public static EndPointMoveableBuilder builder() {
            return new EndPointMoveableBuilder();
         }

         private EndPointMoveableBuilder() {
            movingIncrement = 1;
         }

         private EndPointMoveableBuilder(MoveableControllerBuilder moveableControllerBuilder) {
            this();
            this.controllerBuilder = moveableControllerBuilder;
         }

         public EndPointMoveableBuilder withGrid(Grid grid) {
            this.grid = grid;
            return this;
         }

         public EndPointMoveableBuilder withStartPosition(Position position) {
            this.startPosition = position;
            return this;
         }

         public EndPointMoveableBuilder withHandler(MoveablePostActionHandler handler) {
            this.handler = Objects.requireNonNull(handler, "A Moveable always needs a MoveablePostActionHandler!");
            return this;
         }

         public EndPointMoveableBuilder withMovingIncrement(int movingIncrement) {
            this.movingIncrement = movingIncrement;
            return this;
         }

         public EndPointMoveable build() {
            Objects.requireNonNull(grid, "Attribute 'grid' must not be null!");
            Objects.requireNonNull(startPosition, "Attribute 'startPosition' must not be null!");
            if (nonNull(shape)) {
               moveable = new EndPointMoveableImpl(grid, startPosition, handler, movingIncrement, shape);
            } else {
               moveable = new EndPointMoveableImpl(grid, startPosition, handler, movingIncrement);
            }
            handler.handlePostConditions(moveable.getGrid(), moveable);
            return this.moveable;
         }

         public MoveableControllerBuilder buildAndReturnParentBuilder() {
            build();
            controllerBuilder.endPointMoveable = moveable;
            return controllerBuilder;
         }

         public EndPointMoveableBuilder withShape(Shape shape) {
            this.shape = shape;
            return this;
         }
      }
   }

   public final Position getCurrentEndPos() {
      return this.moveable.getCurrentEndPos();
   }

   public Moveable getMoveable() {
      return moveable;
   }
}

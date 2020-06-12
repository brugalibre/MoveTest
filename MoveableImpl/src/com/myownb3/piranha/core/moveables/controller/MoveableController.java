/**
 * 
 */
package com.myownb3.piranha.core.moveables.controller;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Supplier;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.ForwardStrategyHandler;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.MoveForwardRequest;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.impl.EndPositionForwardStrategyHandler;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.impl.IncrementalForwardStrategyHandler;
import com.myownb3.piranha.core.moveables.controller.forwardstrategy.impl.WithoutEndPosForwardStrategyHandler;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.exception.NotImplementedException;

/**
 * @author Dominic
 *
 */
public class MoveableController {

   private ForwardStrategyHandler forwardStrategyHandler;
   private Supplier<EndPointMoveable> endPointMoveableSupplier;
   private List<EndPosition> endPosList;
   private PostMoveForwardHandler handler;
   private boolean isRunning;

   /**
    * @param moveable
    */
   public MoveableController(EndPointMoveable moveable, List<EndPosition> endPosList) {
      this(() -> moveable, MovingStrategy.FORWARD, endPosList);
   }

   /**
    * @param supplier
    */
   public MoveableController(Supplier<EndPointMoveable> endPointMoveableSupplier, MovingStrategy strategie, List<EndPosition> endPosList) {
      this.isRunning = true;
      this.endPointMoveableSupplier = endPointMoveableSupplier;
      this.endPosList = endPosList;
      this.handler = result -> {
      };
      forwardStrategyHandler = buildForwardStrategyHandler4Strategy(strategie);
   }

   private ForwardStrategyHandler buildForwardStrategyHandler4Strategy(MovingStrategy movingStrategy) {
      switch (movingStrategy) {
         case FORWARD:
            return new EndPositionForwardStrategyHandler(this);
         case FORWARD_WITHOUT_END_POS:
            return new WithoutEndPosForwardStrategyHandler(this);
         case FORWARD_INCREMENTAL:
            return new IncrementalForwardStrategyHandler();
         default:
            throw new NotImplementedException("Not supported Strategie '" + movingStrategy + "'");
      }
   }

   public void leadMoveable() {
      forwardStrategyHandler.moveMoveableForward(MoveForwardRequest.of(endPointMoveableSupplier.get(), endPosList, handler));
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
      private Supplier<EndPointMoveable> endPointMoveableSupplier;

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

      public MoveableControllerBuilder withLazyMoveable(Supplier<EndPointMoveable> endPointMoveableSupplier) {
         this.endPointMoveableSupplier = endPointMoveableSupplier;
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
         if (nonNull(endPointMoveable)) {
            endPointMoveableSupplier = () -> endPointMoveable;
         }
         MoveableController moveableController = new MoveableController(requireNonNull(endPointMoveableSupplier), movingStrategie, endPosList);
         moveableController.endPosList = endPosList;
         moveableController.handler = postMoveForwardHandler;
         return moveableController;
      }
   }

   public final Position getCurrentEndPos() {
      return endPointMoveableSupplier.get().getCurrentEndPos();
   }

   public EndPointMoveable getMoveable() {
      return endPointMoveableSupplier.get();
   }

   public boolean isRunning() {
      return isRunning;
   }
}

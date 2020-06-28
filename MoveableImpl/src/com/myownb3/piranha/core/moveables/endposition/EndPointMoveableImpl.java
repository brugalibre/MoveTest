/**
 * 
 */
package com.myownb3.piranha.core.moveables.endposition;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static java.util.Objects.requireNonNull;

import java.util.Objects;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.moveables.AbstractMoveable;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.controller.MoveResultImpl;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine;

/**
 * @author Dominic
 *
 */
public class EndPointMoveableImpl extends AbstractMoveable implements EndPointMoveable {

   private EndPosition endPos;
   private double prevDistance;
   private BelligerentParty belligerentParty;

   protected EndPointMoveableImpl(Grid grid, MoveablePostActionHandler handler, int movingIncrement,
         Shape shape, DimensionInfo dimensionInfo, BelligerentParty belligerentParty) {
      super(grid, handler, shape, dimensionInfo, movingIncrement);
      init(belligerentParty);
   }

   private void init(BelligerentParty belligerentParty) {
      this.belligerentParty = belligerentParty;
   }

   @Override
   public void setEndPosition(EndPosition endPos) {
      this.endPos = requireNonNull(endPos, "End-pos must not be null!");
      prevDistance = endPos.calcDistanceTo(position);
      if (handler instanceof EvasionStateMachine) {
         ((EvasionStateMachine) handler).setEndPosition(endPos);
      }
   }

   @Override
   public void moveForward() {
      super.moveForward(velocity);
   }

   @Override
   public void moveBackward() {
      super.moveBackward(velocity);
   }

   @Override
   public MoveResult moveForward2EndPos() {
      double distance = endPos.calcDistanceTo(position);
      if (distance >= getSmallestStepWith()) {
         moveForward(velocity);
         if (endPos.checkIfHasReached(this)) {
            return new MoveResultImpl(distance, prevDistance, true);
         }
         prevDistance = distance;
         return new MoveResultImpl(distance, prevDistance);
      }
      return new MoveResultImpl(distance, prevDistance, true);
   }

   @Override
   public EndPosition getCurrentEndPos() {
      return endPos;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return belligerentParty.isEnemyParty(otherBelligerent.getBelligerentParty());
   }

   public static class EndPointMoveableBuilder {
      private Grid grid;
      private MoveablePostActionHandler handler;
      private int movingIncrement;
      private Shape shape;
      private MoveableControllerBuilder controllerBuilder;
      private BelligerentParty belligerentParty;

      private EndPointMoveableBuilder(MoveableControllerBuilder moveableControllerBuilder) {
         this();
         this.controllerBuilder = moveableControllerBuilder;
      }

      private EndPointMoveableBuilder() {
         movingIncrement = 1;
         this.belligerentParty = BelligerentPartyConst.REBEL_ALLIANCE;
      }

      public EndPointMoveableBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public EndPointMoveableBuilder withMoveablePostActionHandler(MoveablePostActionHandler handler) {
         this.handler = Objects.requireNonNull(handler, "A Moveable always needs a MoveablePostActionHandler!");
         return this;
      }

      public EndPointMoveableBuilder withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      public EndPointMoveableBuilder withMovingIncrement(int movingIncrement) {
         this.movingIncrement = movingIncrement;
         return this;
      }

      public EndPointMoveableBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
      }

      public EndPointMoveable build() {
         Objects.requireNonNull(grid, "Attribute 'grid' must not be null!");
         Objects.requireNonNull(shape, "Attribute 'shape' must not be null!");
         return new EndPointMoveableImpl(grid, handler, movingIncrement, shape, getDefaultDimensionInfo(shape.getDimensionRadius()),
               belligerentParty);
      }

      public MoveableControllerBuilder buildAndReturnParentBuilder() {
         EndPointMoveable endPointMoveable = build();
         return controllerBuilder.withMoveable(endPointMoveable);
      }

      public static EndPointMoveableBuilder builder() {
         return new EndPointMoveableBuilder();
      }

      public static EndPointMoveableBuilder builder(MoveableControllerBuilder moveableControllerBuilder) {
         return new EndPointMoveableBuilder(moveableControllerBuilder);
      }
   }
}

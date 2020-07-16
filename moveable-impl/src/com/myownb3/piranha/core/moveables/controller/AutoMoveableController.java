package com.myownb3.piranha.core.moveables.controller;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.UUID;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.destruction.Damage;
import com.myownb3.piranha.core.battle.destruction.Destructible;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.Destructive;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.moveables.AutoMoveable.AbstractAutoMoveableBuilder;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.statemachine.EvasionStateMachine;

public class AutoMoveableController extends EndPointMoveableImpl implements AutoDetectable, Destructible, Destructive, Belligerent {

   private AutoDetectable autoDetectableDelegate;
   private DestructionHelper destructionHelper;
   private MoveableController moveableController;

   public AutoMoveableController(EvasionStateMachine evasionStateMachine, MoveablePostActionHandler handler, DimensionInfo dimensionInfo, Grid grid,
         Shape shape, int velocity, BelligerentParty belligerentParty) {
      super(grid, evasionStateMachine, handler, velocity, shape, dimensionInfo, belligerentParty);
      setName(UUID.randomUUID().toString());
   }

   @Override
   public void autodetect() {
      moveableController.leadMoveable();
      autoDetectableDelegate.autodetect();
   }

   @Override
   public void onCollision(List<GridElement> destructives) {
      destructionHelper.onCollision(destructives);
   }

   @Override
   public Damage getDamage() {
      return destructionHelper.getDamage();
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   public static class AutoMoveableControllerBuilder extends AbstractAutoMoveableBuilder<AutoMoveableController, AutoMoveableControllerBuilder> {

      private MoveableController moveableController;
      private EvasionStateMachine evasionStateMachine;

      private AutoMoveableControllerBuilder() {
         super();
      }

      @Override
      protected AutoMoveableControllerBuilder getThis() {
         return this;
      }

      @Override
      public AutoMoveableController build() {
         requireNonNull(destructionHelper);
         requireNonNull(moveableController);
         requireNonNull(autoDetectableDelegate);
         requireNonNull(dimensionInfo);
         requireNonNull(belligerentParty);
         requireNonNull(evasionStateMachine);

         // The handler must be a EvasionStateMachine
         AutoMoveableController autoMoveable =
               new AutoMoveableController(evasionStateMachine, handler, dimensionInfo, grid, shape, velocity, belligerentParty);
         autoMoveable.autoDetectableDelegate = autoDetectableDelegate;
         autoMoveable.destructionHelper = destructionHelper;
         autoMoveable.moveableController = moveableController;
         return autoMoveable;
      }

      public AutoMoveableControllerBuilder withMoveableController(MoveableController moveableController) {
         this.moveableController = moveableController;
         return this;
      }

      public AutoMoveableControllerBuilder withEvasionStateMachine(EvasionStateMachine evasionStateMachine) {
         this.evasionStateMachine = evasionStateMachine;
         return this;
      }

      public static AutoMoveableControllerBuilder builder() {
         return new AutoMoveableControllerBuilder();
      }
   }
}

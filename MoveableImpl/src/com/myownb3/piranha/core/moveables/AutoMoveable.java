package com.myownb3.piranha.core.moveables;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.destruction.Destructible;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.destruction.Destructive;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.weapon.AutoDetectable;

public class AutoMoveable extends AbstractMoveable implements AutoDetectable, Destructible, Destructive, Belligerent {

   private AutoDetectable autoDetectableDelegate;
   private DestructionHelper destructionHelper;
   private BelligerentParty belligerentParty;

   public AutoMoveable(AutoDetectable autoDetectableDelegate, DestructionHelper destructionHelper, BelligerentParty belligerentParty,
         DimensionInfo dimensionInfo, Grid grid,
         MoveablePostActionHandler handler, Shape shape,
         int velocity) {
      super(grid, handler, shape, dimensionInfo, velocity);
      this.autoDetectableDelegate = autoDetectableDelegate;
      this.destructionHelper = destructionHelper;
      this.belligerentParty = belligerentParty;
   }

   @Override
   public void autodetect() {
      moveForward();
      autoDetectableDelegate.autodetect();
   }

   @Override
   public Damage getDamage() {
      return destructionHelper.getDamage();
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   public static class AutoMoveableBuilder extends AbstractMoveableBuilder<AutoMoveable, AutoMoveableBuilder> {

      private AutoDetectable autoDetectableDelegate;
      private DimensionInfo dimensionInfo;
      private DestructionHelper destructionHelper;
      private BelligerentParty belligerentParty;

      public AutoMoveableBuilder() {
         super();
         autoDetectableDelegate = () -> {
         };
      }

      @Override
      public AutoMoveable build() {
         moveable = new AutoMoveable(autoDetectableDelegate, destructionHelper, belligerentParty, dimensionInfo, grid, handler, shape, velocity);
         return (AutoMoveable) this.moveable;
      }

      public AutoMoveableBuilder withAutoDetectable(AutoDetectable autoDetectableDelegate) {
         this.autoDetectableDelegate = autoDetectableDelegate;
         return this;
      }

      public AutoMoveableBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
      }

      public AutoMoveableBuilder withDestructionHelper(DestructionHelper destructionHelper) {
         this.destructionHelper = destructionHelper;
         return this;
      }

      public AutoMoveableBuilder withDimensionInfo(DimensionInfo dimensionInfo) {
         this.dimensionInfo = dimensionInfo;
         return this;
      }

      public static AutoMoveableBuilder builder() {
         return new AutoMoveableBuilder();
      }

      @Override
      protected AutoMoveableBuilder getThis() {
         return this;
      }
   }
}

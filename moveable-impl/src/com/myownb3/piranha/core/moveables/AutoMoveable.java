package com.myownb3.piranha.core.moveables;

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
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;

public class AutoMoveable extends AbstractMoveable implements AutoDetectable, Destructible, Destructive, Belligerent {

   private AutoDetectable autoDetectableDelegate;
   private DestructionHelper destructionHelper;
   private BelligerentParty belligerentParty;

   public AutoMoveable(DimensionInfo dimensionInfo, Grid grid, MoveablePostActionHandler handler, Shape shape, int velocity) {
      super(grid, handler, shape, dimensionInfo, velocity);
      setName(UUID.randomUUID().toString());
   }

   @Override
   public void autodetect() {
      moveForward();
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

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   public abstract static class AbstractAutoMoveableBuilder<V extends AbstractMoveable, T extends AbstractAutoMoveableBuilder<V, T>>
         extends AbstractMoveableBuilder<V, T> {
      protected AutoDetectable autoDetectableDelegate;
      protected DimensionInfo dimensionInfo;
      protected DestructionHelper destructionHelper;
      protected BelligerentParty belligerentParty;

      protected AbstractAutoMoveableBuilder() {
         super();
         autoDetectableDelegate = () -> {
         };
      }

      public T withAutoDetectable(AutoDetectable autoDetectableDelegate) {
         this.autoDetectableDelegate = autoDetectableDelegate;
         return getThis();
      }

      public T withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return getThis();
      }

      public T withDestructionHelper(DestructionHelper destructionHelper) {
         this.destructionHelper = destructionHelper;
         return getThis();
      }

      public T withDimensionInfo(DimensionInfo dimensionInfo) {
         this.dimensionInfo = dimensionInfo;
         return getThis();
      }
   }
   public static class AutoMoveableBuilder extends AbstractAutoMoveableBuilder<AutoMoveable, AutoMoveableBuilder> {

      @Override
      public AutoMoveable build() {
         requireNonNull(destructionHelper);
         AutoMoveable autoMoveable = new AutoMoveable(dimensionInfo, grid, handler, shape, velocity);
         autoMoveable.autoDetectableDelegate = autoDetectableDelegate;
         autoMoveable.destructionHelper = destructionHelper;
         autoMoveable.belligerentParty = belligerentParty;
         return autoMoveable;
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

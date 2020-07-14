/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.obstacle;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.UUID;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DamageImpl;
import com.myownb3.piranha.core.battle.destruction.DefaultSelfDestructiveImpl;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.destruction.HealthImpl;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.moveables.AbstractMoveable;

/**
 * @author Dominic
 *
 */
public class MoveableObstacleImpl extends AbstractMoveable implements Obstacle, Belligerent, AutoDetectable {

   private DestructionHelper destructionHelper;
   private BelligerentParty belligerentParty;

   private MoveableObstacleImpl(Grid grid, Shape shape, DimensionInfo dimensionInfo, BelligerentParty belligerentParty, double damage, double health,
         int velocity) {
      super(grid, shape, dimensionInfo, velocity);
      this.destructionHelper = getDestructionHelper(damage, health);
      this.belligerentParty = belligerentParty;
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelper.onCollision(gridElements);
   }

   private DestructionHelper getDestructionHelper(double damage, double health) {
      return DestructionHelperBuilder.builder()
            .withDamage(DamageImpl.of(damage))
            .withHealth(HealthImpl.of(health))
            .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(getVelocity()))
            .withOnDestroyedCallbackHandler(() -> grid.remove(this))
            .build();
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   @Override
   public void autodetect() {
      moveForward();
   }

   public static class MoveableObstacleBuilder extends AbstractGridElementBuilder<MoveableObstacleImpl, MoveableObstacleBuilder> {

      private DestructionHelper destructionHelper;
      private double damage;
      private double health;
      private Integer velocity;
      private BelligerentParty belligerentParty;

      private MoveableObstacleBuilder() {
         damage = 3;
         health = 500;
         belligerentParty = BelligerentPartyConst.GALACTIC_EMPIRE;// By default, a MoveableObstacleImpl is a member of the galactic empire
      }

      public static MoveableObstacleBuilder builder() {
         return new MoveableObstacleBuilder();
      }

      public MoveableObstacleBuilder withHealth(double health) {
         this.health = health;
         return this;
      }

      public MoveableObstacleBuilder withDamage(double damage) {
         this.damage = damage;
         return this;
      }

      public MoveableObstacleBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public MoveableObstacleBuilder withDestructionHelper(DestructionHelper destructionHelper) {
         this.destructionHelper = destructionHelper;
         return this;
      }

      @Override
      protected MoveableObstacleBuilder getThis() {
         return this;
      }

      @Override
      public MoveableObstacleImpl build() {
         requireNonNull(shape, "A MoveableObstacle needs a shape!");
         requireNonNull(velocity, "A MoveableObstacle needs a velocity!");
         if (isNull(dimensionInfo)) {
            dimensionInfo = getDefaultDimensionInfo(shape.getDimensionRadius());
         }
         MoveableObstacleImpl moveableObstacleImpl = new MoveableObstacleImpl(grid, shape, dimensionInfo, belligerentParty, damage, health, velocity);
         if (nonNull(destructionHelper)) {
            moveableObstacleImpl.destructionHelper = destructionHelper;
         }
         moveableObstacleImpl.setName(UUID.randomUUID().toString());
         return moveableObstacleImpl;
      }
   }
}

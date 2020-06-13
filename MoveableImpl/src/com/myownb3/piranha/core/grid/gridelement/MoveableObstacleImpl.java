/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DefaultSelfDestructiveImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveable;

/**
 * @author Dominic
 *
 */
public class MoveableObstacleImpl extends AbstractMoveable implements Obstacle, Belligerent {

   private DestructionHelper destructionHelper;

   private MoveableObstacleImpl(Grid grid, Position position, Shape shape, double damage, double health) {
      super(grid, position, shape);
      this.destructionHelper = getDestructionHelper(damage, health);
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
      return BelligerentPartyConst.GALACTIC_EMPIRE;// So far, a MoveableObstacleImpl is a member of the galactic empire
   }

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return getBelligerentParty().isEnemyParty(otherBelligerent.getBelligerentParty());
   }

   public static class MoveableObstacleBuilder extends AbstractGridElementBuilder<MoveableObstacleImpl, MoveableObstacleBuilder> {

      private DestructionHelper destructionHelper;
      private double damage;
      private double health;

      private MoveableObstacleBuilder() {
         damage = 3;
         health = 500;
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
         MoveableObstacleImpl moveableObstacleImpl;
         requireNonNull(shape, "A MoveableObstacle needs a shape!");
         moveableObstacleImpl = new MoveableObstacleImpl(grid, position, shape, damage, health);
         if (nonNull(destructionHelper)) {
            moveableObstacleImpl.destructionHelper = destructionHelper;
         }
         return moveableObstacleImpl;
      }
   }
}

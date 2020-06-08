/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

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
public class MoveableObstacleImpl extends AbstractMoveable implements Obstacle {

   private DestructionHelper destructionHelper;

   private MoveableObstacleImpl(Grid grid, Position position, double damage, double health) {
      super(grid, position);
      this.destructionHelper = getDestructionHelper(damage, health);
   }

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
         if (isNull(shape)) {
            moveableObstacleImpl = new MoveableObstacleImpl(grid, position, damage, health);
         } else {
            moveableObstacleImpl = new MoveableObstacleImpl(grid, position, shape, damage, health);
         }
         if (nonNull(destructionHelper)) {
            moveableObstacleImpl.destructionHelper = destructionHelper;
         }
         return moveableObstacleImpl;
      }
   }
}

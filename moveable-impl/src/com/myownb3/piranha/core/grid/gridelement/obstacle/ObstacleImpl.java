/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.obstacle;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DamageImpl;
import com.myownb3.piranha.core.battle.destruction.DefaultSelfDestructiveImpl;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.destruction.HealthImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

/**
 * @author Dominic
 *
 */
public class ObstacleImpl extends AbstractGridElement implements Obstacle, Belligerent {

   private static final int OBSTACLE_DAMAGE = 3;
   private static final int OBSTACLE_HEALTH = 5;

   private DestructionHelper destructionHelper;

   private ObstacleImpl(Grid grid, Shape shape) {
      super(grid, shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
      destructionHelper = getDestructionHelper();
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelper.onCollision(gridElements);
   }

   private DestructionHelper getDestructionHelper() {
      return DestructionHelperBuilder.builder()
            .withDamage(DamageImpl.of(OBSTACLE_DAMAGE))
            .withHealth(HealthImpl.of(OBSTACLE_HEALTH))
            .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(0))
            .withOnDestroyedCallbackHandler(() -> grid.remove(this))
            .build();
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return BelligerentPartyConst.GALACTIC_EMPIRE;// So far, a ObstacleImpl is a member of the galactic empire
   }

   public static class ObstacleBuilder extends AbstractGridElementBuilder<ObstacleImpl, ObstacleBuilder> {

      private DestructionHelper destructionHelper;

      private ObstacleBuilder() {
         // private
      }

      public static ObstacleBuilder builder() {
         return new ObstacleBuilder();
      }

      public ObstacleBuilder withDestructionHelper(DestructionHelper destructionHelper) {
         this.destructionHelper = destructionHelper;
         return this;
      }

      @Override
      protected ObstacleBuilder getThis() {
         return this;
      }

      @Override
      public ObstacleImpl build() {
         requireNonNull(shape, "A Obstacle always needs a shape!");
         ObstacleImpl obstacleImpl = new ObstacleImpl(grid, shape);
         if (nonNull(destructionHelper)) {
            obstacleImpl.destructionHelper = destructionHelper;
         }
         return obstacleImpl;
      }
   }
}

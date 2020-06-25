/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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

/**
 * @author Dominic
 *
 */
public class ObstacleImpl extends AbstractGridElement implements Obstacle, Belligerent {

   private static final int OBSTACLE_DAMAGE = 3;
   private static final int OBSTACLE_HEALTH = 5;

   private DestructionHelper destructionHelper;

   private ObstacleImpl(Grid grid, Position position) {
      super(grid, position);
      destructionHelper = getDestructionHelper();
   }

   private ObstacleImpl(Grid grid, Position position, Shape shape) {
      super(grid, position, shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
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

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return getBelligerentParty().isEnemyParty(otherBelligerent.getBelligerentParty());
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
         ObstacleImpl obstacleImpl;
         if (isNull(shape)) {
            obstacleImpl = new ObstacleImpl(grid, position);
         } else {
            obstacleImpl = new ObstacleImpl(grid, position, shape);
         }
         if (nonNull(destructionHelper)) {
            obstacleImpl.destructionHelper = destructionHelper;
         }
         return obstacleImpl;
      }
   }
}

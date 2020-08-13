/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.obstacle;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

/**
 * @author Dominic
 *
 */
public class ObstacleImpl extends AbstractGridElement implements Obstacle {

   private static final int OBSTACLE_DAMAGE = 3;
   private static final int OBSTACLE_HEALTH = 5;

   private DestructionHelper destructionHelper;
   private BelligerentParty belligerentParty;

   private ObstacleImpl(Grid grid, Shape shape, BelligerentParty belligerentParty) {
      super(shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
      this.belligerentParty = belligerentParty;
      destructionHelper = getDestructionHelper(grid);
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelper.onCollision(gridElements);
   }

   private DestructionHelper getDestructionHelper(Grid grid) {
      return DestructionHelperBuilder.builder()
            .withDamage(OBSTACLE_DAMAGE)
            .withHealth(OBSTACLE_HEALTH)
            .withSelfDestructiveDamage(0)
            .withDestroyedAudioClip(AudioClipBuilder.builder()
                  .withAudioResource(AudioConstants.EXPLOSION_SOUND)
                  .build())
            .withOnDestroyedCallbackHandler(() -> {
               grid.remove(this);
            }).build();
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   public static class ObstacleBuilder extends AbstractGridElementBuilder<ObstacleImpl, ObstacleBuilder> {

      private DestructionHelper destructionHelper;
      private BelligerentParty belligerentParty;

      private ObstacleBuilder() {
         this.belligerentParty = BelligerentPartyConst.GALACTIC_EMPIRE;// By default a ObstacleImpl is a member of the galactic empire;
      }

      public static ObstacleBuilder builder() {
         return new ObstacleBuilder();
      }

      public ObstacleBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
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
         ObstacleImpl obstacleImpl = new ObstacleImpl(grid, shape, belligerentParty);
         if (nonNull(destructionHelper)) {
            obstacleImpl.destructionHelper = destructionHelper;
         }
         grid.addElement(obstacleImpl);
         return obstacleImpl;
      }
   }
}

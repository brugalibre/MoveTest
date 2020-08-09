package com.myownb3.piranha.core.grid.gridelement.wall;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;

import java.util.List;

import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.lazy.LazyGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

public class WallGridElement extends AbstractGridElement implements Wall {

   private DestructionHelper destructionHelper;

   protected WallGridElement(Shape shape, DestructionHelper destructionHelper) {
      super(shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
      this.destructionHelper = destructionHelper;
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelper.onCollision(gridElements);
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   public static class WallGridElementBuilder extends AbstractGridElementBuilder<WallGridElement, WallGridElementBuilder> {

      private double health;

      private WallGridElementBuilder() {
         this.health = Integer.MAX_VALUE;
      }

      public WallGridElementBuilder withHealth(double health) {
         this.health = health;
         return this;
      }

      public static WallGridElementBuilder builder() {
         return new WallGridElementBuilder();
      }

      @Override
      protected WallGridElementBuilder getThis() {
         return this;
      }

      @Override
      public WallGridElement build() {
         LazyGridElement lazyWallGridElement = new LazyGridElement();
         DestructionHelper destructionHelper = DestructionHelperBuilder.builder()
               .withDamage(0)
               .withHealth(health)
               .withSelfDestructiveDamage(0)
               .withDestroyedAudioClip(AudioClipBuilder.builder()
                     .withAudioResource(AudioConstants.EXPLOSION_SOUND)
                     .build())
               .withOnDestroyedCallbackHandler(() -> {
                  grid.remove(lazyWallGridElement.getGridElement());
               })
               .build();

         WallGridElement wallGridElement = new WallGridElement(shape, destructionHelper);
         grid.addElement(wallGridElement);
         lazyWallGridElement.setGridElement(wallGridElement);
         return wallGridElement;
      }
   }
}

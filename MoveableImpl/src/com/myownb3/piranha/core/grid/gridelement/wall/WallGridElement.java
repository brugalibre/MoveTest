package com.myownb3.piranha.core.grid.gridelement.wall;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;

import java.util.List;

import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DefaultSelfDestructiveImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class WallGridElement extends AbstractGridElement implements Wall {

   private DestructionHelper destructionHelper;

   protected WallGridElement(Grid grid, Position position, Shape shape) {
      super(grid, position, shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
      this.destructionHelper = getDestructionHelper(0, Integer.MAX_VALUE);
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelper.onCollision(gridElements);
   }

   private DestructionHelper getDestructionHelper(double damage, double health) {
      return DestructionHelperBuilder.builder()
            .withDamage(DamageImpl.of(damage))
            .withHealth(HealthImpl.of(health))
            .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(0))
            .build();
   }

   public static class WallGridElementBuilder extends AbstractGridElementBuilder<WallGridElement, WallGridElementBuilder> {

      private WallGridElementBuilder() {
         // private
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
         return new WallGridElement(grid, position, shape);
      }
   }
}

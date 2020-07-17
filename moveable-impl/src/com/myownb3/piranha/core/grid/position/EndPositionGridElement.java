package com.myownb3.piranha.core.grid.position;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

public class EndPositionGridElement extends AbstractGridElement {

   public EndPositionGridElement(Grid grid, Shape shape) {
      super(grid, shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
   }

   @Override
   public boolean isAvoidable() {
      return false;
   }
}

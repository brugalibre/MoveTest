package com.myownb3.piranha.core.grid.position;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

public final class EndPositionGridElement extends AbstractGridElement {

   public EndPositionGridElement(Grid grid, Shape shape) {
      super(shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
      grid.addElement(this);
   }

   @Override
   public boolean isAvoidable() {
      return false;
   }
}

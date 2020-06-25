package com.myownb3.piranha.core.grid.gridelement.position;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class EndPositionGridElement extends AbstractGridElement {

   public EndPositionGridElement(Grid grid, Position position, Shape shape) {
      super(grid, position, shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
   }
}

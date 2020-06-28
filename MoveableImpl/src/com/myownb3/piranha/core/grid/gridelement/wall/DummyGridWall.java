package com.myownb3.piranha.core.grid.gridelement.wall;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

public class DummyGridWall extends AbstractGridElement implements Wall {
   public DummyGridWall(Grid grid, Position position) {
      super(grid, PositionShapeBuilder.builder()
            .withPosition(position)
            .build(), DimensionInfoBuilder.getDefaultDimensionInfo(1));
   }

   @Override
   public boolean isAvoidable() {
      return false;
   }
}

package com.myownb3.piranha.core.grid.gridelement.wall;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class DummyGridWall extends AbstractGridElement implements Wall {
   public DummyGridWall(Grid grid, Position position) {
      super(grid, position);
   }

   @Override
   public boolean isAvoidable() {
      return false;
   }
}

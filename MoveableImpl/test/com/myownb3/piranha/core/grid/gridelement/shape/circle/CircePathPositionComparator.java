package com.myownb3.piranha.core.grid.gridelement.shape.circle;

import java.util.Comparator;

import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.position.Position;

class CircePathPositionComparator implements Comparator<Position> {

   @Override
   public int compare(Position pos1, Position pos2) {
      Direction dir1 = pos1.getDirection();
      Direction dir2 = pos2.getDirection();
      Double angleAsDouble = dir1.getAngle();
      return angleAsDouble.compareTo(dir2.getAngle());
   }

}
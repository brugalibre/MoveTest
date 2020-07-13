package com.myownb3.piranha.core.grid.gridelement.shape.path.compare;

import java.util.Comparator;

import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

public class PathSegmentEndComparator implements Comparator<PathSegment> {

   private Position position2Compare;

   public PathSegmentEndComparator(Position position2Compare) {
      this.position2Compare = position2Compare;
   }

   @Override
   public int compare(PathSegment pathSegment1, PathSegment pathSegment2) {
      Double end1ToComparePosDistance = position2Compare.calcDistanceTo(pathSegment1.getEnd());
      Double end2ToComparePosDistance = position2Compare.calcDistanceTo(pathSegment2.getEnd());
      return end1ToComparePosDistance.compareTo(end2ToComparePosDistance);
   }
}

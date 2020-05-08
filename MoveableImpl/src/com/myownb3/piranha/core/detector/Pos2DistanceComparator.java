package com.myownb3.piranha.core.detector;

import java.util.Comparator;

import com.myownb3.piranha.core.grid.position.Position;

public class Pos2DistanceComparator implements Comparator<Position> {

   private Position posToComapreTo;

   public Pos2DistanceComparator(Position posToComapreTo) {
      this.posToComapreTo = posToComapreTo;
   }

   @Override
   public int compare(Position pos1, Position pos2) {
      Double detectorPosToPos1 = posToComapreTo.calcDistanceTo(pos1);
      Double detectorPosToPos2 = posToComapreTo.calcDistanceTo(pos2);
      return detectorPosToPos1.compareTo(detectorPosToPos2);
   }
}

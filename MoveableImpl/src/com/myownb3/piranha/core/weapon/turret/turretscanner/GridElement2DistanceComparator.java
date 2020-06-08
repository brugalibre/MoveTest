package com.myownb3.piranha.core.weapon.turret.turretscanner;

import java.util.Comparator;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class GridElement2DistanceComparator implements Comparator<GridElement> {

   private Position posToComapreTo;

   public GridElement2DistanceComparator(Position posToComapreTo) {
      this.posToComapreTo = posToComapreTo;
   }

   @Override
   public int compare(GridElement gridElement1, GridElement gridElement2) {
      Double detectorPosToPos1 = posToComapreTo.calcDistanceTo(gridElement1.getPosition());
      Double detectorPosToPos2 = posToComapreTo.calcDistanceTo(gridElement2.getPosition());
      return detectorPosToPos1.compareTo(detectorPosToPos2);
   }
}

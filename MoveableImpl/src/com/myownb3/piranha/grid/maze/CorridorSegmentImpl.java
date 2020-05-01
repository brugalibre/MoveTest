package com.myownb3.piranha.grid.maze;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;

/**
 * The {@link CorridorSegmentImpl} represents a segment within a corridor
 * 
 * @author Dominic
 *
 */
public class CorridorSegmentImpl implements CorridorSegment {

   private GridElement corridorSegmentWallLeft;
   private GridElement corridorSegmentWallRight;
   private Position corridorSegCenter;

   public CorridorSegmentImpl(GridElement corridorSegmentWallLeft, GridElement corridorSegmentWallRight, Position corridorSegCenter) {
      this.corridorSegmentWallLeft = corridorSegmentWallRight;
      this.corridorSegmentWallRight = corridorSegmentWallLeft;
      this.corridorSegCenter = corridorSegCenter;
   }

   @Override
   public GridElement getCorridorSegmentWallLeft() {
      return corridorSegmentWallLeft;
   }

   @Override
   public GridElement getCorridorSegmentWallRight() {
      return corridorSegmentWallRight;
   }

   @Override
   public Position getCorridorSegCenter() {
      return corridorSegCenter;
   }
}

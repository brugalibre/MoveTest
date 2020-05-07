package com.myownb3.piranha.grid.maze.corridor;

import java.util.Optional;

import com.myownb3.piranha.detector.PlacedDetector;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.maze.corridor.CorridorSegment;

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
   private Optional<PlacedDetector> detectorOpt;

   public CorridorSegmentImpl(GridElement corridorSegmentWallLeft, GridElement corridorSegmentWallRight, Position corridorSegCenter) {
      this.corridorSegmentWallLeft = corridorSegmentWallRight;
      this.corridorSegmentWallRight = corridorSegmentWallLeft;
      this.corridorSegCenter = corridorSegCenter;
      this.detectorOpt = Optional.empty();
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

   @Override
   public void setDetector(PlacedDetector corridorDetector) {
      detectorOpt = Optional.of(corridorDetector);
   }

   @Override
   public Optional<PlacedDetector> getDetector() {
      return detectorOpt;
   }
}

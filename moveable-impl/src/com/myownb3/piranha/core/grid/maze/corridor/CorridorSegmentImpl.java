package com.myownb3.piranha.core.grid.maze.corridor;

import java.util.Optional;

import com.myownb3.piranha.core.detector.PlacedDetector;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link CorridorSegmentImpl} represents a segment within a corridor
 * 
 * @author Dominic
 *
 */
public class CorridorSegmentImpl implements CorridorSegment {

   private Wall corridorSegmentWallLeft;
   private Wall corridorSegmentWallRight;
   private Position corridorSegCenter;
   private Optional<PlacedDetector> detectorOpt;
   private boolean isAngleBend;

   public CorridorSegmentImpl(Wall corridorSegmentWallLeft, Wall corridorSegmentWallRight, Position corridorSegCenter, boolean isAngleBend) {
      this.corridorSegmentWallLeft = corridorSegmentWallRight;
      this.corridorSegmentWallRight = corridorSegmentWallLeft;
      this.corridorSegCenter = corridorSegCenter;
      this.detectorOpt = Optional.empty();
      this.isAngleBend = isAngleBend;
   }

   @Override
   public Wall getCorridorSegmentWallLeft() {
      return corridorSegmentWallLeft;
   }

   @Override
   public Wall getCorridorSegmentWallRight() {
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

   @Override
   public boolean isAngleBend() {
      return isAngleBend;
   }
}

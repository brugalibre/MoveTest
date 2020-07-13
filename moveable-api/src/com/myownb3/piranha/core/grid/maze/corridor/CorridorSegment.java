package com.myownb3.piranha.core.grid.maze.corridor;

import java.util.Optional;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.PlacedDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;
import com.myownb3.piranha.core.grid.position.Position;

public interface CorridorSegment {

   /**
    * @return the left wall as a {@link GridElement}
    */
   Wall getCorridorSegmentWallLeft();

   /**
    * @return the right wall as a {@link GridElement}
    */
   Wall getCorridorSegmentWallRight();

   /**
    * @return the center of this {@link CorridorSegment}
    */
   Position getCorridorSegCenter();

   /**
    * 
    * @return <code>true</code> if this {@link CorridorSegment} is a bended angle segment and <code>false</code> if not
    * 
    */
   boolean isAngleBend();

   /**
    * Sets the given {@link Detector} at the given {@link Position}
    * 
    * @param corridorDetector
    *        the {@link PlacedDetector}
    */
   void setDetector(PlacedDetector corridorDetector);

   /**
    * Returns the Detector of this {@link CorridorSegment} if there is one or an {@link Optional#empty()} if not
    * 
    * @return the Detector of this {@link CorridorSegment} if there is one or an {@link Optional#empty()} if not
    */
   Optional<PlacedDetector> getDetector();
}

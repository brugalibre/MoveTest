package com.myownb3.piranha.detector;

import com.myownb3.piranha.grid.gridelement.position.Position;

/**
 * A {@link PlacedDetector} defines a {@link IDetector} which is placed on a specific {@link Position}
 * 
 * @author DStalder
 *
 */
public interface PlacedDetector {

   /**
    * @return the {@link Position } of the {@link IDetector}
    * 
    */
   Position getPosition();

   /**
    * @return the {@link IDetector} of this {@link PlacedDetector}
    * 
    */
   IDetector getDetector();
}

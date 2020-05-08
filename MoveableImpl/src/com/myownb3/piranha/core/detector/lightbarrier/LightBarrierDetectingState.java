package com.myownb3.piranha.core.detector.lightbarrier;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * Defines the different states a {@link GridElement} can be while passing a {@link LightBarrier}
 * 
 * @author Dominic
 *
 */
public enum LightBarrierDetectingState {

   /** Defines that a {@link GridElement} has not yet been detected by a {@link LightBarrier} */
   NOT_YET_DETECTED,

   /**
    * Defines that a {@link GridElement} has entered a {@link LightBarrier}. That means, it most front {@link Position} has been detected
    */
   ENTERED,

   /**
    * Defines that a {@link GridElement} has been detected by a {@link LightBarrier} with its it most back {@link Position}. As soon as the
    * {@link GridElement} is no longer detected, it has passed the {@link LightBarrier} completely
    */
   LEAVING,
}

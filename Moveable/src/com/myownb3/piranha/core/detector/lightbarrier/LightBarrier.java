package com.myownb3.piranha.core.detector.lightbarrier;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link LightBarrier} is a actual light barrier which recognize when a {@link GridElement} has passed the barrier
 * 
 * @author DStalder
 *
 */
public interface LightBarrier {

   /**
    * Checks if this {@link GridElement} has passed this {@link LightBarrier}. If so a {@link LightBarrierPassedCallbackHandler} is
    * triggered
    * 
    * @param gridElement
    *        the {@link GridElement} to check
    */
   void checkGridElement(GridElement gridElement);

}

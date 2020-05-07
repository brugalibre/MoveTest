package com.myownb3.piranha.detector.lightbarrier;

import com.myownb3.piranha.grid.gridelement.GridElement;

@FunctionalInterface
public interface LightBarrierPassedCallbackHandler {

   /**
    * Is called as soon as a {@link GridElement} has passed a {@link LightBarrierImpl}
    * 
    * @param gridElement
    *        the {@link GridElement} which has passed this {@link LightBarrierImpl}
    */
   void handleLightBarrierTriggered(GridElement gridElement);

}

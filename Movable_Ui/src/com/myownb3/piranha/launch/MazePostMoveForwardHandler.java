package com.myownb3.piranha.launch;

import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierImpl;
import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierPassedCallbackHandler;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.Moveable;

public class MazePostMoveForwardHandler extends DefaultPostMoveForwardHandler implements LightBarrierPassedCallbackHandler {

   private LightBarrierImpl lightBarrier;
   private boolean isNotTriggered = true;

   public MazePostMoveForwardHandler(PostMoveForwardHandlerCtx ctx) {
      super(ctx);
   }

   @Override
   public boolean handlePostConditions(Moveable moveable) {
      super.handlePostConditions(moveable);
      lightBarrier.checkGridElement(moveable);
      return isNotTriggered;
   }

   @Override
   public void handleLightBarrierTriggered(GridElement gridElement) {
      isNotTriggered = false;
      getMoveableController().stop();
   }

   public void setLightBarrier(LightBarrierImpl lightBarrier) {
      this.lightBarrier = lightBarrier;
   }
}

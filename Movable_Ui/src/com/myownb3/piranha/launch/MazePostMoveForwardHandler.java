package com.myownb3.piranha.launch;

import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierImpl;
import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierPassedCallbackHandler;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.Moveable;

public class MazePostMoveForwardHandler extends DefaultPostMoveForwardHandler implements LightBarrierPassedCallbackHandler {

   private LightBarrierImpl lightBarrier;

   public MazePostMoveForwardHandler(PostMoveForwardHandlerCtx ctx) {
      super(ctx);
   }

   @Override
   public void handlePostMoveForward(MoveResult moveResult) {
      super.handlePostMoveForward(moveResult);
      Moveable moveable = getMoveableController().getMoveable();
      lightBarrier.checkGridElement(moveable);
   }

   @Override
   public void handleLightBarrierTriggered(GridElement gridElement) {
      getMoveableController().stop();
   }

   public void setLightBarrier(LightBarrierImpl lightBarrier) {
      this.lightBarrier = lightBarrier;
   }
}

package com.myownb3.piranha.launch;

import java.util.List;

import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierImpl;
import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierPassedCallbackHandler;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.ui.render.Renderer;

public class MazePostMoveForwardHandler extends DefaultPostMoveForwardHandler implements LightBarrierPassedCallbackHandler {

   private LightBarrierImpl lightBarrier;

   public MazePostMoveForwardHandler(MainWindowHolder windowHolder, MoveableControllerHolder moveableControllerHolder,
         List<GridElement> gridElements, List<Renderer> renderers) {
      super(windowHolder, moveableControllerHolder, gridElements, renderers);
   }

   @Override
   public void handlePostMoveForward(MoveResult moveResult) {
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

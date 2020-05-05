package com.myownb3.piranha.launch;

import java.util.List;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.moveables.MoveResult;
import com.myownb3.piranha.ui.render.Renderer;

public class MazePostMoveForwardHandler extends DefaultPostMoveForwardHandler {

   private EndPosition endPos;

   public MazePostMoveForwardHandler(EndPosition endPos, MainWindowHolder windowHolder, MoveableControllerHolder moveableControllerHolder,
         List<GridElement> gridElements, List<Renderer> renderers) {
      super(windowHolder, moveableControllerHolder, gridElements, renderers);
      this.endPos = endPos;
   }

   @Override
   public void handlePostMoveForward(MoveResult moveResult) {

      Position moveablePosition = moveResult.getMoveablePosition();
      if (moveablePosition.getX() >= endPos.getX() && moveablePosition.getY() >= endPos.getY()) {
         getMoveableController().stop();
      }
   }

}

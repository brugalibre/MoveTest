/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;

/**
 * @author Dominic
 *
 */
public class EndPositionGridElementPainter extends AbstractGridElementPainter<GridElement> {

   private boolean isCurrentTargetPos;

   public EndPositionGridElementPainter(GridElement gridElement, Color color, int height, int width) {
      super(gridElement, color, height, width);
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      setDrawMode();
      shapePainter.render(graphicsCtx);
   }

   private void setDrawMode() {
      if (isCurrentTargetPos) {
         shapePainter.setColorSetMode(ColorSetMode.PULSATIVE);
      } else {
         shapePainter.setColorSetMode(ColorSetMode.DEFAULT);
      }
   }

   public void setIsCurrentTargetPosition(Position currentTargetPos) {
      isCurrentTargetPos = currentTargetPos.equals(value.getPosition());
   }

}

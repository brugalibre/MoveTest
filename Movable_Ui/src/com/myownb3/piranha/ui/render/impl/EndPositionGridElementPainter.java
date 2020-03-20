/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.ui.render.RenderContext;

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
      if (isCurrentTargetPos) {

      }
      shapePainter.render(graphicsCtx);
   }

   public void setIsCurrentTargetPosition(Position currentTargetPos) {
      isCurrentTargetPos = currentTargetPos.equals(value.getPosition());
   }

}

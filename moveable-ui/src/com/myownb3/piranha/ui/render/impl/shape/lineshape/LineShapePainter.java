/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape.lineshape;

import java.awt.Color;
import java.awt.Graphics;

import com.myownb3.piranha.core.grid.gridelement.shape.lineshape.ImmutableLineShape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Graphics2DContext;
import com.myownb3.piranha.ui.render.impl.shape.AbstractShapePainter;

/**
 * @author Dominic
 *
 */
public class LineShapePainter extends AbstractShapePainter<ImmutableLineShape> {

   private Color color;

   public LineShapePainter(ImmutableLineShape immutableLineShape, Color color, int height, int width) {
      super(immutableLineShape);
      this.color = color;
   }

   @Override
   public void render(RenderContext graphicsCtx) {

      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics graphics = context.getGraphics2d();
      graphics.setColor(color);

      renderPosition(graphics, value.getRearmostPosition(), value.getForemostPosition());
   }

   private void renderPosition(Graphics graphics, Position beginPos, Position endPos) {
      graphics.drawLine((int) beginPos.getX(), (int) beginPos.getY(), (int) endPos.getX(), (int) endPos.getY());
   }
}

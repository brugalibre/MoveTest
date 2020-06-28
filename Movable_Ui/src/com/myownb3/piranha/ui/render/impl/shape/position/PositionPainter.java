/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape.position;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;

/**
 * @author Dominic
 *
 */
public class PositionPainter extends Drawable<PositionShape> {
   private Color color;

   public PositionPainter(PositionShape pointShape, Color color, int height, int width) {
      super(pointShape);

      this.color = color;
      Position position = pointShape.getPosition();
      setBounds(new Rectangle((int) position.getX(), (int) position.getY(), height, width));
   }

   @Override
   public void render(RenderContext graphicsCtx) {

      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();
      graphics.setColor(color);

      renderPosition(graphics, value.getPosition());
   }

   private void renderPosition(Graphics graphics, Position position) {
      graphics.drawRoundRect((int) position.getX(), (int) position.getY(), width, height, 2, 2);
      graphics.fillRoundRect((int) position.getX(), (int) position.getY(), width, height, 2, 2);
   }
}

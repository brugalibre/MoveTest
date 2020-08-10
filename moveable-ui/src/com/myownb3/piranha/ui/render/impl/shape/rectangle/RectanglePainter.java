/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape.rectangle;

import java.awt.Color;
import java.awt.Graphics2D;

import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.Graphics2DContext;

/**
 * @author Dominic
 *
 */
public class RectanglePainter extends Drawable<Rectangle> {
   private Color color;

   public RectanglePainter(Rectangle rectangle, Color color) {
      super(rectangle);

      Position upperLeftPosition = rectangle.getUpperLeftPosition();
      this.color = color;
      setBounds(new java.awt.Rectangle((int) upperLeftPosition.getX(), (int) upperLeftPosition.getY(), (int) rectangle.getWidth(),
            (int) rectangle.getHeight()));
   }

   @Override
   public void render(RenderContext graphicsCtx) {

      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics2D graphics = context.getGraphics2d();
      graphics.setColor(color);

      renderRectangle(graphics);
   }

   @Override
   public void setBounds(int x, int y, int height, int width) {
      Position center = value.getCenter();
      int newX = (int) Math.floor(center.getX() - value.getHeight() / 2);
      int newY = (int) Math.floor(center.getY() - value.getWidth() / 2);
      int newWidth = (int) value.getWidth();
      int newHeight = (int) value.getHeight();
      super.setBounds(newX, newY, newWidth, newHeight);
   }

   private void renderRectangle(Graphics2D graphics) {
      setBounds(0, 0, 0, 0);
      double angle2Rotate = RectanglePaintUtil.getRectangleRotationAngle(value);
      graphics.rotate(angle2Rotate, (int) value.getCenter().getX(), (int) value.getCenter().getY());
      graphics.fill3DRect(x, y, width, height, true);
      graphics.rotate(-angle2Rotate, (int) value.getCenter().getX(), (int) value.getCenter().getY());
   }
}

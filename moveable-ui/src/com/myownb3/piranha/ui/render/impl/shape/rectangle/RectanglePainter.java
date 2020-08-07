/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape.rectangle;

import static com.myownb3.piranha.ui.render.impl.shape.ShapePaintUtil.getPoligon4Path;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

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
   private boolean drawBorder;

   public RectanglePainter(Rectangle rectangle, Color color, boolean drawBorder) {
      this(rectangle, color);
      this.drawBorder = drawBorder;
   }

   public RectanglePainter(Rectangle rectangle, Color color) {
      super(rectangle);

      Position upperLeftPosition = rectangle.getUpperLeftPosition();
      this.color = color;
      setBounds(new java.awt.Rectangle((int) upperLeftPosition.getX(), (int) upperLeftPosition.getY(), (int) rectangle.getWidth(),
            (int) rectangle.getHeight()));
      this.drawBorder = false;
   }

   @Override
   public void render(RenderContext graphicsCtx) {

      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics graphics = context.getGraphics2d();
      graphics.setColor(color);

      renderRectangle(graphics);
   }

   @Override
   private void renderRectangle(Graphics graphics) {
      Polygon polygon = getPoligon4Path(value);
      graphics.fillPolygon(polygon);
      if (drawBorder) {
         drawBorder(graphics, polygon);
      }
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

   private void drawBorder(Graphics graphics, Polygon polygon) {
      graphics.setColor(Color.BLACK);
      graphics.drawPolygon(polygon);
   }
}

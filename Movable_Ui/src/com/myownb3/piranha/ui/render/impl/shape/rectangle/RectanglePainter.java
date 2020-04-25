/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape.rectangle;

import static com.myownb3.piranha.ui.render.impl.shape.ShapePaintUtil.getPoligon4Path;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;

/**
 * @author Dominic
 *
 */
public class RectanglePainter extends Drawable<Rectangle> implements Renderer {
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

      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();
      graphics.setColor(color);

      renderRectangle(graphics);
   }

   private void renderRectangle(Graphics graphics) {
      Polygon polygon = getPoligon4Path(value);
      graphics.fillPolygon(polygon);
   }

}

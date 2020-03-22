/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape.circle;

import static com.myownb3.piranha.ui.render.impl.shape.ShapePaintUtil.getPoligon4Path;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.shape.Circle;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;
import com.myownb3.piranha.ui.render.impl.shape.PaintMode;

/**
 * @author Dominic
 *
 */
public class CirclePainter extends Drawable<Circle> implements Renderer {

   private Color color;
   private PaintMode paintMode;

   public CirclePainter(Circle circle, PaintMode paintMode, Color color, int height, int width) {
      super(circle);
      this.color = color;
      this.width = 2;
      this.height = 2;
      this.paintMode = paintMode;
   }

   @Override
   public void render(RenderContext graphicsCtx) {

      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();

      renderPosition(graphics);
   }

   private void renderPosition(Graphics graphics) {
      switch (paintMode) {
         case PAINT_PATH:
            drawPath(graphics);
            break;
         case SHAPE:
            drawCircle(graphics);
         default:
            break;
      }
   }

   private void drawCircle(Graphics graphics) {
      Position center = value.getCenter();
      int radius = value.getRadius();
      int yCoordinate = (int) (center.getY() - radius);
      int xCoordinate = (int) (center.getX() - radius);

      graphics.setColor(color);
      graphics.fillOval(xCoordinate, yCoordinate, radius * 2, radius * 2);
   }

   private void drawPath(Graphics graphics) {
      graphics.setColor(Color.black);
      Polygon polygon = getPoligon4Path(value);
      graphics.drawPolygon(polygon);
   }
}

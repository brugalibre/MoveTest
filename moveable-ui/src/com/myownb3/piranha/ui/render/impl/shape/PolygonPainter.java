/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape;

import static com.myownb3.piranha.ui.render.impl.shape.ShapePaintUtil.getPoligon4Path;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Graphics2DContext;

/**
 * @author Dominic
 *
 */
public class PolygonPainter extends AbstractShapePainter<Shape> {
   private Color color;
   private boolean drawBorder;

   public PolygonPainter(Shape shape, Color color, boolean drawBorder) {
      this(shape, color);
      this.drawBorder = drawBorder;
   }

   public PolygonPainter(Shape shape, Color color) {
      super(shape);
      this.color = color;
      this.drawBorder = false;
   }

   @Override
   public void render(RenderContext graphicsCtx) {

      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics graphics = context.getGraphics2d();
      graphics.setColor(color);

      renderPolygon(graphics);
   }

   private void renderPolygon(Graphics graphics) {
      Polygon polygon = getPoligon4Path(value);
      graphics.fillPolygon(polygon);
      if (drawBorder) {
         drawBorder(graphics, polygon);
      }
   }

   private void drawBorder(Graphics graphics, Polygon polygon) {
      graphics.setColor(Color.BLACK);
      graphics.drawPolygon(polygon);
   }
}

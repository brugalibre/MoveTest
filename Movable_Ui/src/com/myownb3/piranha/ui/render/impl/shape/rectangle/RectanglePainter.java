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
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;

/**
 * @author Dominic
 *
 */
public class RectanglePainter extends Drawable<Rectangle> implements Renderer {
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
      this.drawBorder = true;
   }

   @Override
   public void render(RenderContext graphicsCtx) {

      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();
      graphics.setColor(color);

      renderRectangle(graphics);
      //      drawMoveableDirection(graphics, value.getForemostPosition());
   }

   //   private void drawMoveableDirection(Graphics graphics, Position gridElemPos) {
   //      graphics.setColor(Color.BLACK);
   //      drawDirectionFromPosition(graphics, gridElemPos, gridElemPos.getDirection().getVector(), 30);
   //   }
   //
   //   private static void drawDirectionFromPosition(Graphics graphics, Position position, Float64Vector directionVectorIn, int length) {
   //      Float64Vector directionVector = directionVectorIn.times(length * 10);
   //      int gridElemX1 = round(position.getX());
   //      int gridElemY1 = round(position.getY());
   //      int gridElemX2 = gridElemX1 + round(directionVector.getValue(0));
   //      int gridElemY2 = gridElemY1 + round(directionVector.getValue(1));
   //      graphics.drawLine(gridElemX1, gridElemY1, gridElemX2, gridElemY2);
   //   }

   //   private static int round(double x) {
   //      return (int) MathUtil.round(x, 0);
   //   }

   private void renderRectangle(Graphics graphics) {
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

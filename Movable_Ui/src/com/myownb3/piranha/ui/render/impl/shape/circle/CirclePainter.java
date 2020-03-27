/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape.circle;

import static com.myownb3.piranha.ui.render.impl.shape.ShapePaintUtil.getPoligon4Path;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.Circle;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.shape.PaintMode;

/**
 * @author Dominic
 *
 */
public class CirclePainter extends Drawable<Circle> implements Renderer, ActionListener {

   private Color color;
   private Color initColor;
   private PaintMode paintMode;
   private Timer timer;
   private boolean switchIt;

   public CirclePainter(Circle circle, PaintMode paintMode, Color color, int height, int width) {
      super(circle);
      this.color = color;
      this.initColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
      this.width = 2;
      this.height = 2;
      this.paintMode = paintMode;
      timer = new Timer(700, this);
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

   @Override
   public void setColorSetMode(ColorSetMode drawMode) {
      ColorSetMode drawModeBefore = this.colorSetMode;
      super.setColorSetMode(drawMode);
      if (isNowPulsative(drawMode, drawModeBefore)) {
         timer.start();
         color = initColor.brighter();
      } else if (isNotPulsativeAnymore(drawMode, drawModeBefore)) {
         color = initColor;
         timer.stop();
      }
   }

   private static boolean isNotPulsativeAnymore(ColorSetMode drawMode, ColorSetMode drawModeBefore) {
      return drawModeBefore == ColorSetMode.PULSATIVE && drawMode != ColorSetMode.PULSATIVE;
   }

   private static boolean isNowPulsative(ColorSetMode drawMode, ColorSetMode drawModeBefore) {
      return drawModeBefore != ColorSetMode.PULSATIVE && drawMode == ColorSetMode.PULSATIVE;
   }

   @Override
   public void actionPerformed(ActionEvent arg0) {
      timer.restart();
      if (switchIt) {
         color = initColor.brighter();
      } else {
         color = initColor;
      }
      switchIt = !switchIt;
   }
}

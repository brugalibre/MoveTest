/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;

/**
 * @author Dominic
 *
 */
public class PositionListPainter implements Renderer<List<Position>> {

   private List<Position> posList;
   private Color color;
   private int height;
   private int width;

   public PositionListPainter(List<Position> posList, Color color, int height, int width) {
      this.posList = posList;
      this.color = color;
      this.height = height;
      this.width = width;
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();
      graphics.setColor(color);

      int i = 0;
      for (Position position : posList) {
         i++;
         if (i % 20 == 0) {
            renderPosition(graphics, position);
         }
      }
   }

   private void renderPosition(Graphics graphics, Position position) {
      graphics.drawRoundRect((int) position.getX(), (int) position.getY(), width, height, 2, 2);
      graphics.fillRoundRect((int) position.getX(), (int) position.getY(), width, height, 2, 2);
   }

   @Override
   public void setBounds(Rectangle bounds) {
      this.height = bounds.height;
      this.width = bounds.width;
   }

   public void setPositions(List<Position> positions) {
      posList = positions;
   }

   @Override
   public ColorSetMode getColorSetMode() {
      return ColorSetMode.DEFAULT;
   }

   @Override
   public void setColorSetMode(ColorSetMode drawMode) {
      throw new UnsupportedOperationException("Not allowed here!");
   }

   @Override
   public List<Position> getValue() {
      return posList;
   }
}

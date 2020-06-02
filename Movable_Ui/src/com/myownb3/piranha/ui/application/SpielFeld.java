/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.JComponent;

import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;
import com.myownb3.piranha.ui.render.impl.GridPainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainter;

/**
 * @author Dominic
 *
 */
public class SpielFeld extends JComponent {

   private static final long serialVersionUID = 1L;
   private List<Renderer> renderers;
   private GridPainter gridPainter;

   public SpielFeld(Grid grid, List<Renderer> renderers, int padding, int pointWidth) {
      this.renderers = renderers;

      Dimension gridDimension = grid.getDimension();
      this.gridPainter = new GridPainter(grid, padding, pointWidth, gridDimension.getHeight() + 2 * padding,
            gridDimension.getWidth() + 2 * padding);
   }

   @Override
   protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      Graphics2D g2 = (Graphics2D) graphics;

      paintSpielfeld(g2);
      gridPainter.render(new GraphicsContext(g2));
      paintMoveableContent(g2);
   }

   private void paintMoveableContent(Graphics2D g2) {
      GraphicsContext graphicsContext = new GraphicsContext(g2);
      renderMoveable(graphicsContext);
      synchronized (renderers) {
         renderers.stream()
               .filter(isMoveablePainter().negate())
               .forEach(renderer -> renderer.render(graphicsContext));
      }
   }

   /**
    * @param graphicsContext
    */
   private void renderMoveable(GraphicsContext graphicsContext) {
      synchronized (renderers) {
         renderers.stream()
               .filter(isMoveablePainter())
               .forEach(moveablePainter -> moveablePainter.render(graphicsContext));
      }
   }

   private Predicate<? super Renderer> isMoveablePainter() {
      return renderer -> renderer instanceof MoveablePainter;
   }

   private void paintSpielfeld(Graphics2D g2) {
      g2.setColor(Color.WHITE);
      g2.fillRect(0, 0, getWidth(), getHeight());
   }
}

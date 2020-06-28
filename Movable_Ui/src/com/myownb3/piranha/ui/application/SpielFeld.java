/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;

import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;
import com.myownb3.piranha.ui.render.impl.GridPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;

/**
 * @author Dominic
 *
 */
public class SpielFeld extends JComponent {

   private static final long serialVersionUID = 1L;
   private List<Renderer<? extends GridElement>> renderers;
   private List<PositionListPainter> endPositionRenderers;
   private GridPainter gridPainter;

   public SpielFeld(Grid grid, List<Renderer<? extends GridElement>> renderers, List<PositionListPainter> endPositionRenderers, int padding,
         int pointWidth) {
      this.renderers = renderers;
      this.endPositionRenderers = endPositionRenderers;

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
      synchronized (renderers) {
         renderers.stream()
               .sorted(new RendererComparator())
               .forEach(renderer -> renderer.render(graphicsContext));
      }
      endPositionRenderers.stream()
            .forEach(renderer -> renderer.render(graphicsContext));
   }

   private static class RendererComparator implements Comparator<Renderer<? extends GridElement>> {

      @Override
      public int compare(Renderer<? extends GridElement> o1, Renderer<? extends GridElement> o2) {
         return compareGridElements(o1.getValue(), o2.getValue());
      }

      private int compareGridElements(GridElement value1, GridElement value2) {
         Position gridElem1Pos = value1.getPosition();
         Position gridElem2Pos = value2.getPosition();
         return Double.valueOf(gridElem1Pos.getZ()).compareTo(Double.valueOf(gridElem2Pos.getZ()));
      }
   }

   private void paintSpielfeld(Graphics2D g2) {
      g2.setColor(Color.WHITE);
      g2.fillRect(0, 0, getWidth(), getHeight());
   }
}

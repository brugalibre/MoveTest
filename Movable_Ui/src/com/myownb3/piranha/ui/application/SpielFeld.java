/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
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
   private transient List<Renderer<? extends GridElement>> renderers;
   private transient List<PositionListPainter> endPositionRenderers;
   private transient GridPainter gridPainter;
   private transient Optional<BufferedImage> imageOpt;
   private int padding;

   public SpielFeld(String backgroundImage, Grid grid, List<Renderer<? extends GridElement>> renderers,
         List<PositionListPainter> endPositionRenderers, int padding, int pointWidth) {
      this.renderers = renderers;
      this.padding = padding;
      this.endPositionRenderers = endPositionRenderers;

      Dimension gridDimension = grid.getDimension();
      this.gridPainter = new GridPainter(grid, padding, pointWidth, gridDimension.getHeight() + 2 * padding,
            gridDimension.getWidth() + 2 * padding);
      loadImageOpt(backgroundImage, padding, gridDimension);
   }

   private void loadImageOpt(String backgroundImage, int padding, Dimension gridDimension) {
      BufferedImage dimg = null;
      try {
         dimg = scaleBicubic(ImageIO.read(new File(backgroundImage)), 2, 2);
      } catch (Exception e) {
         // Ignore
      }
      this.imageOpt = Optional.ofNullable(dimg);
   }

   public static BufferedImage scaleBicubic(BufferedImage before, double scaleWidth, double scaleHeight) {
      return scale(before, scaleWidth, scaleHeight, AffineTransformOp.TYPE_BICUBIC);
   }

   private static BufferedImage scale(BufferedImage before, double scaleWidth, double scaleHeight, int type) {
      int w = before.getWidth();
      int h = before.getHeight();
      int w2 = (int) (w * scaleWidth);
      int h2 = (int) (h * scaleHeight);
      BufferedImage after = new BufferedImage(w2, h2, before.getType());
      AffineTransform scaleInstance = AffineTransform.getScaleInstance(scaleWidth, scaleWidth);
      AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, type);
      scaleOp.filter(before, after);
      RescaleOp rescaleOp = new RescaleOp(1.5f, 15, null);
      rescaleOp.filter(after, after); // Source and destination are the same.
      return after;
   }

   @Override
   protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      Graphics2D g2 = (Graphics2D) graphics;

      paintSpielfeldAndGrid(g2);
      paintMoveableContent(g2);
   }

   private void paintSpielfeldAndGrid(Graphics2D g2) {
      if (imageOpt.isPresent()) {
         g2.drawImage(imageOpt.get(), padding, padding, null);
      } else {
         paintSpielfeld(g2);
         gridPainter.render(new GraphicsContext(g2));
      }
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

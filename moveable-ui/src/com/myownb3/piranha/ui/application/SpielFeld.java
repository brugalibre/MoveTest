/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.ui.image.ImageRotator;
import com.myownb3.piranha.ui.image.ImageScaler;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Graphics2DContext;
import com.myownb3.piranha.ui.render.impl.GridPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;

/**
 * @author Dominic
 *
 */
public class SpielFeld extends JComponent {

   private static final long serialVersionUID = 1L;
   private transient List<Renderer<?>> renderers;
   private transient List<PositionListPainter> endPositionRenderers;
   private transient GridPainter gridPainter;
   private transient Optional<BufferedImage> imageOpt;
   private int padding;

   public SpielFeld(String backgroundImage, Grid grid, List<Renderer<?>> renderers,
         List<PositionListPainter> endPositionRenderers, int padding, int pointWidth) {
      this.renderers = renderers;
      this.padding = padding;
      this.endPositionRenderers = endPositionRenderers;

      Dimension gridDimension = grid.getDimension();
      this.gridPainter = new GridPainter(grid, padding, pointWidth, gridDimension.getHeight() + 2 * padding,
            gridDimension.getWidth() + 2 * padding);
      loadImageOpt(backgroundImage, gridDimension);
   }

   private void loadImageOpt(String backgroundImage, Dimension gridDimension) {
      BufferedImage bufferedImage = null;
      try {
         bufferedImage = ImageIO.read(new File(backgroundImage));
         bufferedImage = ImageRotator.rotateImage(bufferedImage, -90);
         double scaledWidth = ((double) gridDimension.getWidth() / bufferedImage.getWidth()) * bufferedImage.getWidth();
         double scaledHeight = ((double) gridDimension.getHeight() / bufferedImage.getHeight()) * bufferedImage.getHeight();
         bufferedImage = ImageScaler.scaleImage(bufferedImage, scaledWidth + 30, scaledHeight);
      } catch (Exception e) {
         // Ignore
      }
      this.imageOpt = Optional.ofNullable(bufferedImage);
   }

   @Override
   protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      Graphics2D g2 = (Graphics2D) graphics;
      setRenderingHints(g2);
      paintSpielfeldAndGrid(g2);
      paintMoveableContent(g2);
   }

   private static void setRenderingHints(Graphics2D g2) {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
      g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
   }

   private void paintSpielfeldAndGrid(Graphics2D g2) {
      if (imageOpt.isPresent()) {
         g2.drawImage(imageOpt.get(), padding, padding, null);
      } else {
         paintSpielfeld(g2);
         gridPainter.render(new Graphics2DContext(g2));
      }
   }

   private void paintMoveableContent(Graphics2D g2) {
      Graphics2DContext graphicsContext = new Graphics2DContext(g2);
      List<Renderer<?>> renderersCopy = new ArrayList<>(renderers);
      renderersCopy.stream()
            .sorted(new RendererComparator())
            .forEach(renderer -> renderer.render(graphicsContext));
      endPositionRenderers.stream()
            .forEach(renderer -> renderer.render(graphicsContext));
   }

   private static class RendererComparator implements Comparator<Renderer<?>> {
      @Override
      public int compare(Renderer<?> o1, Renderer<?> o2) {
         Double heightFromGround1 = o1.getHightFromGround();
         Double heightFromGround2 = o1.getHightFromGround();
         return heightFromGround1.compareTo(heightFromGround2);
      }
   }

   private void paintSpielfeld(Graphics2D g2) {
      g2.setColor(Color.WHITE);
      g2.fillRect(0, 0, getWidth(), getHeight());
   }
}

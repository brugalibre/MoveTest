package com.myownb3.piranha.ui.render.impl.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.Graphics2DContext;
import com.myownb3.piranha.util.MathUtil;

public class ImageSeriesPainter extends Drawable<ImageSeries> {

   private ImageSeries imageSeries;
   private Supplier<Position> imagePositionSupplier;
   private double rotationAngle;

   public ImageSeriesPainter(ImageSeries imageSeries, Supplier<Position> imagePositionSupplier) {
      super(imageSeries);
      this.imageSeries = imageSeries;
      this.imagePositionSupplier = imagePositionSupplier;
      this.rotationAngle = MathUtil.getRandom(360) + 20;
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics2D graphics = context.getGraphics2d();
      if (imageSeries.hasNextImage()) {
         BufferedImage nextImage = imageSeries.getNextImage();
         Position imagePos = imagePositionSupplier.get();
         int imageX = (int) imagePos.getX() - nextImage.getWidth() / 2;
         int imageY = (int) imagePos.getY() - nextImage.getHeight() / 2;
         drawImage(graphics, nextImage, imagePos, imageX, imageY);
      }
   }

   private void drawImage(Graphics2D graphics, BufferedImage nextImage, Position imagePos, int imageX, int imageY) {
      rotate(graphics, imagePos, rotationAngle);
      graphics.drawImage(nextImage, imageX, imageY, null);
      rotate(graphics, imagePos, -rotationAngle);
   }

   private void rotate(Graphics2D graphics, Position imagePos, double angle2Rotate) {
      if (angle2Rotate != 0.0) {
         graphics.rotate(angle2Rotate, (int) imagePos.getX(), (int) imagePos.getY());
      }
   }

   public boolean isPlayback() {
      return imageSeries.isPlayback();
   }

   @Override
   public boolean canBeRemoved() {
      return !isPlayback();
   }

   @Override
   public double getHightFromGround() {
      return imagePositionSupplier.get().getZ();
   }
}

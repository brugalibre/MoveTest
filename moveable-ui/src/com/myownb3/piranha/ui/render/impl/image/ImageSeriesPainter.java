package com.myownb3.piranha.ui.render.impl.image;

import static com.myownb3.piranha.ui.render.impl.image.ImagePainter.rotateAndPaintImage;

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
   private Supplier<Double> imageRotateDegreeSupplier;
   protected Supplier<Position> imagePositionSupplier;

   public ImageSeriesPainter(ImageSeries imageSeries, Supplier<Position> imagePositionSupplier) {
      this(imageSeries, imagePositionSupplier, getRandomRotationDegree());
   }

   private static Supplier<Double> getRandomRotationDegree() {
      double randomRotationDegree = MathUtil.getRandom(360) + 20;
      return () -> randomRotationDegree;
   }

   public ImageSeriesPainter(ImageSeries imageSeries, Supplier<Position> imagePositionSupplier, Supplier<Double> imageRotateDegreeSupplier) {
      super(imageSeries);
      this.imageSeries = imageSeries;
      this.imagePositionSupplier = imagePositionSupplier;
      this.imageRotateDegreeSupplier = imageRotateDegreeSupplier;
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics2D graphics = context.getGraphics2d();
      if (imageSeries.hasNextImage()) {
         BufferedImage nextImage = imageSeries.getNextImage();
         Position imagePos = imagePositionSupplier.get();
         Double degree = imageRotateDegreeSupplier.get();
         rotateAndPaintImage(graphics, nextImage, imagePos, degree);
      }
   }

   @Override
   public boolean canBeRemoved() {
      return !imageSeries.isPlayback();
   }

   @Override
   public double getHightFromGround() {
      return imagePositionSupplier.get().getZ();
   }
}

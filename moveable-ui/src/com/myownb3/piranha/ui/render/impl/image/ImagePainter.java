package com.myownb3.piranha.ui.render.impl.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.Graphics2DContext;

public class ImagePainter extends Drawable<BufferedImage> {

   private Supplier<Position> imagePositionSupplier;
   private Supplier<Double> imageRotateDegreeSupplier;

   public ImagePainter(BufferedImage bufferedImage, Supplier<Position> imagePositionSupplier, Supplier<Double> imageRotateDegreeSupplier) {
      super(bufferedImage);
      this.imagePositionSupplier = imagePositionSupplier;
      this.imageRotateDegreeSupplier = imageRotateDegreeSupplier;
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics2D graphics = context.getGraphics2d();
      Position imagePos = imagePositionSupplier.get();
      double angle2Rotate = imageRotateDegreeSupplier.get();
      rotateAndPaintImage(graphics, value, imagePos, angle2Rotate);
   }

   /**
    * Rotate and paints the given {@link BufferedImage} on the given {@link Graphics2D}
    * 
    * @param graphics
    *        the {@link Graphics2D}
    * @param image
    *        the image to paint
    * @param imagePos
    *        the {@link Position} of the {@link BufferedImage}
    * @param degree
    *        the rotation degree of the {@link BufferedImage}
    */
   public static void rotateAndPaintImage(Graphics2D graphics, BufferedImage image, Position imagePos, Double degree) {
      double imageX = imagePos.getX() - image.getWidth() / 2d;
      double imageY = imagePos.getY() - image.getHeight() / 2d;

      AffineTransform at = new AffineTransform();
      at.translate(imageX, imageY);
      at.rotate(degree, image.getWidth() / 2d, image.getHeight() / 2d);

      graphics.setTransform(at);
      graphics.drawImage(image, 0, 0, null);
      graphics.dispose();
   }

   @Override
   public double getHightFromGround() {
      return imagePositionSupplier.get().getZ();
   }
}

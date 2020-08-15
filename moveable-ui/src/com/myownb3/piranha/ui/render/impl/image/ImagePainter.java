package com.myownb3.piranha.ui.render.impl.image;

import java.awt.Graphics2D;
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
      int imageX = (int) imagePos.getX() - value.getWidth() / 2;
      int imageY = (int) imagePos.getY() - value.getHeight() / 2;

      double angle2Rotate = imageRotateDegreeSupplier.get();
      rotate(graphics, imagePos, angle2Rotate);
      graphics.drawImage(value, imageX, imageY, null);
      rotate(graphics, imagePos, -angle2Rotate);
   }

   private void rotate(Graphics2D graphics, Position imagePos, double angle2Rotate) {
      if (angle2Rotate != 0.0) {
         graphics.rotate(angle2Rotate, (int) imagePos.getX(), (int) imagePos.getY());
      }
   }

   @Override
   public double getHightFromGround() {
      return imagePositionSupplier.get().getZ();
   }
}

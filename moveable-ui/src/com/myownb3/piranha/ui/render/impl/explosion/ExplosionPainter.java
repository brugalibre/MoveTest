package com.myownb3.piranha.ui.render.impl.explosion;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.image.ImageRotator;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.Graphics2DContext;
import com.myownb3.piranha.util.MathUtil;

public class ExplosionPainter extends Drawable<Explosion> {

   private Explosion explosion;
   private Supplier<Position> imagePositionSupplier;
   private double randomAngle;

   public ExplosionPainter(Explosion explosion, Supplier<Position> imagePositionSupplier) {
      super(explosion);
      this.explosion = explosion;
      this.imagePositionSupplier = imagePositionSupplier;
      this.randomAngle = MathUtil.getRandom(360) + 20;
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics2D graphics = context.getGraphics2d();
      if (explosion.hasNextImage()) {
         BufferedImage explosionImage = ImageRotator.rotateImage(explosion.getNextExplosionImage(), randomAngle);
         Position gridElemPos = imagePositionSupplier.get();
         int explosionX = (int) gridElemPos.getX() - explosionImage.getWidth() / 2;
         int explosionY = (int) gridElemPos.getY() - explosionImage.getHeight() / 2;
         graphics.drawImage(explosionImage, explosionX, explosionY, null);
      }
   }

   public boolean isExploding() {
      return explosion.isExploding();
   }

   @Override
   public boolean canBeRemoved() {
      return !isExploding();
   }

   @Override
   public double getHightFromGround() {
      return imagePositionSupplier.get().getZ();
   }
}

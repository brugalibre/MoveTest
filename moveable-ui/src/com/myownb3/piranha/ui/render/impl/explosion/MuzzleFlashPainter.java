package com.myownb3.piranha.ui.render.impl.explosion;

import java.util.function.Supplier;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.impl.image.ImageSeriesPainter;

/**
 * The {@link MuzzleFlashPainter} paints the musszle flash
 * 
 * @author Dominic
 *
 */
public class MuzzleFlashPainter extends ImageSeriesPainter {

   private static final double MUZZLE_FLASH_EXPLOSION_RADIUS = 20d;

   public MuzzleFlashPainter(Supplier<Position> furthermostGunPosSupplier) {
      super(Explosion.buildDefaultExplosion(MUZZLE_FLASH_EXPLOSION_RADIUS, true), moveFurthermostGunPosForward(furthermostGunPosSupplier));
   }

   private static Supplier<Position> moveFurthermostGunPosForward(Supplier<Position> furthermostGunPosSupplier) {
      return () -> furthermostGunPosSupplier.get().movePositionForward4Distance(MUZZLE_FLASH_EXPLOSION_RADIUS / 2d);
   }
}

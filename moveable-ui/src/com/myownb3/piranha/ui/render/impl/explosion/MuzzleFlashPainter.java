package com.myownb3.piranha.ui.render.impl.explosion;

import java.util.function.Supplier;

import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link MuzzleFlashPainter} paints the musszle flash
 * 
 * @author Dominic
 *
 */
public class MuzzleFlashPainter extends ExplosionPainter {

   private static final double MUZZLE_FLASH_EXPLOSION_RADIUS = 20d;

   public MuzzleFlashPainter(Supplier<Position> furthermostGunPosSupplier) {
      super(Explosion.buildDefaultExplosionWitzResizeSmaller(MUZZLE_FLASH_EXPLOSION_RADIUS), moveFurthermostGunPosForward(furthermostGunPosSupplier));
   }

   private static Supplier<Position> moveFurthermostGunPosForward(Supplier<Position> furthermostGunPosSupplier) {
      return () -> furthermostGunPosSupplier.get().movePositionForward4Distance(MUZZLE_FLASH_EXPLOSION_RADIUS / 2d);
   }
}

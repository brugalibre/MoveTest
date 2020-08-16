package com.myownb3.piranha.ui.render.impl.missile;

import java.util.function.Supplier;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.image.ImageSeriesPainter;

public class MissileTailPainter extends ImageSeriesPainter {

   private Position originPos;
   private double distanceAfterMissileTailsIsVisible;
   private boolean canEnableMissileTail; // for performance reasons

   public MissileTailPainter(MissileTail missileTail, Supplier<Position> imagePositionSupplier,
         Supplier<Double> imageRotateDegreeSupplier) {
      super(missileTail, imagePositionSupplier, imageRotateDegreeSupplier);
      originPos = imagePositionSupplier.get();
      // since there is also a 'muzzle flash', the visibility of the missile tail can be delayed a bit more
      this.distanceAfterMissileTailsIsVisible = missileTail.getDimensionRadius() * 1.5d;
      this.canEnableMissileTail = false;
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      if (isMissileTailEnabled()) {
         super.render(graphicsCtx);
      }
   }

   private boolean isMissileTailEnabled() {
      return canEnableMissileTail = canEnableMissileTail || isDistanceReached4MissileTailEnabled();
   }

   private boolean isDistanceReached4MissileTailEnabled() {
      double distanceFromMissile2OriginPos = imagePositionSupplier.get().calcDistanceTo(originPos);
      return distanceFromMissile2OriginPos >= distanceAfterMissileTailsIsVisible;
   }
}

/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;
import java.util.Optional;

import com.myownb3.piranha.core.battle.destruction.Destructible;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.countermeasure.DecoyFlareUtil;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.explosion.Explosion;
import com.myownb3.piranha.ui.render.impl.image.ImageSeriesPainter;

/**
 * @author Dominic
 *
 */
public class GridElementPainter extends AbstractGridElementPainter<GridElement> {
   private Optional<ImageSeriesPainter> explosionPainterOpt;

   public GridElementPainter(GridElement gridElement, Color color) {
      super(gridElement, color);
      ImageSeriesPainter explosionPainter = null;
      if (canShowExplosion(gridElement)) {
         explosionPainter = new ImageSeriesPainter(buildDefaultExplosion(gridElement), () -> gridElement.getPosition());
      }
      explosionPainterOpt = Optional.ofNullable(explosionPainter);
   }

   private Explosion buildDefaultExplosion(GridElement gridElement) {
      double dimensionRadius = gridElement.getDimensionInfo().getDimensionRadius();
      return Explosion.buildDefaultExplosion(dimensionRadius, resizeExplosionSmaller(gridElement));
   }

   protected boolean resizeExplosionSmaller(GridElement gridElement) {
      return DecoyFlareUtil.isDecoyFlareGridElement(gridElement);
   }

   protected boolean canShowExplosion(GridElement gridElement) {
      return gridElement instanceof Destructible;
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      if (DestructionHelper.isDestroyed(value)) {
         explosionPainterOpt.ifPresent(explosionPainter -> explosionPainter.render(graphicsCtx));
      } else {
         renderGridElement(graphicsCtx);
      }
   }

   protected void renderGridElement(RenderContext graphicsCtx) {
      super.render(graphicsCtx);
   }

   @Override
   public boolean canBeRemoved() {
      return DestructionHelper.isDestroyed(value)
            && explosionPainterOpt.map(ImageSeriesPainter::canBeRemoved)
                  .orElse(true);
   }
}

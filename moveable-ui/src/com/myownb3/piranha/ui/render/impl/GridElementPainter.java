/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;
import java.util.Optional;

import com.myownb3.piranha.core.battle.destruction.Destructible;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.explosion.Explosion;
import com.myownb3.piranha.ui.render.impl.explosion.ExplosionPainter;

/**
 * @author Dominic
 *
 */
public class GridElementPainter extends AbstractGridElementPainter<GridElement> {
   private Optional<ExplosionPainter> explosionPainterOpt;

   public GridElementPainter(GridElement gridElement, Color color, int height, int width) {
      super(gridElement, color, height, width);
      ExplosionPainter explosionPainter = null;
      if (canShowExplosion(gridElement)) {
         explosionPainter = new ExplosionPainter(Explosion.buildDefaultExplosion(gridElement), gridElement);
      }
      explosionPainterOpt = Optional.ofNullable(explosionPainter);
   }

   private boolean canShowExplosion(GridElement gridElement) {
      return gridElement instanceof Destructible
            && (isNotProjectile(gridElement)
                  || isMissile(gridElement));
   }

   private static boolean isMissile(GridElement gridElement) {
      return ((ProjectileGridElement) gridElement).getProjectileType() == ProjectileTypes.MISSILE;
   }

   private static boolean isNotProjectile(GridElement gridElement) {
      return !(gridElement instanceof ProjectileGridElement);
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
            && explosionPainterOpt.map(ExplosionPainter::canBeRemoved)
                  .orElse(true);
   }
}

package com.myownb3.piranha.ui.render.impl.missile;

import java.awt.Color;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;

public class ProjectileGridElementPainter extends GridElementPainter {

   public ProjectileGridElementPainter(ProjectileGridElement projectileGridElement, Color color) {
      super(projectileGridElement, color);
   }

   @Override
   protected boolean canShowExplosion(GridElement gridElement) {
      return false;
   }
}

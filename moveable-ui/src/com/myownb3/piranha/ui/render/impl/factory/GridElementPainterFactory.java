package com.myownb3.piranha.ui.render.impl.factory;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.missile.MissileGridElementPainter;
import com.myownb3.piranha.ui.render.impl.missile.ProjectileGridElementPainter;

public class GridElementPainterFactory {

   private GridElementPainterFactory() {
      // private 
   }

   public static GridElementPainter createGridElementPainter(GridElement gridElement) {
      if (isProjectile(gridElement)) {
         ProjectileGridElement projectileGridElement = (ProjectileGridElement) gridElement;
         if (isMissile(projectileGridElement)) {
            return new MissileGridElementPainter(projectileGridElement, getColor(gridElement));
         }
         return new ProjectileGridElementPainter(projectileGridElement, getColor(gridElement));
      }
      return new GridElementPainter(gridElement, getColor(gridElement));
   }

   private static boolean isMissile(ProjectileGridElement gridElement) {
      return gridElement.getProjectileType() == ProjectileTypes.MISSILE;
   }

   private static boolean isProjectile(GridElement gridElement) {
      return gridElement instanceof ProjectileGridElement;
   }
}

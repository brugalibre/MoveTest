package com.myownb3.piranha.ui.render.util;

import java.awt.Color;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyTypes;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositionGridElement;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class GridElementColorUtil {

   private GridElementColorUtil() {
      // private
   }

   /**
    * Returns the {@link Color} which is used to render the given {@link GridElement}
    * 
    * @param gridElement
    *        the GridElement
    * @return the {@link Color} which is used to render the given {@link GridElement}
    */
   public static Color getColor(GridElement gridElement) {

      if (gridElement instanceof Obstacle) {
         return Color.RED;
      } else if (gridElement instanceof Tank) {
         return getTankColor(((Belligerent) gridElement).getBelligerentParty());
      } else if (gridElement instanceof Turret) {
         return getTurretColor(((Belligerent) gridElement).getBelligerentParty());
      } else if (gridElement instanceof Projectile) {
         return Color.BLACK;
      } else if (gridElement instanceof Moveable) {
         return new Color(0, 206, 209).darker();
      } else if (gridElement instanceof EndPositionGridElement) {
         return new Color(34, 139, 34);
      } else if (gridElement instanceof GridElement) {
         return Color.BLACK;
      }
      throw new IllegalStateException("Unknown GridElement '" + gridElement + "'!");
   }

   public static Color getTankColor(BelligerentParty belligerentParty) {
      if (isGalacticEmpire(belligerentParty)) {
         return new Color(105, 139, 34).darker().darker();
      }
      return Color.GRAY.darker().darker();
   }

   public static Color getTurretColor(BelligerentParty belligerentParty) {
      if (isGalacticEmpire(belligerentParty)) {
         return Color.BLACK;
      }
      return Color.RED.darker();
   }

   private static boolean isGalacticEmpire(BelligerentParty belligerentParty) {
      return belligerentParty.getBelligerentPartyType() == BelligerentPartyTypes.GALACTIC_EMPIRE;
   }

   /**
    * @return the {@link Color} which is used to render the path of a {@link Moveable}
    */
   public static Color getPositionListColor() {
      return Color.GREEN;
   }
}

package com.myownb3.piranha.ui.render.impl.weapon.turret;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.ui.image.ImageResource;
import com.myownb3.piranha.ui.image.constants.ImageConsts;

public class TurretGridElementPaintUtil {

   private TurretGridElementPaintUtil() {
      // private 
   }

   public static String getGunImageLocation(BelligerentParty belligerentParty) {
      if (belligerentParty == BelligerentPartyConst.GALACTIC_EMPIRE) {
         return ImageConsts.GUN_IMAGE_V2;
      }
      return ImageConsts.GUN_IMAGE;
   }

   public static ImageResource getGunCarriageImageRes(BelligerentParty belligerentParty) {
      if (belligerentParty == BelligerentPartyConst.GALACTIC_EMPIRE) {
         return ImageConsts.GUN_CARRIAGE_IMAGE_V2;
      }
      return ImageConsts.GUN_CARRIAGE_IMAGE;
   }
}

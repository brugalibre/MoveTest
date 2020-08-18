package com.myownb3.piranha.core.battle.weapon.countermeasure;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.AutoMoveable;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController;
import com.myownb3.piranha.core.moveables.types.AutoMoveableTypes;

public class DecoyFlareUtil {

   private DecoyFlareUtil() {
      // private
   }

   /**
    * @param gridElement
    *        the {@link GridElement} to check
    * @return <code>true</code> if the given {@link GridElement} is a decoy flare or <code>false</code> if not
    */
   public static boolean isDecoyFlareGridElement(GridElement gridElement) {
      return gridElement instanceof AutoMoveable && ((AutoMoveable) gridElement).getAutoMoveableTypes() == AutoMoveableTypes.DECOY_FLARE
            || gridElement instanceof AutoMoveableController
                  && ((AutoMoveableController) gridElement).getAutoMoveableTypes() == AutoMoveableTypes.DECOY_FLARE;
   }
}

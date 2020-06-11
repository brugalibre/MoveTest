package com.myownb3.piranha.core.statemachine.impl.handler;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;

public class OneTimeDetectableMoveableHelper extends DetectableMoveableHelper {
   private boolean hasAllreadyChecked;

   public OneTimeDetectableMoveableHelper(Detector detector) {
      super(detector);
      hasAllreadyChecked = false;
   }

   @Override
   public boolean check4Evasion(Grid grid, GridElement gridElement) {
      if (!hasAllreadyChecked) {
         hasAllreadyChecked = true;
         return true;
      }
      return false;
   }

   @Override
   public void checkSurrounding(Grid grid, GridElement gridElement) {}
}

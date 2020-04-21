package com.myownb3.piranha.statemachine.impl.handler;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;

public class OneTimeDetectableMoveableHelper extends DetectableMoveableHelper {
   private boolean hasAllreadyChecked;

   public OneTimeDetectableMoveableHelper(Detector detector) {
      super(detector);
      hasAllreadyChecked = false;
   }

   @Override
   public boolean check4Evasion(Grid grid, GridElement moveable) {
      if (!hasAllreadyChecked) {
         hasAllreadyChecked = true;
         return true;
      }
      return false;
   }

   @Override
   public void checkSurrounding(Grid grid, Moveable moveable) {}
}

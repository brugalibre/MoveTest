/**
 * 
 */
package com.myownb3.piranha.core.moveables.postaction.impl;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;

/**
 * An {@link DetectableMoveableHelper} implements the
 * {@link MoveablePostActionHandler} with basic functions of detecting other
 * {@link GridElement}. This includes the ability to identify weather or not a
 * certain {@link GridElement} is evaded
 * 
 * @author Dominic
 *
 */
public class DetectableMoveableHelper extends GridElementDetectorImpl implements MoveablePostActionHandler {

   public DetectableMoveableHelper(Detector detector) {
      super(detector);
   }

   @Override
   public void handlePostConditions(Grid grid, Moveable moveable) {
      checkSurrounding(grid, moveable);
   }
}

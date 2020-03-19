/**
 * 
 */
package com.myownb3.piranha.moveables.postaction.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.MoveablePostActionHandler;

/**
 * An {@link DetectableMoveableHelper} implements the
 * {@link MoveablePostActionHandler} with basic functions of detecting other
 * {@link GridElement}. This includes the ability to identify weather or not a
 * certain {@link GridElement} is evaded
 * 
 * @author Dominic
 *
 */
public class DetectableMoveableHelper implements MoveablePostActionHandler {

   protected Detector detector;

   public DetectableMoveableHelper(Detector detector) {
      super();
      this.detector = detector;
   }

   @Override
   public void handlePostConditions(Grid grid, Moveable moveable) {
      checkSurrounding(grid, moveable);
   }

   /**
    * Verifies if there is any {@link GridElement} on the {@link Grid} for which
    * there is currently an evasion with the given {@link GridElement}
    * 
    * @param grid
    *        the {@link Grid}
    * @param moveable
    *        the {@link GridElement} for which the evasion is checked
    * @return <code>true</code> if there is any evasion or <code>false</code> if
    *         not
    */
   public boolean check4Evasion(Grid grid, GridElement moveable) {
      return grid.getAllAvoidables(moveable).stream()
            .anyMatch(gridElement -> detector.isEvasion(gridElement));
   }

   /**
    * Checks the surrounding of the given {@link Moveable} on the given
    * {@link Grid}
    * 
    * @param grid
    *        the {@link Grid}
    * @param moveable
    *        the given {@link Moveable}
    */
   public void checkSurrounding(Grid grid, Moveable moveable) {
      grid.getAllAvoidables(moveable)
            .forEach(avoidable -> moveable.hasAvoidableDetected(avoidable, detector));
   }

   public List<Avoidable> getDetectedAvoidable(Grid grid, Moveable moveable) {
      return grid.getAllAvoidables(moveable)
            .stream()
            .filter(avoidable -> detector.hasObjectDetected(avoidable))
            .collect(Collectors.toList());
   }
}

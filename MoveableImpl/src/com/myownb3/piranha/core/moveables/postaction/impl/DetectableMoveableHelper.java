/**
 * 
 */
package com.myownb3.piranha.core.moveables.postaction.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.detector.Detector;
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
public class DetectableMoveableHelper implements MoveablePostActionHandler {

   private static final int DETECTABLE_RANGE_MARGIN = 2;
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
    * there is currently an evasion with the given {@link Moveable}
    * 
    * @param grid
    *        the {@link Grid}
    * @param moveable
    *        the {@link Moveable} for which the evasion is checked
    * @return <code>true</code> if there is any evasion or <code>false</code> if
    *         not
    */
   public boolean check4Evasion(Grid grid, Moveable moveable) {
      return grid.getAllAvoidableGridElementsWithinDistance(moveable, getDetectableRange())
            .stream()
            .anyMatch(avoidableGridElement -> detector.isEvasion(avoidableGridElement));
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
      grid.getAllAvoidableGridElementsWithinDistance(moveable, getDetectableRange())
            .forEach(gridElement -> moveable.hasGridElementDetected(gridElement, detector));
   }

   public List<GridElement> getDetectedGridElement(Grid grid, Moveable moveable) {
      return grid.getAllAvoidableGridElements(moveable)
            .stream()
            .filter(gridElement -> detector.hasObjectDetected(gridElement))
            .collect(Collectors.toList());
   }

   private int getDetectableRange() {
      return detector.getDetectorRange() + DETECTABLE_RANGE_MARGIN;
   }

}
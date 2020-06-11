/**
 * 
 */
package com.myownb3.piranha.core.detector;

import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * An {@link DetectableGridElementHelper} implements the
 * {@link GridElementPostActionHandler} with basic functions of detecting other
 * {@link GridElement}. This includes the ability to identify weather or not a
 * certain {@link GridElement} is evaded
 * 
 * @author Dominic
 *
 */
public class GridElementDetectorImpl implements GridElementDetector {

   private static final int DETECTABLE_RANGE_MARGIN = 2;
   private Grid grid;
   protected Detector detector;

   public GridElementDetectorImpl(Grid grid, Detector detector) {
      super();
      this.grid = grid;
      this.detector = detector;
   }

   @Override
   public boolean check4Evasion(GridElement gridElement) {
      return grid.getAllAvoidableGridElementsWithinDistance(gridElement, getDetectableRange())
            .stream()
            .anyMatch(avoidableGridElement -> detector.isEvasion(avoidableGridElement));
   }

   @Override
   public void checkSurrounding(GridElement detectableGridElement) {
      grid.getAllAvoidableGridElementsWithinDistance(detectableGridElement, getDetectableRange())
            .forEach(gridElement -> gridElement.isDetectedBy(detectableGridElement.getForemostPosition(), detector));
   }

   @Override
   public List<GridElement> getDetectedGridElement(GridElement detectableGridElement) {
      return grid.getAllAvoidableGridElements(detectableGridElement)
            .stream()
            .filter(gridElement -> detector.hasObjectDetected(gridElement))
            .collect(Collectors.toList());
   }

   private int getDetectableRange() {
      return detector.getDetectorRange() + DETECTABLE_RANGE_MARGIN;
   }

}

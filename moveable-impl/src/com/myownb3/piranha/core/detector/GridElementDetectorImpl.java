/**
 * 
 */
package com.myownb3.piranha.core.detector;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

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
   private Predicate<GridElement> checkSurroundingFilter;
   protected Detector detector;

   public GridElementDetectorImpl(Grid grid, Detector detector) {
      this(grid, detector, getDefaultFilter());
   }

   private GridElementDetectorImpl(Grid grid, Detector detector, Predicate<GridElement> checkSurroundingFilter) {
      super();
      this.grid = grid;
      this.detector = detector;
      this.checkSurroundingFilter = checkSurroundingFilter;
   }

   @Override
   public boolean check4Evasion(GridElement gridElement) {
      return grid.getAllAvoidableGridElementsWithinDistance(gridElement, getDetectableRange())
            .parallelStream()
            .anyMatch(avoidableGridElement -> detector.isEvasion(avoidableGridElement));
   }

   @Override
   public void checkSurrounding(GridElement detectableGridElement) {
      grid.getAllAvoidableGridElementsWithinDistance(detectableGridElement, getDetectableRange())
            .parallelStream()
            .filter(checkSurroundingFilter::test)
            .forEach(gridElement -> gridElement.isDetectedBy(detectableGridElement.getForemostPosition(), detector));
   }

   @Override
   public void checkSurroundingFromPosition(GridElement detectableGridElement, Position detectorPos) {
      grid.getAllAvoidableGridElementsWithinDistance(detectableGridElement, getDetectableRange())
            .parallelStream()
            .filter(checkSurroundingFilter::test)
            .forEach(gridElement -> gridElement.isDetectedBy(detectorPos, detector));
   }

   @Override
   public List<GridElement> getDetectedGridElements(GridElement detectableGridElement) {
      return grid.getAllGridElements(detectableGridElement).parallelStream()
            .filter(GridElement::isAvoidable)
            .filter(gridElement -> detector.hasObjectDetected(gridElement))
            .collect(Collectors.toList());
   }

   @Override
   public boolean isEvasion(GridElement gridElement2Check) {
      return detector.isEvasion(gridElement2Check);
   }

   private int getDetectableRange() {
      return detector.getDetectorRange() + DETECTABLE_RANGE_MARGIN;
   }

   private static Predicate<GridElement> getDefaultFilter() {
      return gridElement -> true;
   }

   public static class GridElementDetectorBuilder {
      private Grid grid;
      private Predicate<GridElement> checkSurroundingFilter;
      protected Detector detector;

      private GridElementDetectorBuilder() {
         this.checkSurroundingFilter = getDefaultFilter();
      }

      public GridElementDetectorBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public GridElementDetectorBuilder withDetector(Detector detector) {
         this.detector = detector;
         return this;
      }

      public GridElementDetectorBuilder withDetectingGridElementFilter(Predicate<GridElement> checkSurroundingFilter) {
         this.checkSurroundingFilter = checkSurroundingFilter;
         return this;
      }

      public static GridElementDetectorBuilder builder() {
         return new GridElementDetectorBuilder();
      }

      public GridElementDetectorImpl build() {
         requireNonNull(grid);
         requireNonNull(detector);
         return new GridElementDetectorImpl(grid, detector, checkSurroundingFilter);
      }
   }
}

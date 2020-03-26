/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;

/**
 * @author Dominic
 *
 */
public abstract class AbstractShape implements Shape {

   protected List<Position> path;
   protected GridElement gridElement;

   /**
    * Creates a new {@link AbstractShape}
    */
   public AbstractShape(List<Position> path) {
      this.path = path;
   }

   @Override
   public boolean detectObject(Position detectorPosition, Detector detector) {
      requireNonNull(gridElement, "A Shape needs a GridElement when calling 'detectObject'");
      for (Position posOnPath : path) {
         boolean hasPosDetected = detector.detectObject(gridElement, posOnPath, detectorPosition);
         if (hasPosDetected) {
            break;
         }
      }
      return false;
   }

   @Override
   public List<Position> getPath() {
      return Collections.unmodifiableList(path);
   }

   public void setGridElement(GridElement gridElement) {
      this.gridElement = requireNonNull(gridElement);
   }
}

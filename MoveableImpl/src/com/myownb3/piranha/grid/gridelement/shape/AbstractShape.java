/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Position;

/**
 * @author Dominic
 *
 */
public abstract class AbstractShape implements Shape {

   protected List<Position> path;

   /**
    * Creates a new {@link AbstractShape}
    */
   public AbstractShape(List<Position> path) {
      this.path = path;
   }

   @Override
   public boolean detectObject(Avoidable avoidable, Position detectorPosition, Detector detector) {
      for (Position posOnPath : path) {
         boolean hasPosDetected = detector.detectObject(avoidable, posOnPath, detectorPosition);
         if (hasPosDetected) {
            break;
         }
      }
      return false;
   }

   @Override
   public List<Position> getPath() {
      return path;
   }
}

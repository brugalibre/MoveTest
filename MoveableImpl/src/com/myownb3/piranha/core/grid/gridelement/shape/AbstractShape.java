/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.collision.CollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * @author Dominic
 *
 */
public abstract class AbstractShape implements Shape {

   protected List<Position> path;
   protected GridElement gridElement;
   protected CollisionDetector collisionDetector;

   /**
    * Creates a new {@link AbstractShape}
    */
   public AbstractShape(List<Position> path) {
      this.path = path;
      this.collisionDetector = buildCollisionDetector();
   }

   @Override
   public boolean detectObject(Position detectorPosition, Detector detector) {
      requireNonNull(gridElement, "A Shape needs a GridElement when calling 'detectObject'");
      List<Position> path4Detection = buildPath4Detection();
      detector.detectObjectAlongPath(gridElement, path4Detection, detectorPosition);
      return detector.hasObjectDetected(gridElement);
   }

   /**
    * Provides the {@link CollisionDetector} used by this {@link Shape}
    * 
    * @return the {@link CollisionDetector}
    */
   protected abstract CollisionDetector buildCollisionDetector();

   /**
    * Builds a List of {@link Position} which is used to verify if the {@link GridElement} of this {@link Shape}
    * has been detected
    * 
    * @return the path of this Shape
    */
   protected abstract List<Position> buildPath4Detection();

   @Override
   public boolean isWithinUpperBounds(List<Position> detectedPositions, Position detectorPos) {
      DetectionResult detectionResult = new DetectionResult();
      for (Position detectedPos : detectedPositions) {
         boolean evasionAngle = isPositionWithinUpperBounds(detectorPos, detectedPos);
         if (evasionAngle) {
            detectionResult.leftSideCounter++;
         } else {
            detectionResult.rightSideCounter++;
         }
      }
      if (areAllDetectedGridElementsLeft(detectionResult)) {
         return false;
      }
      if (areAllDetectedGridElementsRight(detectionResult)) {
         return true;
      }
      return areMoreDetectedGridElementsLeft(detectionResult) ? false : true;
   }

   private boolean areMoreDetectedGridElementsLeft(DetectionResult detectionResult) {
      return detectionResult.leftSideCounter > detectionResult.rightSideCounter;
   }

   private boolean areAllDetectedGridElementsRight(DetectionResult detectionResult) {
      return detectionResult.rightSideCounter > 0 && detectionResult.leftSideCounter == 0;
   }

   private boolean areAllDetectedGridElementsLeft(DetectionResult detectionResult) {
      return detectionResult.leftSideCounter > 0 && detectionResult.rightSideCounter == 0;
   }

   protected boolean isPositionWithinUpperBounds(Position detectorPos, Position detectedPos) {
      return detectedPos.calcAngleRelativeTo(detectorPos) >= 0;
   }

   @Override
   public List<Position> getPath() {
      return Collections.unmodifiableList(path);
   }

   public void setGridElement(GridElement gridElement) {
      this.gridElement = requireNonNull(gridElement);
   }

   private static class DetectionResult {
      private int leftSideCounter = 0;
      private int rightSideCounter = 0;

      private DetectionResult() {
         // private
      }
   }

}

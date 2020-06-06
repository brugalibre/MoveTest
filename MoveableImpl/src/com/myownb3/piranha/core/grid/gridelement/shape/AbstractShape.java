/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetector;
import com.myownb3.piranha.core.collision.detection.DefaultCollisionDetectorImpl.DefaultCollisionDetectorBuilder;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * @author Dominic
 *
 */
public abstract class AbstractShape implements Shape {

   protected List<PathSegment> path;
   protected GridElement gridElement;
   protected CollisionDetector collisionDetector;
   protected List<Position> path4Detection;
   protected Position center;

   /**
    * Creates a new {@link AbstractShape}
    */
   protected AbstractShape(List<PathSegment> path, Position center) {
      this.path = requireNonNull(path);
      this.center = requireNonNull(center);
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
   protected CollisionDetector buildCollisionDetector() {
      return DefaultCollisionDetectorBuilder.builder()
            .withShape(this)
            .build();
   }

   /**
    * Builds a List of {@link Position} which is used to verify if the {@link GridElement} of this {@link Shape}
    * has been detected
    * 
    * @return the path of this Shape
    */
   protected List<Position> buildPath4Detection() {
      return Collections.emptyList();// subclasses has to override it, CombinedShape does not need it
   }

   @Override
   public void transform(Position position) {
      this.center = position;
   }

   @Override
   public Position getCenter() {
      return center;
   }

   @Override
   public Shape clone() {
      try {
         return cloneShape();
      } catch (CloneNotSupportedException e) {
         throw new IllegalStateException(e);
      }
   }

   protected Shape cloneShape() throws CloneNotSupportedException {
      return (Shape) super.clone();
   }

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
         if (moreThanHalfeDone(detectedPositions, detectionResult)) {
            break;
         }
      }
      if (areMoreDetectedGridElementsLeft(detectionResult)) {
         return true;
      }
      return false;
   }

   private static boolean moreThanHalfeDone(List<Position> detectedPositions, DetectionResult detectionResult) {
      return detectionResult.rightSideCounter > detectedPositions.size() / 2
            || detectionResult.leftSideCounter > detectedPositions.size() / 2;
   }

   private boolean areMoreDetectedGridElementsLeft(DetectionResult detectionResult) {
      return detectionResult.leftSideCounter > detectionResult.rightSideCounter;
   }

   protected boolean isPositionWithinUpperBounds(Position detectorPos, Position detectedPos) {
      return detectorPos.calcAngleRelativeTo(detectedPos) >= 0.0;
   }

   @Override
   public List<PathSegment> getPath() {
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

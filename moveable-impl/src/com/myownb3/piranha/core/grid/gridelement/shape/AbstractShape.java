/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

   private static final long serialVersionUID = 7117577277575209230L;
   protected transient List<PathSegment> path;
   protected transient GridElement gridElement;
   protected transient CollisionDetector collisionDetector;
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
      List<Position> path4Detection = Collections.unmodifiableList(buildPath4Detection());
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
    * For precision reasons the 'path4Detection' may consist of more {@link Position} then it's normal path, which is
    * only used to describe the {@link Shape}s shape. This may have an inpact on performance, that's why each shape can
    * handle it's 'collision detection path' individually
    * 
    * @return the path of this Shape
    */
   protected List<Position> buildPath4Detection() {
      return path.stream()
            .map(PathSegment::getBegin)
            .collect(Collectors.toList());
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
      return areMoreDetectedGridElementsLeft(detectionResult);
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

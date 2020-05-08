/**
 * 
 */
package com.myownb3.piranha.core.detector;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.detector.detectionaware.DetectionAware;
import com.myownb3.piranha.core.detector.detectionaware.impl.DefaultDetectionAware;
import com.myownb3.piranha.core.detector.evasion.EvasionAngleEvaluator;
import com.myownb3.piranha.core.detector.evasion.impl.DefaultEvasionAngleEvaluatorImpl.DefaultEvasionAngleEvaluatorBuilder;
import com.myownb3.piranha.core.grid.gridelement.Avoidable;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class DetectorImpl implements IDetector {

   private int detectorReach;
   private double detectorAngle;
   private int evasionDistance;
   private double evasionAngle;
   private EvasionAngleEvaluator evasionAngleEvaluator;
   private DetectionAware detectionAware;

   private DetectorImpl(int detectorReach, int evasionDistance, double detectorAngle, double evasionAngle,
         EvasionAngleEvaluator evasionAngleEvaluator, DetectionAware detectionAware) {
      this.detectorReach = detectorReach;
      this.detectorAngle = detectorAngle;
      this.evasionAngle = evasionAngle;
      this.evasionDistance = evasionDistance;
      this.detectionAware = detectionAware;
      this.evasionAngleEvaluator = evasionAngleEvaluator;
      this.evasionAngleEvaluator.setDetectionAware(detectionAware);
   }

   @Override
   public void detectObjectAlongPath(GridElement gridElement, List<Position> gridElementPath, Position detectorPosition) {
      Collections.sort(gridElementPath, new Pos2DistanceComparator(detectorPosition));
      preDetecting(gridElement);
      List<DetectionResult> detectionResults = new ArrayList<>();
      for (Position gridElemPathPos : gridElementPath) {
         DetectionResult detectionResult = detectObjectInternal(gridElement, gridElemPathPos, detectorPosition);
         detectionResults.add(detectionResult);
      }
      postDetecting(gridElement, detectionResults);
   }

   @Override
   public void detectObject(GridElement gridElement, Position avoidablePos, Position detectorPosition) {
      preDetecting(gridElement);
      DetectionResult detectionResult = detectObjectInternal(gridElement, avoidablePos, detectorPosition);
      postDetecting(gridElement, Collections.singletonList(detectionResult));
   }

   private void preDetecting(GridElement gridElement) {
      detectionAware.clearGridElement(gridElement);
   }

   private void postDetecting(GridElement gridElement, List<DetectionResult> detectionResults) {
      detectionAware.checkGridElement4Detection(gridElement, detectionResults);
   }

   private DetectionResult detectObjectInternal(GridElement gridElement, Position avoidablePos, Position detectorPosition) {
      double distance = avoidablePos.calcDistanceTo(detectorPosition);
      if (detectorReach >= distance) {
         double degValue = MathUtil.calcAngleBetweenPositions(detectorPosition, avoidablePos);
         boolean isDetected = degValue <= (detectorAngle / 2);
         boolean isEvasion = isEvasion(gridElement, distance, degValue, isDetected);
         return new DetectionResult(isEvasion, isDetected, avoidablePos);
      }
      return new DetectionResult();
   }

   private boolean isEvasion(GridElement gridElement, double distance, double degValue, boolean isDetected) {
      boolean isEvasion = false;
      if (isDetected && evasionDistance >= distance) {
         boolean isPotentialCollisionCourse = degValue <= (evasionAngle / 2);
         isEvasion = isEvasion(gridElement, isDetected, isPotentialCollisionCourse);
      }
      return isEvasion;
   }

   private boolean isEvasion(GridElement gridElement, boolean isDetected, boolean isPotentialCollisionCourse) {
      return isDetected && isPotentialCollisionCourse && gridElement instanceof Avoidable;
   }

   @Override
   public boolean hasGridElementDetected(Position position) {
      return detectionAware.getNearestDetectedGridElement(position)
            .isPresent();
   }

   @Override
   public double getEvasionAngleRelative2(Position position) {
      return evasionAngleEvaluator.getEvasionAngleRelative2(position);
   }

   @Override
   public final boolean isEvasion(GridElement gridElement) {
      return detectionAware.isEvasion(gridElement);
   }

   @Override
   public boolean hasObjectDetected(GridElement gridElement) {
      return detectionAware.hasObjectDetected(gridElement);
   }

   @Override
   public int getDetectorRange() {
      return detectorReach;
   }

   @Override
   public Integer getEvasionDelayDistance() {
      return evasionDistance;
   }

   @Override
   public int getEvasionRange() {
      return evasionDistance;
   }

   @Override
   public double getDetectorAngle() {
      return detectorAngle;
   }

   @Override
   public double getEvasionAngle() {
      return evasionAngle;
   }

   public static class DetectorBuilder {

      private int detectorReach;
      private double detectorAngle;
      private Integer evasionDistance;
      private double evasionAngle;
      private EvasionAngleEvaluator evasionAngleEvaluator;
      private DetectionAware detectionAware;
      private double angleInc;

      private DetectorBuilder() {
         // private
      }

      public static DetectorBuilder builder() {
         return new DetectorBuilder();
      }

      public DetectorBuilder withDetectorReach(int detectorReach) {
         this.detectorReach = detectorReach;
         return this;
      }

      public DetectorBuilder withDetectorAngle(double detectorAngle) {
         this.detectorAngle = detectorAngle;
         return this;
      }

      public DetectorBuilder withEvasionDistance(int evasionDistance) {
         this.evasionDistance = evasionDistance;
         return this;
      }

      public DetectorBuilder withAngleInc(double angleInc) {
         this.angleInc = angleInc;
         return this;
      }

      public DetectorBuilder withEvasionAngle(double evasionAngle) {
         this.evasionAngle = evasionAngle;
         return this;
      }

      public DetectorBuilder withDefaultEvasionAngleEvaluator(EvasionAngleEvaluator evasionAngleEvaluator) {
         this.evasionAngleEvaluator = evasionAngleEvaluator;
         return this;
      }

      public DetectorBuilder withDetectionAware(DetectionAware detectionAware) {
         this.detectionAware = detectionAware;
         return this;
      }

      public DetectorImpl build() {
         setDefaultValuesIfMissing();
         return new DetectorImpl(detectorReach, evasionDistance, detectorAngle, evasionAngle, evasionAngleEvaluator, detectionAware);
      }

      /*
       * In order to support a minimum builder, we set a default value for those attributes 
       */
      private void setDefaultValuesIfMissing() {
         if (isNull(evasionDistance)) {
            evasionDistance = 2 * detectorReach / 3;
         }
         if (isNull(detectionAware)) {
            detectionAware = new DefaultDetectionAware();// 4 default builder
         }
         if (isNull(evasionAngleEvaluator)) {
            evasionAngleEvaluator = DefaultEvasionAngleEvaluatorBuilder.builder()
                  .withAngleInc(angleInc)
                  .build();// 4 default builder
         }
      }
   }
}

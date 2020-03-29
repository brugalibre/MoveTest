/**
 * 
 */
package com.myownb3.piranha.detector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class DetectorImpl implements Detector {

   private int detectorReach;
   private double detectorAngle;
   private double angleInc;
   private int evasionDistance;
   private double evasionAngle;

   private Map<GridElement, Boolean> detectionMap;
   private Map<GridElement, Boolean> isEvasionMap;

   /**
    * Default Constructor, only used for Tests
    */
   public DetectorImpl() {
      this(8, 45, 11.25);
   }

   public DetectorImpl(int detectorReach, int detectorAngle, int evasionAngle, double angleInc) {
      this.detectorReach = detectorReach;
      this.detectorAngle = detectorAngle;
      this.evasionAngle = evasionAngle;
      this.evasionDistance = 2 * detectorReach / 3;
      this.angleInc = angleInc;
      detectionMap = new HashMap<>();
      isEvasionMap = new HashMap<>();
   }

   public DetectorImpl(int detectorReach, int detectorAngle, double angleInc) {
      this(detectorReach, detectorAngle, detectorAngle, angleInc);
   }

   @Override
   public void detectObjectAlongPath(GridElement gridElement, List<Position> gridElementPath, Position detectorPosition) {
      preDetecting(gridElement);
      List<DetectionResult> detectionResults = new ArrayList<>();
      for (Position gridElemPathPos : gridElementPath) {
         DetectionResult detectionResult = detectObjectInternal(gridElement, gridElemPathPos, detectorPosition);
         detectionResults.add(detectionResult);
         if (detectionResult.isEvasion) {
            break;
         }
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
      detectionMap.remove(gridElement);
      isEvasionMap.remove(gridElement);
   }

   private void postDetecting(GridElement gridElement, List<DetectionResult> detectionResults) {
      detectionMap.put(gridElement, detectionResults.stream().anyMatch(DetectionResult::getIsDetected));
      isEvasionMap.put(gridElement, detectionResults.stream().anyMatch(DetectionResult::getIsEvasion));
   }

   private DetectionResult detectObjectInternal(GridElement gridElement, Position avoidablePos, Position detectorPosition) {
      double distance = avoidablePos.calcDistanceTo(detectorPosition);
      if (detectorReach >= distance) {
         double degValue = MathUtil.calcAngleBetweenPositions(detectorPosition, avoidablePos);
         boolean isDetected = degValue <= (detectorAngle / 2);
         boolean isEvasion = isEvasion(gridElement, distance, degValue, isDetected);
         return new DetectionResult(isEvasion, isDetected);
      }
      return new DetectionResult(false, false);
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
   public double getEvasionAngleRelative2(Position position) {

      double avoidAngle = 0;
      double ourAngle = position.getDirection().getAngle();

      Optional<Avoidable> evasionAvoidable = getNearestEvasionAvoidable(position);
      if (evasionAvoidable.isPresent()) {
         Avoidable avoidable = evasionAvoidable.get();
         Position gridElemPos = avoidable.getPosition();
         double avoidableAngle = gridElemPos.calcAbsolutAngle();

         boolean isInUpperBounds = isWithinUpperBorder(ourAngle, avoidableAngle, detectorAngle)
               && avoidableAngle >= ourAngle;

         if (isInUpperBounds) {
            avoidAngle = -angleInc;// Turn to the right
         } else {
            avoidAngle = angleInc;// Turn to the left
         }
      }
      return avoidAngle;
   }

   private Optional<Avoidable> getNearestEvasionAvoidable(Position position) {
      List<Avoidable> avoidables = getEvasionAvoidables();
      return getNearestEvasionAvoidable(position, avoidables);
   }

   @Visible4Testing
   Optional<Avoidable> getNearestEvasionAvoidable(Position position, List<Avoidable> avoidables) {
      Map<Avoidable, Double> avoidable2DistanceMap = fillupMap(position, avoidables);
      return avoidable2DistanceMap.keySet().stream().sorted(sort4Distance(avoidable2DistanceMap)).findFirst();
   }

   private Map<Avoidable, Double> fillupMap(Position position, List<Avoidable> avoidables) {
      Map<Avoidable, Double> avoidable2DistanceMap = new HashMap<>();
      for (Avoidable avoidable : avoidables) {
         Position gridElemPos = avoidable.getPosition();
         double distance = gridElemPos.calcDistanceTo(position);
         avoidable2DistanceMap.put(avoidable, Double.valueOf(distance));
      }
      return avoidable2DistanceMap;
   }

   private static Comparator<? super Avoidable> sort4Distance(Map<Avoidable, Double> avoidable2DistanceMap) {
      return (g1, g2) -> {
         Double distanceGridElem1ToPoint = avoidable2DistanceMap.get(g1);
         Double distanceGridElem2ToPoint = avoidable2DistanceMap.get(g2);
         return distanceGridElem1ToPoint.compareTo(distanceGridElem2ToPoint);
      };
   }

   private List<Avoidable> getEvasionAvoidables() {
      return isEvasionMap.keySet()
            .stream()
            .filter(Avoidable.class::isInstance)
            .map(Avoidable.class::cast)
            .filter(avoidable -> isEvasionMap.get(avoidable))
            .collect(Collectors.toList());
   }

   @Override
   public final boolean isEvasion(GridElement gridElement) {
      Boolean isEvasion = isEvasionMap.get(gridElement);
      return isEvasion == null ? false : isEvasion;
   }

   @Override
   public boolean hasObjectDetected(GridElement gridElement) {
      Boolean hasObjectDetected = detectionMap.get(gridElement);
      return hasObjectDetected == null ? false : hasObjectDetected;
   }

   @Override
   public int getDetectorRange() {
      return detectorReach;
   }

   private boolean isWithinUpperBorder(double ourAngle, double gridElementAngle, double detectorAngle) {
      return ourAngle + (detectorAngle / 2) >= gridElementAngle;
   }
}

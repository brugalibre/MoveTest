package com.myownb3.piranha.core.detector.detectionaware.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.detector.DetectionResult;
import com.myownb3.piranha.core.detector.detectionaware.DetectionAware;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;


public class DefaultDetectionAware implements DetectionAware {

   // contains information about weather or not a GridElement is detected or even is on collision course. Those values are not permanently, they are only until as the detector detects again
   private Map<GridElement, Boolean> transientDetectionMap;
   private Map<GridElement, Boolean> transientIsEvasionMap;
   private Map<GridElement, List<Position>> transientGridElemt2DetectedPosMap;

   public DefaultDetectionAware() {
      this.transientDetectionMap = new HashMap<>();
      this.transientIsEvasionMap = new HashMap<>();
      this.transientGridElemt2DetectedPosMap = new HashMap<>();
   }

   @Override
   public void clearGridElement(GridElement gridElement) {
      transientDetectionMap.remove(gridElement);
      transientIsEvasionMap.remove(gridElement);
      transientGridElemt2DetectedPosMap.remove(gridElement);
   }

   @Override
   public void checkGridElement4Detection(GridElement gridElement, List<DetectionResult> detectionResults) {
      boolean isDetected = detectionResults.stream().anyMatch(DetectionResult::getIsDetected);
      boolean isEvasion = detectionResults.stream().anyMatch(DetectionResult::getIsEvasion);
      transientDetectionMap.put(gridElement, isDetected);
      transientIsEvasionMap.put(gridElement, isEvasion);
      fillupDetectedPositions(gridElement, detectionResults);
   }

   private void fillupDetectedPositions(GridElement gridElement, List<DetectionResult> detectionResults) {
      transientGridElemt2DetectedPosMap.put(gridElement,
            detectionResults.stream()
                  .filter(DetectionResult::getIsDetected)
                  .map(DetectionResult::getDetectedPosition)
                  .collect(Collectors.toList()));
   }

   @Override
   public List<Position> getDetectedPositions4GridElement(GridElement gridElement) {
      if (transientGridElemt2DetectedPosMap.containsKey(gridElement)) {
         return transientGridElemt2DetectedPosMap.get(gridElement);
      }
      return Collections.emptyList();
   }

   @Override
   public Optional<GridElement> getNearestDetectedGridElement(Position position) {
      List<GridElement> detectedGridElements = getDetectedGridElemnt();
      return getNearestGridElement(position, detectedGridElements);
   }

   @Override
   public Optional<GridElement> getNearestEvasionGridElement(Position position) {
      List<GridElement> evasionGridElements = getEvasionGridElement();
      return getNearestGridElement(position, evasionGridElements);
   }

   private List<GridElement> getDetectedGridElemnt() {
      return transientDetectionMap.keySet()
            .stream()
            .filter(gridElement -> transientDetectionMap.get(gridElement))
            .collect(Collectors.toList());
   }

   private List<GridElement> getEvasionGridElement() {
      return transientIsEvasionMap.keySet()
            .stream()
            .filter(GridElement::isAvoidable)
            .filter(avoidable -> transientIsEvasionMap.get(avoidable))
            .collect(Collectors.toList());
   }

   @Visible4Testing
   <T extends GridElement> Optional<T> getNearestGridElement(Position position, List<T> gridElements) {
      Map<T, Double> gridElement2DistanceMap = fillupMap(position, gridElements);
      return gridElement2DistanceMap.keySet()
            .stream()
            .sorted(sort4Distance(gridElement2DistanceMap))
            .findFirst();
   }

   private <T extends GridElement> Map<T, Double> fillupMap(Position position, List<T> gridElements) {
      Map<T, Double> gridElement2DistanceMap = new HashMap<>();
      for (T gridElement : gridElements) {
         Position gridElemPos = gridElement.getPosition();
         double distance = gridElemPos.calcDistanceTo(position);
         gridElement2DistanceMap.put(gridElement, Double.valueOf(distance));
      }
      return gridElement2DistanceMap;
   }

   private static <T extends GridElement> Comparator<? super T> sort4Distance(Map<T, Double> gridElement2DistanceMap) {
      return (g1, g2) -> {
         Double distanceGridElem1ToPoint = gridElement2DistanceMap.get(g1);
         Double distanceGridElem2ToPoint = gridElement2DistanceMap.get(g2);
         return distanceGridElem1ToPoint.compareTo(distanceGridElem2ToPoint);
      };
   }

   @Override
   public final boolean isEvasion(GridElement gridElement) {
      Boolean isEvasion = transientIsEvasionMap.get(gridElement);
      return isEvasion != null && isEvasion.booleanValue();
   }

   @Override
   public boolean hasObjectDetected(GridElement gridElement) {
      Boolean hasObjectDetected = transientDetectionMap.get(gridElement);
      return hasObjectDetected != null && hasObjectDetected.booleanValue();
   }
}

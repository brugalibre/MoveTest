package com.myownb3.piranha.detector.detectionaware.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.detector.DetectionResult;
import com.myownb3.piranha.detector.detectionaware.DetectionAware;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;


public class DefaultDetectionAware implements DetectionAware {

   // contains information about weather or not a GridElement is detected or even is on collision course. Those values are not permanently, they are only until as the detector detects again
   private Map<GridElement, Boolean> transientDetectionMap;
   private Map<GridElement, Boolean> transientIsEvasionMap;

   public DefaultDetectionAware() {
      this.transientDetectionMap = new HashMap<>();
      this.transientIsEvasionMap = new HashMap<>();
   }

   @Override
   public void clearGridElement(GridElement gridElement) {
      transientDetectionMap.remove(gridElement);
      transientIsEvasionMap.remove(gridElement);
   }

   @Override
   public void checkGridElement4Detection(GridElement gridElement, List<DetectionResult> detectionResults) {
      boolean isDetected = detectionResults.stream().anyMatch(DetectionResult::getIsDetected);
      boolean isEvasion = detectionResults.stream().anyMatch(DetectionResult::getIsEvasion);
      transientDetectionMap.put(gridElement, isDetected);
      transientIsEvasionMap.put(gridElement, isEvasion);
   }

   @Override
   public Optional<Avoidable> getNearestEvasionAvoidable(Position position) {
      List<Avoidable> avoidables = getEvasionAvoidables();
      return getNearestEvasionAvoidable(position, avoidables);
   }

   private List<Avoidable> getEvasionAvoidables() {
      return transientIsEvasionMap.keySet()
            .stream()
            .filter(Avoidable.class::isInstance)
            .map(Avoidable.class::cast)
            .filter(avoidable -> transientIsEvasionMap.get(avoidable))
            .collect(Collectors.toList());
   }

   @Visible4Testing
   Optional<Avoidable> getNearestEvasionAvoidable(Position position, List<Avoidable> avoidables) {
      Map<Avoidable, Double> avoidable2DistanceMap = fillupMap(position, avoidables);
      return avoidable2DistanceMap.keySet()
            .stream()
            .sorted(sort4Distance(avoidable2DistanceMap))
            .findFirst();
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

   @Override
   public final boolean isEvasion(GridElement gridElement) {
      Boolean isEvasion = transientIsEvasionMap.get(gridElement);
      return isEvasion == null ? false : isEvasion;
   }

   @Override
   public boolean hasObjectDetected(GridElement gridElement) {
      Boolean hasObjectDetected = transientDetectionMap.get(gridElement);
      return hasObjectDetected == null ? false : hasObjectDetected;
   }
}

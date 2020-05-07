package com.myownb3.piranha.detector.lightbarrier;

import java.util.HashMap;
import java.util.Map;

import com.myownb3.piranha.detector.PlacedDetector;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;

public class LightBarrierImpl implements LightBarrier {

   private PlacedDetector placedDetector;
   private Map<GridElement, LightBarrierDetectingState> gridElementDetectionMap;
   private LightBarrierPassedCallbackHandler callbackHandler;

   private LightBarrierImpl(LightBarrierPassedCallbackHandler callbackHandler, PlacedDetector placedDetector) {
      this.gridElementDetectionMap = new HashMap<>();
      this.callbackHandler = callbackHandler;
      this.placedDetector = placedDetector;
   }

   @Override
   public void checkGridElement(GridElement gridElement) {
      LightBarrierDetectingState detectingState = getCurrentDetectingState(gridElement);
      switch (detectingState) {
         case NOT_YET_DETECTED:
            handleGridElementNotYetDetected(gridElement);
            break;
         case ENTERED:
            handleGridElementFrontDetected(gridElement);
            break;
         case LEAVING:
            handleFullyDetected(gridElement);
            break;
         default:
            break;
      }
   }

   private void handleFullyDetected(GridElement gridElement) {
      detectGridElement(gridElement, gridElement.getFurthermostBackPosition());
      if (!placedDetector.getDetector().hasObjectDetected(gridElement)) {
         callbackHandler.handleLightBarrierTriggered(gridElement);
      }
   }

   private LightBarrierDetectingState getCurrentDetectingState(GridElement gridElement) {
      if (!gridElementDetectionMap.containsKey(gridElement)) {
         return LightBarrierDetectingState.NOT_YET_DETECTED;
      }
      return gridElementDetectionMap.get(gridElement);

   }

   private void handleGridElementFrontDetected(GridElement gridElement) {
      detectGridElement(gridElement, gridElement.getFurthermostBackPosition());
      if (placedDetector.getDetector().hasObjectDetected(gridElement)) {
         gridElementDetectionMap.put(gridElement, LightBarrierDetectingState.LEAVING);
      }
   }

   private void detectGridElement(GridElement gridElement, Position position) {
      placedDetector.getDetector().detectObject(gridElement, position, placedDetector.getPosition());
   }

   private void handleGridElementNotYetDetected(GridElement gridElement) {
      detectGridElement(gridElement, gridElement.getFurthermostFrontPosition());
      if (placedDetector.getDetector().hasObjectDetected(gridElement)) {
         gridElementDetectionMap.put(gridElement, LightBarrierDetectingState.ENTERED);
      }
   }

   public static class LightBarrierBuilder {
      private PlacedDetector placedDetector;
      private LightBarrierPassedCallbackHandler callbackHandler;

      private LightBarrierBuilder() {
         // private
      }

      public LightBarrierBuilder withPlacedDetector(PlacedDetector placedDetector) {
         this.placedDetector = placedDetector;
         return this;
      }

      public LightBarrierBuilder withLightBarrierCallbackHandler(LightBarrierPassedCallbackHandler callbackHandler) {
         this.callbackHandler = callbackHandler;
         return this;
      }

      public LightBarrierImpl build() {
         return new LightBarrierImpl(callbackHandler, placedDetector);
      }

      public static LightBarrierBuilder builder() {
         return new LightBarrierBuilder();
      }
   }
}

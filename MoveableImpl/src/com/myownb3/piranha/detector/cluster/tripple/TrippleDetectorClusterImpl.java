package com.myownb3.piranha.detector.cluster.tripple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.detector.IDetector;
import com.myownb3.piranha.detector.cluster.DetectingStrategyHandler;
import com.myownb3.piranha.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.detector.strategy.DetectingStrategyHandlerFactory;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;

public class TrippleDetectorClusterImpl implements TrippleDetectorCluster {

   private DetectingStrategyHandler detectingStrategyHandler;
   private IDetectorInfo centerDetector;
   private IDetectorInfo rightSideDetector;
   private IDetectorInfo leftSideDetector;

   private TrippleDetectorClusterImpl(DetectingStrategyHandler detectingStrategyHandler) {
      this.detectingStrategyHandler = detectingStrategyHandler;
   }

   @Override
   public void init() {
      detectingStrategyHandler.init();
   }

   @Override
   public void detectObject(GridElement gridElement, Position gridElementPos, Position zeroDetectorPosition) {
      detectObjectAlongPath(gridElement, Collections.singletonList(gridElementPos), zeroDetectorPosition);
   }

   @Override
   public void detectObjectAlongPath(GridElement gridElement, List<Position> gridElementPath, Position zeroDetectorPosition) {
      detectingStrategyHandler.detectObjectAlongPath(gridElement, gridElementPath, zeroDetectorPosition);
   }

   @Override
   public double getEvasionAngleRelative2(Position position) {
      return detectingStrategyHandler.getEvasionAngleRelative2(position);
   }

   @Override
   public Integer getEvasionDelayDistance() {
      return detectingStrategyHandler.getEvasionDistance4DetectingDetector();
   }

   @Override
   public boolean isEvasion(GridElement gridElement) {
      return getDetectors().stream()
            .anyMatch(detector -> detector.isEvasion(gridElement));
   }

   @Override
   public boolean hasObjectDetected(GridElement gridElement) {
      return getDetectors().stream()
            .anyMatch(detector -> detector.hasObjectDetected(gridElement));
   }

   @Override
   public int getDetectorRange() {
      return getDetectors().stream()
            .map(Detector::getDetectorRange)
            .mapToInt(range -> range)
            .max()
            .orElse(0);
   }

   @Override
   public int getEvasionRange() {
      return getDetectors().stream()
            .map(Detector::getEvasionRange)
            .mapToInt(range -> range)
            .max()
            .orElse(0);
   }


   @Override
   public IDetectorInfo getCenterDetector() {
      return centerDetector;
   }

   @Override
   public IDetectorInfo getLeftSideDetector() {
      return leftSideDetector;
   }

   @Override
   public IDetectorInfo getRightSideDetector() {
      return rightSideDetector;
   }

   private List<Detector> getDetectors() {
      return Arrays.asList(leftSideDetector.getDetector(),
            rightSideDetector.getDetector(),
            centerDetector.getDetector());
   }

   public static class TrippleDetectorClusterBuilder {

      private TrippleDetectorClusterImpl detectorCluster;
      private IDetectorInfoImpl centerDetector;
      private IDetectorInfoImpl leftSideDetector;
      private IDetectorInfoImpl rightSideDetector;
      private DetectingStrategyHandler detectingStrategyHandler;
      private DetectingStrategy detectingStrategy;

      private TrippleDetectorClusterBuilder() {
         // private  
      }

      public static TrippleDetectorClusterBuilder builder() {
         return new TrippleDetectorClusterBuilder();
      }

      public TrippleDetectorClusterBuilder withCenterDetector(IDetector centerDetector) {
         this.centerDetector = IDetectorInfoImpl.of(centerDetector, 0);
         return this;
      }

      public TrippleDetectorClusterBuilder withLeftSideDetector(IDetector rightSideDetector, double leftSideDetectorOffset) {
         this.rightSideDetector = IDetectorInfoImpl.of(rightSideDetector, leftSideDetectorOffset);
         return this;
      }

      public TrippleDetectorClusterBuilder withRightSideDetector(IDetector leftSideDetector, double rightSideDetectorOffset) {
         this.leftSideDetector = IDetectorInfoImpl.of(leftSideDetector, rightSideDetectorOffset);
         return this;
      }

      public TrippleDetectorClusterBuilder withDetectionStrategyHandler(DetectingStrategyHandler detectingStrategyHandler) {
         this.detectingStrategyHandler = detectingStrategyHandler;
         return this;
      }

      public TrippleDetectorClusterBuilder withStrategy(DetectingStrategy detectingStrategy) {
         this.detectingStrategy = detectingStrategy;
         return this;
      }

      public TrippleDetectorClusterBuilder withAutoDetectionStrategyHandler() {
         this.detectingStrategyHandler = buildDetectingStrategyHandler();
         return this;
      }

      public TrippleDetectorCluster build() {
         detectorCluster = new TrippleDetectorClusterImpl(detectingStrategyHandler);
         detectorCluster.centerDetector = centerDetector;
         detectorCluster.leftSideDetector = leftSideDetector;
         detectorCluster.rightSideDetector = rightSideDetector;
         return detectorCluster;
      }

      private SupportiveFlanksDetectingStrategyHandler buildDetectingStrategyHandler() {
         SupportiveFlanksDetectingStrategyHandler handler = DetectingStrategyHandlerFactory.getHandler(detectingStrategy);
         handler.setCenterDetector(centerDetector);
         handler.setRightSideDetector(rightSideDetector);
         handler.setLeftSideDetector(leftSideDetector);
         return handler;
      }

      /**
       * The default {@link TrippleDetectorCluster} contains three {@link IDetector}:
       * - one placed in the middle
       * - two placed on the outside
       * 
       * @param centerDetectorConfig
       *        the {@link EvasionStateMachineConfig} for the center {@link Detector}
       * @param sideDetectorConfig
       *        the {@link EvasionStateMachineConfig} for the two side {@link Detector}s
       * @return a {@link TrippleDetectorCluster}
       */
      public static TrippleDetectorCluster buildDefaultDetectorCluster(EvasionStateMachineConfig centerDetectorConfig,
            EvasionStateMachineConfig sideDetectorConfig) {
         IDetector mainDetector = new DetectorImpl(centerDetectorConfig.getDetectorReach(), centerDetectorConfig.getEvasionDistance(),
               centerDetectorConfig.getDetectorAngle(), centerDetectorConfig.getEvasionAngle(), centerDetectorConfig.getEvasionAngleInc());
         IDetector rightSideDetector = new DetectorImpl(sideDetectorConfig.getDetectorReach(), sideDetectorConfig.getEvasionDistance(),
               sideDetectorConfig.getDetectorAngle(), sideDetectorConfig.getDetectorAngle(), sideDetectorConfig.getEvasionAngleInc());
         IDetector leftSideDetector = new DetectorImpl(sideDetectorConfig.getDetectorReach(), sideDetectorConfig.getEvasionDistance(),
               sideDetectorConfig.getDetectorAngle(), sideDetectorConfig.getDetectorAngle(), sideDetectorConfig.getEvasionAngleInc());

         double mainDetectorAngle = mainDetector.getDetectorAngle();
         double rightSideDetectorOffset = -(mainDetectorAngle / 2) - (rightSideDetector.getDetectorAngle() / 2);
         double leftSideDetectorOffset = (mainDetectorAngle / 2) + (leftSideDetector.getDetectorAngle() / 2);

         return TrippleDetectorClusterBuilder.builder()
               .withCenterDetector(mainDetector)
               .withRightSideDetector(rightSideDetector, rightSideDetectorOffset)
               .withLeftSideDetector(leftSideDetector, leftSideDetectorOffset)
               .withStrategy(DetectingStrategy.SUPPORTIVE_FLANKS)
               .withAutoDetectionStrategyHandler()
               .build();
      }
   }
}

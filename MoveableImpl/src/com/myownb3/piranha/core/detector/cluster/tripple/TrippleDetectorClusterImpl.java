package com.myownb3.piranha.core.detector.cluster.tripple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.cluster.DetectingStrategyHandler;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.detectionaware.impl.DefaultDetectionAware;
import com.myownb3.piranha.core.detector.evasion.impl.DefaultEvasionAngleEvaluatorImpl.DefaultEvasionAngleEvaluatorBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategyHandlerFactory;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;

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
      public static TrippleDetectorCluster buildDefaultDetectorCluster(DetectorConfig centerDetectorConfig,
            DetectorConfig sideDetectorConfig) {
         double mainDetectorAngle = centerDetectorConfig.getDetectorAngle();
         double rightSideDetectorOffset = -(mainDetectorAngle / 2) - (sideDetectorConfig.getDetectorAngle() / 2);
         double leftSideDetectorOffset = (mainDetectorAngle / 2) + (sideDetectorConfig.getDetectorAngle() / 2);

         double sideDetectorDetectorAngle = (180 - mainDetectorAngle) / 2;
         double sideDetectorEvasionAngle = Math.min(sideDetectorDetectorAngle, sideDetectorConfig.getEvasionAngle());
         return TrippleDetectorClusterBuilder.builder()
               .withCenterDetector(DetectorBuilder.builder()
                     .withDetectorReach(centerDetectorConfig.getDetectorReach())
                     .withDetectorAngle(centerDetectorConfig.getDetectorAngle())
                     .withEvasionDistance(centerDetectorConfig.getEvasionDistance())
                     .withEvasionAngle(centerDetectorConfig.getEvasionAngle())
                     .withDetectionAware(new DefaultDetectionAware())
                     .withDefaultEvasionAngleEvaluator(DefaultEvasionAngleEvaluatorBuilder.builder()
                           .withAngleInc(centerDetectorConfig.getEvasionAngleInc())
                           .build())
                     .build())
               .withRightSideDetector(DetectorBuilder.builder()
                     .withDetectorReach(sideDetectorConfig.getDetectorReach())
                     .withDetectorAngle(sideDetectorDetectorAngle)
                     .withEvasionDistance(sideDetectorConfig.getEvasionDistance())
                     .withEvasionAngle(sideDetectorEvasionAngle)
                     .withDetectionAware(new DefaultDetectionAware())
                     .withDefaultEvasionAngleEvaluator(DefaultEvasionAngleEvaluatorBuilder.builder()
                           .withAngleInc(sideDetectorConfig.getEvasionAngleInc())
                           .build())
                     .build(), rightSideDetectorOffset)
               .withLeftSideDetector(DetectorBuilder.builder()
                     .withDetectorReach(sideDetectorConfig.getDetectorReach())
                     .withDetectorAngle(sideDetectorDetectorAngle)
                     .withEvasionDistance(sideDetectorConfig.getEvasionDistance())
                     .withEvasionAngle(sideDetectorEvasionAngle)
                     .withDetectionAware(new DefaultDetectionAware())
                     .withDefaultEvasionAngleEvaluator(DefaultEvasionAngleEvaluatorBuilder.builder()
                           .withAngleInc(sideDetectorConfig.getEvasionAngleInc())
                           .build())
                     .build(), leftSideDetectorOffset)
               .withStrategy(DetectingStrategy.SUPPORTIVE_FLANKS_WITH_DETECTION)
               .withAutoDetectionStrategyHandler()
               .build();
      }
   }
}

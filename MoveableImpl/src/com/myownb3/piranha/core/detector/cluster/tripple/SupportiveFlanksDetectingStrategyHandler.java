package com.myownb3.piranha.core.detector.cluster.tripple;

import static java.lang.Math.abs;

import java.util.List;
import java.util.Optional;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.cluster.DetectingStrategyHandler;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link SupportiveFlanksDetectingStrategyHandler} handles a specific {@link DetectingStrategy}
 * 
 * @author Dominic
 *
 */
public class SupportiveFlanksDetectingStrategyHandler implements DetectingStrategyHandler {
   protected IDetectorInfo centerDetector;
   protected IDetectorInfo rightSideDetector;
   protected IDetectorInfo leftSideDetector;
   protected Optional<IDetector> evasionDetectedDetectorOpt;

   public SupportiveFlanksDetectingStrategyHandler() {
      init();
   }

   private void init() {
      evasionDetectedDetectorOpt = Optional.empty();
   }

   @Override
   public void detectObjectAlongPath(GridElement gridElement, List<Position> gridElementPath, Position zeroDetectorPosition) {
      boolean hasObjectDetected = detectObject(leftSideDetector, gridElement, zeroDetectorPosition, gridElementPath);
      if (!hasObjectDetected) {
         hasObjectDetected = detectObject(rightSideDetector, gridElement, zeroDetectorPosition, gridElementPath);
         if (!hasObjectDetected) {
            detectObject(centerDetector, gridElement, zeroDetectorPosition, gridElementPath);
         }
      }
      setCurrentEvasionDetectingDetector(gridElement);
   }

   @Visible4Testing
   void setCurrentEvasionDetectingDetector(GridElement gridElement) {
      if (leftSideDetector.getDetector().isEvasion(gridElement)) {
         evasionDetectedDetectorOpt = Optional.of(leftSideDetector.getDetector());
      } else if (rightSideDetector.getDetector().isEvasion(gridElement)) {
         evasionDetectedDetectorOpt = Optional.of(rightSideDetector.getDetector());
      } else if (centerDetector.getDetector().isEvasion(gridElement)) {
         evasionDetectedDetectorOpt = Optional.of(centerDetector.getDetector());
      }
   }

   /*
    * Returns true if we do not need to continue detecting with the other detectors
    */
   private boolean detectObject(IDetectorInfo detectorInfo, GridElement gridElement, Position zeroDetectorPosition,
         List<Position> gridElementPath) {
      IDetector detector = detectorInfo.getDetector();
      Position detectorPosition = zeroDetectorPosition.rotate(detectorInfo.getOffsetAngle());
      detector.detectObjectAlongPath(gridElement, gridElementPath, detectorPosition);
      return detector.isEvasion(gridElement);
   }

   @Override
   public double getEvasionAngleRelative2(Position position) {
      return evasionDetectedDetectorOpt.map(detector -> {
         double evaluatedEvasionAngle = detector.getEvasionAngleRelative2(position);
         // The side detectors need a special treatment: If the left detector is evasion, we only can turn right and vice versa for the right detector
         if (isLeftSideDetector(detector)) {
            return abs(evaluatedEvasionAngle);
         } else if (isRightSideDetector(detector)) {
            return -abs(evaluatedEvasionAngle);
         }
         return evaluatedEvasionAngle;
      }).orElse(0.0);
   }

   private boolean isLeftSideDetector(IDetector detector) {
      return detector == leftSideDetector.getDetector();
   }

   private boolean isRightSideDetector(IDetector detector) {
      return detector == rightSideDetector.getDetector();
   }

   @Override
   public Integer getEvasionDistance4DetectingDetector() {
      return evasionDetectedDetectorOpt.map(IDetector::getEvasionDelayDistance)
            .orElse(null);
   }

   public final void setCenterDetector(IDetectorInfo centerDetector) {
      this.centerDetector = centerDetector;
   }

   public final void setRightSideDetector(IDetectorInfo rightSideDetector) {
      this.rightSideDetector = rightSideDetector;
   }

   public final void setLeftSideDetector(IDetectorInfo leftSideDetector) {
      this.leftSideDetector = leftSideDetector;
   }

}

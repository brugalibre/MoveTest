package com.myownb3.piranha.core.battle.weapon.turret.turretscanner;

import static com.myownb3.piranha.core.battle.weapon.target.TargetGridElement.isSameGridElementTarget;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.myownb3.piranha.core.battle.weapon.target.TargetGridElement;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluator;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.battle.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

public class TurretScanner {
   private Turret turret;
   private TargetPositionLeadEvaluator leadEvaluator;
   private TargetGridElementEvaluator targetGridElementEvaluator;
   private Optional<TargetGridElement> nearestDetectedTargetGridElementOpt;

   private TurretScanner(Turret turret, IDetector detector, TargetPositionLeadEvaluator targetPositionLeadEvaluator,
         GridElementEvaluator gridElementEvaluator) {
      this.targetGridElementEvaluator = TargetGridElementEvaluatorBuilder.builder()
            .withBelligerentParty(turret.getBelligerentParty())
            .withDetector(detector)
            .withGridElementEvaluator(gridElementEvaluator)
            .build();
      this.turret = requireNonNull(turret);
      this.leadEvaluator = targetPositionLeadEvaluator;
      resetNearestDetectedTargetGridElement();
   }

   public TurretState scan(TurretState currentState) {
      boolean targetAcquired;
      switch (currentState) {
         case RETURNING:
            //fall through
         case SCANNING:
            return evaluateNearestTargetGridElement(currentState);
         case TARGET_DETECTED:
            targetAcquired = evaluatePosition2Acquire4DetectedTargetGridElement();
            return targetAcquired ? TurretState.ACQUIRING : TurretState.SCANNING;
         case ACQUIRING:
            // Nothing to do, the turret is turning arround
            return currentState;
         case SHOOTING:
            targetAcquired = evaluatePosition2Acquire4DetectedTargetGridElement();
            return targetAcquired ? TurretState.SHOOTING : TurretState.SCANNING;
         default:
            throw new IllegalStateException("Unknown State '" + currentState + "'");
      }
   }

   private TurretState evaluateNearestTargetGridElement(TurretState currentState) {
      this.nearestDetectedTargetGridElementOpt = getNearestTargetGridElement();
      return nearestDetectedTargetGridElementOpt.map(nearestDetectedTargetPos -> TurretState.TARGET_DETECTED)
            .orElse(currentState);
   }

   private boolean evaluatePosition2Acquire4DetectedTargetGridElement() {
      Optional<TargetGridElement> currentTargetGridElementAvailableOpt = getNearestTargetGridElement();
      if (isSameGridElementTarget(nearestDetectedTargetGridElementOpt, currentTargetGridElementAvailableOpt)) {
         TargetGridElement currentDetectedTargetGridElement = currentTargetGridElementAvailableOpt.get();
         TargetGridElement prevDetectedTargetGridElement = nearestDetectedTargetGridElementOpt.get();
         currentDetectedTargetGridElement.setPrevAcquiredPos(prevDetectedTargetGridElement.getCurrentGridElementPosition());
         Position targetPos2Acquire = evaluateTargetPositionFromIdentifiedGridElement(currentTargetGridElementAvailableOpt.get());
         updateNearestDetectedTargetGridElement(currentDetectedTargetGridElement, targetPos2Acquire);
         return true;
      }
      resetNearestDetectedTargetGridElement();
      return false;
   }

   private Position evaluateTargetPositionFromIdentifiedGridElement(TargetGridElement nearestTargetGridElement) {
      Position turretPos = turret.getShape().getForemostPosition();
      return leadEvaluator.calculateTargetConsideringLead(nearestTargetGridElement, Positions.of(turretPos));
   }

   private void updateNearestDetectedTargetGridElement(TargetGridElement currentNearestTargetGridElement, Position targetPos2Acquire) {
      currentNearestTargetGridElement.setTargetPosWithLead2Acquire(targetPos2Acquire);
      nearestDetectedTargetGridElementOpt = Optional.of(currentNearestTargetGridElement);
   }

   private void resetNearestDetectedTargetGridElement() {
      this.nearestDetectedTargetGridElementOpt = Optional.empty();
   }

   /**
    * @return the nearest acquired or still acquiring {@link Position}
    */
   public Optional<Position> getNearestDetectedTargetPos() {
      return nearestDetectedTargetGridElementOpt
            .map(TargetGridElement::getTargetPosWithLead2Acquire)
            .flatMap(Optional::ofNullable);
   }

   private Optional<TargetGridElement> getNearestTargetGridElement() {
      Position detectorPos = turret.getShape().getForemostPosition();
      return targetGridElementEvaluator.getNearestTargetGridElement(detectorPos);
   }

   public static final class TurretScannerBuilder {
      private Turret turret;
      private IDetector detector;
      private TargetPositionLeadEvaluator leadEvaluator;
      private GridElementEvaluator gridElementEvaluator;

      private TurretScannerBuilder() {
         // private
      }

      public TurretScannerBuilder withTurret(Turret turret) {
         this.turret = turret;
         return this;
      }

      public TurretScannerBuilder withGridElementEvaluator(GridElementEvaluator gridElementEvaluator) {
         this.gridElementEvaluator = gridElementEvaluator;
         return this;
      }

      public TurretScannerBuilder withDetector(IDetector detector) {
         this.detector = detector;
         return this;
      }

      public TurretScannerBuilder withTargetPositionLeadEvaluator(TargetPositionLeadEvaluator leadEvaluator) {
         this.leadEvaluator = leadEvaluator;
         return this;
      }

      public TurretScanner build() {
         return new TurretScanner(turret, detector, leadEvaluator, gridElementEvaluator);
      }

      public static TurretScannerBuilder builder() {
         return new TurretScannerBuilder();
      }
   }
}

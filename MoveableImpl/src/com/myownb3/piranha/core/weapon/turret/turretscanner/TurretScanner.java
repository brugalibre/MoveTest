package com.myownb3.piranha.core.weapon.turret.turretscanner;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

public class TurretScanner {
   private Turret turret;
   private IDetector detector;
   private TargetPositionLeadEvaluator leadEvaluator;
   private GridElementEvaluator gridElementEvaluator;
   private Optional<TargetGridElement> nearestDetectedTargetGridElementOpt;

   private TurretScanner(Turret turret, IDetector detector, TargetPositionLeadEvaluator targetPositionLeadEvaluator,
         GridElementEvaluator gridElementEvaluator) {
      this.detector = requireNonNull(detector);
      this.gridElementEvaluator = requireNonNull(gridElementEvaluator);
      this.turret = requireNonNull(turret);
      resetNearestDetectedTargetGridElement();
      this.leadEvaluator = targetPositionLeadEvaluator;
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
      Optional<TargetGridElement> currentTargetGridElementAvailable = getNearestTargetGridElement();
      if (isStillSameTargetDetected(currentTargetGridElementAvailable)) {
         TargetGridElement currentDetectedTargetGridElement = currentTargetGridElementAvailable.get();
         TargetGridElement prevDetectedTargetGridElement = nearestDetectedTargetGridElementOpt.get();
         currentDetectedTargetGridElement.setPrevAcquiredPos(prevDetectedTargetGridElement.getCurrentGridElementPosition());
         Position targetPos2Acquire = evaluateTargetPositionFromIdentifiedGridElement(currentTargetGridElementAvailable.get());
         updateNearestDetectedTargetGridElement(currentDetectedTargetGridElement, targetPos2Acquire);
         return true;
      }
      resetNearestDetectedTargetGridElement();
      return false;
   }

   private boolean isStillSameTargetDetected(Optional<TargetGridElement> currentTargetGridElementAvailable) {
      return currentTargetGridElementAvailable
            .map(TargetGridElement::getGridElement)
            .map(isStillSameGridElementDetected())
            .orElse(false);
   }

   private Function<? super GridElement, Boolean> isStillSameGridElementDetected() {
      return currentEvaluatedTargetGridElement -> currentEvaluatedTargetGridElement == nearestDetectedTargetGridElementOpt.get()
            .getGridElement();
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

   private Predicate<? super GridElement> isGridElementDetected() {
      Position detectorPos = turret.getShape().getForemostPosition();
      return gridElement -> gridElement.isDetectedBy(detectorPos, detector);
   }

   private Optional<TargetGridElement> getNearestTargetGridElement() {
      Position detectorPos = turret.getShape().getForemostPosition();
      Predicate<GridElement> isProjectile = Projectile.class::isInstance;
      return getAllPotentialTargetsWithinReach(detectorPos).stream()
            .filter(isProjectile.negate())
            .filter(isEnemy())
            .filter(isGridElementDetected())
            .sorted(new GridElement2DistanceComparator(detectorPos))
            .map(TargetGridElement::of)
            .findFirst();
   }

   private Predicate<? super GridElement> isEnemy() {
      return gridElement -> isGridElementEnemy(gridElement);
   }

   @Visible4Testing
   boolean isGridElementEnemy(GridElement gridElement) {
      return (gridElement instanceof Belligerent) ? turret.isEnemy((Belligerent) gridElement) : false;
   }

   private List<GridElement> getAllPotentialTargetsWithinReach(Position detectorPos) {
      return gridElementEvaluator.evaluateGridElementsWithinDistance(detectorPos, detector.getDetectorRange());
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

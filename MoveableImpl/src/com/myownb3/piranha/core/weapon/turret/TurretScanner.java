package com.myownb3.piranha.core.weapon.turret;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.PlacedDetector;
import com.myownb3.piranha.core.detector.Pos2DistanceComparator;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

public class TurretScanner {
   private Turret turret;
   private PlacedDetector placedDetector;
   private TargetPositionLeadEvaluator leadEvaluator;
   private GridElementEvaluator gridElementEvaluator;
   private Optional<Position> nearestDetectedTargetPositionOpt;

   private TurretScanner(Turret turret, PlacedDetector placedDetector, TargetPositionLeadEvaluator targetPositionLeadEvaluator,
         GridElementEvaluator gridElementEvaluator) {
      this.placedDetector = requireNonNull(placedDetector);
      this.gridElementEvaluator = requireNonNull(gridElementEvaluator);
      this.turret = requireNonNull(turret);
      this.nearestDetectedTargetPositionOpt = Optional.empty();
      this.leadEvaluator = targetPositionLeadEvaluator;
   }

   public TurretState scan(TurretState currentState) {
      switch (currentState) {
         case SCANNING:
            return getNearestTargetPosAndEvalNextState(currentState);
         case ACQUIRING:
            // Nothing to do
            return currentState;
         case SHOOTING:
            return getNearestTargetPosAndEvalNextState(currentState);
         default:
            return currentState;
      }
   }

   private TurretState getNearestTargetPosAndEvalNextState(TurretState currentState) {
      this.nearestDetectedTargetPositionOpt = getNearestTargetPos();
      return nearestDetectedTargetPositionOpt.map(nearestDetectedTargetPos -> TurretState.ACQUIRING)
            .orElse(currentState);
   }

   /**
    * 
    * @return the nearest acquired or still acquiring {@link Position}
    */
   public Optional<Position> getNearestDetectedTargetPos() {
      return nearestDetectedTargetPositionOpt;
   }

   private Predicate<? super GridElement> isGridElementDetected() {
      IDetector detector = placedDetector.getDetector();
      return gridElement -> gridElement.isDetectedBy(placedDetector.getPosition(), detector);
   }

   private Optional<Position> getNearestTargetPos() {
      return getAllPotentialTargetsWithinReach().stream()
            .filter(GridElement::isAimable)
            .filter(isGridElementDetected())
            .map(GridElement::getPosition)
            .sorted(new Pos2DistanceComparator(placedDetector.getPosition()))
            .map(acquiredPos -> leadEvaluator.calculateTargetConsideringLead(acquiredPos, turret.getForemostPosition()))
            .findFirst();
   }

   private List<GridElement> getAllPotentialTargetsWithinReach() {
      IDetector detector = placedDetector.getDetector();
      return gridElementEvaluator.evaluateGridElementsWithinDistance(turret.getForemostPosition(), detector.getDetectorRange());
   }

   public static final class TurretScannerBuilder {
      private Turret turret;
      private PlacedDetector placedDetector;
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

      public TurretScannerBuilder withPlacedDetector(PlacedDetector placedDetector) {
         this.placedDetector = placedDetector;
         return this;
      }

      public TurretScannerBuilder withTargetPositionLeadEvaluator(TargetPositionLeadEvaluator leadEvaluator) {
         this.leadEvaluator = leadEvaluator;
         return this;
      }

      public TurretScanner build() {
         return new TurretScanner(turret, placedDetector, leadEvaluator, gridElementEvaluator);
      }

      public static TurretScannerBuilder builder() {
         return new TurretScannerBuilder();
      }
   }
}

package com.myownb3.piranha.core.weapon.target;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.turret.turretscanner.GridElement2DistanceComparator;

public class TargetGridElementEvaluatorImpl implements TargetGridElementEvaluator {

   private GridElementEvaluator gridElementEvaluator;
   private BelligerentParty belligerentParty;
   private IDetector detector;

   private TargetGridElementEvaluatorImpl(BelligerentParty belligerentParty, IDetector detector, GridElementEvaluator gridElementEvaluator) {
      this.belligerentParty = requireNonNull(belligerentParty);
      this.detector = requireNonNull(detector);
      this.gridElementEvaluator = requireNonNull(gridElementEvaluator);
   }

   @Override
   public Optional<TargetGridElement> getNearestTargetGridElement(Position detectorPos) {
      Predicate<GridElement> isProjectile = Projectile.class::isInstance;
      return getAllPotentialTargetsWithinReach(detectorPos).stream()
            .filter(isProjectile.negate())
            .filter(this::isGridElementEnemy)
            .filter(isGridElementDetected(detectorPos))
            .sorted(new GridElement2DistanceComparator(detectorPos))
            .map(TargetGridElementImpl::of)
            .findFirst();
   }

   private Predicate<? super GridElement> isGridElementDetected(Position detectorPos) {
      return gridElement -> gridElement.isDetectedBy(detectorPos, detector);
   }

   boolean isGridElementEnemy(GridElement gridElement) {
      return (gridElement instanceof Belligerent) && belligerentParty.isEnemyParty(((Belligerent) gridElement).getBelligerentParty());
   }

   private List<GridElement> getAllPotentialTargetsWithinReach(Position detectorPos) {
      return gridElementEvaluator.evaluateGridElementsWithinDistance(detectorPos, detector.getDetectorRange());
   }

   public static class TargetGridElementEvaluatorBuilder {

      private GridElementEvaluator gridElementEvaluator;
      private BelligerentParty belligerentParty;
      private IDetector detector;

      private TargetGridElementEvaluatorBuilder() {
         // privatos
      }

      public TargetGridElementEvaluatorBuilder withGridElementEvaluator(GridElementEvaluator gridElementEvaluator) {
         this.gridElementEvaluator = gridElementEvaluator;
         return this;
      }

      public TargetGridElementEvaluatorBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
      }

      public TargetGridElementEvaluatorBuilder withDetector(IDetector detector) {
         this.detector = detector;
         return this;
      }

      public static TargetGridElementEvaluatorBuilder builder() {
         return new TargetGridElementEvaluatorBuilder();
      }

      /**
       * Creates a new {@link TargetGridElementEvaluator}
       * 
       * @return new {@link TargetGridElementEvaluator}
       */
      public TargetGridElementEvaluator build() {
         return new TargetGridElementEvaluatorImpl(belligerentParty, detector, gridElementEvaluator);
      }
   }
}

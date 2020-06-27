package com.myownb3.piranha.core.weapon.target;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.turret.turretscanner.GridElement2DistanceComparator;

public class TargetGridElementEvaluator {

   private GridElementEvaluator gridElementEvaluator;
   private BelligerentParty belligerentParty;
   private Detector detector;

   private TargetGridElementEvaluator(BelligerentParty belligerentParty, IDetector detector, GridElementEvaluator gridElementEvaluator) {
      this.belligerentParty = requireNonNull(belligerentParty);
      this.detector = requireNonNull(detector);
      this.gridElementEvaluator = requireNonNull(gridElementEvaluator);
   }

   public Optional<TargetGridElement> getNearestTargetGridElement(Position detectorPos) {
      Predicate<GridElement> isProjectile = Projectile.class::isInstance;
      return getAllPotentialTargetsWithinReach(detectorPos).stream()
            .filter(isProjectile.negate())
            .filter(isEnemy())
            .filter(isGridElementDetected(detectorPos))
            .sorted(new GridElement2DistanceComparator(detectorPos))
            .map(TargetGridElement::of)
            .findFirst();
   }

   private Predicate<? super GridElement> isGridElementDetected(Position detectorPos) {
      return gridElement -> gridElement.isDetectedBy(detectorPos, detector);
   }

   private Predicate<? super GridElement> isEnemy() {
      return gridElement -> isGridElementEnemy(gridElement);
   }

   boolean isGridElementEnemy(GridElement gridElement) {
      return (gridElement instanceof Belligerent) ? belligerentParty.isEnemyParty(((Belligerent) gridElement).getBelligerentParty()) : false;
   }

   private List<GridElement> getAllPotentialTargetsWithinReach(Position detectorPos) {
      return gridElementEvaluator.evaluateGridElementsWithinDistance(detectorPos, detector.getDetectorRange());
   }

   /**
    * Creates a new {@link TargetGridElementEvaluator}
    * 
    * @param belligerentParty
    *        the {@link BelligerentParty} of this evaluator
    * @param detector
    *        the {@link IDetector} to detect enemys
    * @param gridElementEvaluator
    *        the {@link GridElementEvaluator} to evaluate all {@link GridElement}s in the first place
    * @return
    */
   public static TargetGridElementEvaluator of(BelligerentParty belligerentParty, IDetector detector, GridElementEvaluator gridElementEvaluator) {
      return new TargetGridElementEvaluator(belligerentParty, detector, gridElementEvaluator);
   }
}

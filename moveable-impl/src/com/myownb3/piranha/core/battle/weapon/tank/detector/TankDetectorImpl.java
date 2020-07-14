package com.myownb3.piranha.core.battle.weapon.tank.detector;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.myownb3.piranha.core.battle.weapon.countermeasure.DecoyFlareDispenser;
import com.myownb3.piranha.core.battle.weapon.countermeasure.MissileCounterMeasureSystem;
import com.myownb3.piranha.core.battle.weapon.countermeasure.MissileCounterMeasureSystemImpl.MissileCounterMeasureSystemBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.GridElementDetector;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl.GridElementDetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.filter.FilterGridElementsMovingAway;
import com.myownb3.piranha.core.grid.filter.ProjectileGridElementsFilter;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

public class TankDetectorImpl implements TankDetector {

   private GridElementDetector gridElementDetector;
   private Supplier<TankGridElement> tankGridElementSupplier;
   private Optional<MissileCounterMeasureSystem> missileCounterMeasureSystemOpt;

   private TankDetectorImpl(GridElementDetector gridElementDetector, Optional<MissileCounterMeasureSystem> missileCounterMeasureSystemOpt,
         Supplier<TankGridElement> tankGridElementSupplier) {
      this.gridElementDetector = gridElementDetector;
      this.tankGridElementSupplier = tankGridElementSupplier;
      this.missileCounterMeasureSystemOpt = missileCounterMeasureSystemOpt;
   }

   @Override
   public void autodetect() {
      TankGridElement tankGridElement = tankGridElementSupplier.get();
      gridElementDetector.checkSurroundingFromPosition(tankGridElement, tankGridElement.getPosition());
      missileCounterMeasureSystemOpt.ifPresent(MissileCounterMeasureSystem::autodetect);
   }

   @Override
   public boolean isUnderFire() {
      return gridElementDetector.getDetectedGridElements(tankGridElementSupplier.get())
            .stream()
            .count() > 0;
   }

   public static class TankDetectorBuilder {
      private Supplier<TankGridElement> tankGridElementSupplier;
      private DecoyFlareDispenser decoyFlareDispenser;
      private Grid grid;
      private Detector detector;
      private GridElementDetector gridElementDetector;

      private TankDetectorBuilder() {
         // private
      }

      public TankDetectorBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TankDetectorBuilder withDetector(Detector detector) {
         this.detector = detector;
         return this;
      }

      public TankDetectorBuilder withTankGridElement(Supplier<TankGridElement> tankGridElementSupplier) {
         this.tankGridElementSupplier = tankGridElementSupplier;
         return this;
      }

      public TankDetectorBuilder withGridElementDetector(GridElementDetector gridElementDetector) {
         this.gridElementDetector = gridElementDetector;
         return this;
      }

      public TankDetectorBuilder withDecoyFlareDispenser(DecoyFlareDispenser decoyFlareDispenser) {
         this.decoyFlareDispenser = decoyFlareDispenser;
         return this;
      }

      public TankDetectorImpl build() {
         requireAllNotNull(gridElementDetector);
         GridElementDetector tankDetectorGridElementDetector = getGridElementDetector();
         Optional<MissileCounterMeasureSystem> missileCounterMeasureSystemOpt = getMissileCounterMeasureSystemOpt();
         return new TankDetectorImpl(tankDetectorGridElementDetector, missileCounterMeasureSystemOpt, tankGridElementSupplier);
      }

      private Optional<MissileCounterMeasureSystem> getMissileCounterMeasureSystemOpt() {
         Optional<MissileCounterMeasureSystem> missileCounterMeasureSystemOpt = Optional.empty();
         if (nonNull(decoyFlareDispenser)) {
            missileCounterMeasureSystemOpt = Optional.of(MissileCounterMeasureSystemBuilder.builder()
                  .withDecoyFlareDispenser(decoyFlareDispenser)
                  .withGrid(grid)
                  .withDetector(detector)
                  .withTankGridElementSupplier(tankGridElementSupplier)
                  .build());
         }
         return missileCounterMeasureSystemOpt;
      }

      private GridElementDetector getGridElementDetector() {
         if (nonNull(gridElementDetector)) {
            return gridElementDetector;
         }
         return buildGridElementDetector(getCheckSurroundingFilter());
      }

      private GridElementDetector buildGridElementDetector(Predicate<GridElement> checkSurroundingFilter) {
         return GridElementDetectorBuilder.builder()
               .withDetectingGridElementFilter(checkSurroundingFilter)
               .withDetector(detector)
               .withGrid(grid)
               .build();
      }

      private Predicate<GridElement> getCheckSurroundingFilter() {
         Predicate<GridElement> isProjectile = new ProjectileGridElementsFilter()::test;
         return isProjectile.and(FilterGridElementsMovingAway.of(tankGridElementSupplier));
      }

      public static TankDetectorBuilder builder() {
         return new TankDetectorBuilder();
      }

      private void requireAllNotNull(GridElementDetector gridElementDetector) {
         if (isNull(gridElementDetector)) {
            requireNonNull(grid);
            requireNonNull(detector);
         }
         requireNonNull(tankGridElementSupplier);
      }
   }
}

package com.myownb3.piranha.core.battle.weapon.countermeasure;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.turretscanner.GridElement2DistanceComparator;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.GridElementDetector;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl.GridElementDetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.filter.FilterGridElementsMovingAway;
import com.myownb3.piranha.core.grid.filter.ProjectileGridElementsFilter;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class MissileCounterMeasureSystemImpl implements MissileCounterMeasureSystem {
   private GridElementDetector gridElementDetector;
   private Supplier<TankGridElement> tankGridElementSupplier;
   private DecoyFlareDispenser decoyFlareDispenser;
   private DecoyFlareDeployPosEvaluator decoyFlareDeployPosEvaluator;

   private MissileCounterMeasureSystemImpl(GridElementDetector gridElementDetector, Supplier<TankGridElement> tankGridElementSupplier,
         DecoyFlareDispenser decoyFlareDispenser) {
      this.tankGridElementSupplier = tankGridElementSupplier;
      this.gridElementDetector = gridElementDetector;
      this.decoyFlareDispenser = decoyFlareDispenser;
      this.decoyFlareDeployPosEvaluator =
            new DecoyFlareDeployPosEvaluator(decoyFlareDispenser.getDecoyFlareConfig().getDimensionInfo());
   }

   @Override
   public void autodetect() {
      checkSurrounding();
      getDetectedMissiles().stream()
            .sorted(new GridElement2DistanceComparator(getDetectorPos()))
            .filter(hasEvasion())
            .map(GridElement::getPosition)
            .findFirst()
            .ifPresent(this::dispenseDecoyFlares);
   }

   private void dispenseDecoyFlares(Position detectedProjectilePosition) {
      Position deployFromPosition = decoyFlareDeployPosEvaluator.getDeployFromPosition(detectedProjectilePosition, getDetectorPos(), getTankHull());
      decoyFlareDispenser.dispenseDecoyFlares(deployFromPosition);
   }

   private List<GridElement> getDetectedMissiles() {
      return gridElementDetector.getDetectedGridElements(getTankGridElement()).stream()
            .map(ProjectileGridElement.class::cast)
            .collect(Collectors.toList());
   }

   private void checkSurrounding() {
      TankGridElement tankGridElement = getTankGridElement();
      gridElementDetector.checkSurroundingFromPosition(tankGridElement, tankGridElement.getPosition());
   }

   private Predicate<? super GridElement> hasEvasion() {
      return gridElement -> gridElementDetector.isEvasion(gridElement);
   }

   private TankGridElement getTankGridElement() {
      return tankGridElementSupplier.get();
   }

   private Shape getTankHull() {
      return getTankGridElement().getShape()
            .getHull();
   }

   private Position getDetectorPos() {
      return getTankGridElement()
            .getPosition();
   }

   public static class MissileCounterMeasureSystemBuilder {
      private Supplier<TankGridElement> tankGridElementSupplier;
      private DecoyFlareDispenser decoyFlareDispenser;
      private Grid grid;
      private Detector detector;

      private MissileCounterMeasureSystemBuilder() {
         // privatos
      }

      public MissileCounterMeasureSystemBuilder withDecoyFlareDispenser(DecoyFlareDispenser decoyFlareDispenser) {
         this.decoyFlareDispenser = decoyFlareDispenser;
         return this;
      }

      public MissileCounterMeasureSystemBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public MissileCounterMeasureSystemBuilder withDetector(Detector detector) {
         this.detector = detector;
         return this;
      }

      public MissileCounterMeasureSystemBuilder withTankGridElementSupplier(Supplier<TankGridElement> tankGridElementSupplier) {
         this.tankGridElementSupplier = tankGridElementSupplier;
         return this;
      }

      public static MissileCounterMeasureSystemBuilder builder() {
         return new MissileCounterMeasureSystemBuilder();
      }

      public MissileCounterMeasureSystem build() {
         requireAllNotNull();
         return new MissileCounterMeasureSystemImpl(buildGridElementDetector(), tankGridElementSupplier, decoyFlareDispenser);
      }

      private GridElementDetectorImpl buildGridElementDetector() {
         return GridElementDetectorBuilder.builder()
               .withDetectingGridElementFilter(getCheckSurroundingFilter())
               .withDetector(detector)
               .withGrid(grid)
               .build();
      }

      private Predicate<GridElement> getCheckSurroundingFilter() {
         Predicate<GridElement> isProjectile = new ProjectileGridElementsFilter()::test;
         return isProjectile.and(isMissile()).and(FilterGridElementsMovingAway.of(tankGridElementSupplier));
      }

      private Predicate<GridElement> isMissile() {
         return projectile -> ((ProjectileGridElement) projectile).getProjectileType() == ProjectileTypes.MISSILE;
      }

      private void requireAllNotNull() {
         requireNonNull(tankGridElementSupplier);
         requireNonNull(decoyFlareDispenser);
         requireNonNull(detector);
         requireNonNull(grid);
      }
   }
}

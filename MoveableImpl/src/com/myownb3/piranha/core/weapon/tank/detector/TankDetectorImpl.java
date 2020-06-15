package com.myownb3.piranha.core.weapon.tank.detector;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.detector.GridElementDetector;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.tank.TankGridElement;

public class TankDetectorImpl implements TankDetector {

   private GridElementDetector gridElementDetector;
   private Supplier<TankGridElement> tankGridElementSupplier;

   private TankDetectorImpl(GridElementDetector gridElementDetector, Supplier<TankGridElement> tankGridElementSupplier) {
      this.gridElementDetector = gridElementDetector;
      this.tankGridElementSupplier = tankGridElementSupplier;
   }

   @Override
   public void autodetect() {
      gridElementDetector.checkSurrounding(tankGridElementSupplier.get());
   }

   @Override
   public boolean isUnderFire() {
      return gridElementDetector.getDetectedGridElement(tankGridElementSupplier.get())
            .stream()
            .filter(Projectile.class::isInstance)
            .map(Belligerent.class::cast)
            .anyMatch(isEnemyProjectile());
   }

   private Predicate<? super Belligerent> isEnemyProjectile() {
      return projectile -> {
         TankGridElement otherBelligerent = tankGridElementSupplier.get();
         return projectile.isEnemy(otherBelligerent);
      };
   }

   public static class TankDetectorBuilder {
      private GridElementDetector gridElementDetector;
      private Supplier<TankGridElement> tankGridElementSupplier;

      private TankDetectorBuilder() {
         // private
      }

      public TankDetectorBuilder withGridElementDetector(GridElementDetector gridElementDetector) {
         this.gridElementDetector = gridElementDetector;
         return this;
      }

      public TankDetectorBuilder withTankGridElement(Supplier<TankGridElement> tankGridElementSupplier) {
         this.tankGridElementSupplier = tankGridElementSupplier;
         return this;
      }

      public TankDetectorImpl build() {
         return new TankDetectorImpl(gridElementDetector, tankGridElementSupplier);
      }

      public static TankDetectorBuilder builder() {
         return new TankDetectorBuilder();
      }

   }

}

package com.myownb3.piranha.core.weapon.tank.detector;

import java.util.function.Predicate;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.detector.GridElementDetector;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;

public class TankDetectorImpl implements TankDetector {

   private GridElementDetector gridElementDetector;
   private TankGridElementContextHolder tankGridElementHolder;

   private TankDetectorImpl(GridElementDetector gridElementDetector, TankGridElementContextHolder tankGridElementHolder) {
      this.gridElementDetector = gridElementDetector;
      this.tankGridElementHolder = tankGridElementHolder;
   }

   @Override
   public void autodetect() {
      gridElementDetector.checkSurrounding(tankGridElementHolder.getTankGridElement());
   }

   @Override
   public boolean isUnderFire() {
      return gridElementDetector.getDetectedGridElement(tankGridElementHolder.getTankGridElement())
            .stream()
            .filter(Projectile.class::isInstance)
            .map(Belligerent.class::cast)
            .allMatch(isEnemyProjectile());
   }

   private Predicate<? super Belligerent> isEnemyProjectile() {
      return projectile -> projectile.isEnemy(tankGridElementHolder.getTankGridElement());
   }

   public static class TankDetectorBuilder {
      private GridElementDetector gridElementDetector;
      private TankGridElementContextHolder tankGridElementContextHolder;

      private TankDetectorBuilder() {
         // private
      }

      public TankDetectorBuilder withGridElementDetector(GridElementDetector gridElementDetector) {
         this.gridElementDetector = gridElementDetector;
         return this;
      }

      public TankDetectorBuilder withTankGridElement(TankGridElementContextHolder tankGridElementContextHolder) {
         this.tankGridElementContextHolder = tankGridElementContextHolder;
         return this;
      }

      public TankDetectorImpl build() {
         return new TankDetectorImpl(gridElementDetector, tankGridElementContextHolder);
      }

      public static TankDetectorBuilder builder() {
         return new TankDetectorBuilder();
      }

   }

}

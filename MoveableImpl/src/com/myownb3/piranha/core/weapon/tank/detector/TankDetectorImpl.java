package com.myownb3.piranha.core.weapon.tank.detector;

import java.util.function.Supplier;

import com.myownb3.piranha.core.detector.GridElementDetector;
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
      TankGridElement tankGridElement = tankGridElementSupplier.get();
      gridElementDetector.checkSurroundingFromPosition(tankGridElement, tankGridElement.getPosition());
   }

   @Override
   public boolean isUnderFire() {
      return gridElementDetector.getDetectedGridElements(tankGridElementSupplier.get())
            .stream()
            .count() > 0;
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

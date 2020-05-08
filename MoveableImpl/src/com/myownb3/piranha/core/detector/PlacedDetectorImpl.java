package com.myownb3.piranha.core.detector;

import com.myownb3.piranha.core.grid.gridelement.position.Position;

public class PlacedDetectorImpl implements PlacedDetector {

   private IDetector detector;
   private Position position;

   private PlacedDetectorImpl(IDetector detector, Position position) {
      this.detector = detector;
      this.position = position;
   }

   @Override
   public Position getPosition() {
      return position;
   }

   @Override
   public IDetector getDetector() {
      return detector;
   }

   public static class PlacedDetectorBuilder {

      private IDetector iDetector;
      private Position position;

      private PlacedDetectorBuilder() {
         // private
      }

      public PlacedDetectorBuilder withIDetector(IDetector iDetector) {
         this.iDetector = iDetector;
         return this;
      }

      public PlacedDetectorBuilder withPosition(Position position) {
         this.position = position;
         return this;
      }

      public PlacedDetector build() {
         return new PlacedDetectorImpl(iDetector, position);
      }

      public static PlacedDetectorBuilder builder() {
         return new PlacedDetectorBuilder();
      }
   }
}

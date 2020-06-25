package com.myownb3.piranha.core.grid.gridelement.shape.dimension;

import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;

public class DimensionInfoImpl implements DimensionInfo {

   private double dimensionRadius;
   private double distanceToGround;
   private double heightFromBottom;

   private DimensionInfoImpl(double dimensionRadius, double distanceToGround, double heightFromBottom) {
      this.dimensionRadius = dimensionRadius;
      this.distanceToGround = distanceToGround;
      this.heightFromBottom = heightFromBottom;
   }

   @Override
   public double getDimensionRadius() {
      return dimensionRadius;
   }

   @Override
   public double getDistanceToGround() {
      return distanceToGround;
   }

   @Override
   public double getHeightFromBottom() {
      return heightFromBottom;
   }

   @Override
   public boolean isWithinHeight(DimensionInfo otherDimensionInfo) {
      double totalHeightFromGround = distanceToGround + heightFromBottom;
      return totalHeightFromGround >= otherDimensionInfo.getDistanceToGround();
   }

   public static class DimensionInfoBuilder {
      private double dimensionRadius;
      private double heightFromBottom;
      private double distanceToGround;

      private DimensionInfoBuilder() {
         // private
      }

      public DimensionInfoBuilder withDimensionRadius(double dimensionRadius) {
         this.dimensionRadius = dimensionRadius;
         return this;
      }

      public DimensionInfoBuilder withDistanceToGround(double distanceToGround) {
         this.distanceToGround = distanceToGround;
         return this;
      }

      public DimensionInfoBuilder withHeightFromBottom(double heightFromBottom) {
         this.heightFromBottom = heightFromBottom;
         return this;
      }

      public DimensionInfoImpl build() {
         return new DimensionInfoImpl(dimensionRadius, distanceToGround, heightFromBottom);
      }

      public static DimensionInfoBuilder builder() {
         return new DimensionInfoBuilder();
      }

      public static DimensionInfo getDefaultDimensionInfo(double dimensionRadius) {
         return DimensionInfoBuilder.builder()
               .withDimensionRadius(dimensionRadius)
               .withDistanceToGround(0.0)
               .withHeightFromBottom(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM)
               .build();
      }
   }
}

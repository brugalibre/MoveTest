package com.myownb3.piranha.core.grid.gridelement.shape.dimension;

import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

public class DimensionInfoImpl implements DimensionInfo {

   private double dimensionRadius;
   private double heightFromBottom;

   private DimensionInfoImpl(double dimensionRadius, double heightFromBottom) {
      this.dimensionRadius = dimensionRadius;
      this.heightFromBottom = heightFromBottom;
   }

   @Override
   public double getDimensionRadius() {
      return dimensionRadius;
   }

   @Override
   public double getHeightFromBottom() {
      return heightFromBottom;
   }

   @Override
   public boolean isWithinHeight(double ourDistanceToGround, double otherGridElemDistanceToGround) {
      double totalHeightFromGround = ourDistanceToGround + heightFromBottom;
      return totalHeightFromGround >= otherGridElemDistanceToGround;
   }

   public static class DimensionInfoBuilder {
      private double dimensionRadius;
      private double heightFromBottom;

      private DimensionInfoBuilder() {
         // private
      }

      public DimensionInfoBuilder withDimensionRadius(double dimensionRadius) {
         this.dimensionRadius = dimensionRadius;
         return this;
      }

      public DimensionInfoBuilder withHeightFromBottom(double heightFromBottom) {
         this.heightFromBottom = heightFromBottom;
         return this;
      }

      public DimensionInfoImpl build() {
         return new DimensionInfoImpl(dimensionRadius, heightFromBottom);
      }

      public static DimensionInfoBuilder builder() {
         return new DimensionInfoBuilder();
      }

      /**
       * Builds a default {@link DimensionInfo} with a default height from bottom
       * 
       * @see {@link GridElementConst#DEFAULT_HEIGHT_FROM_BOTTOM}
       * @param dimensionRadius
       *        the given radius
       * @return a default {@link DimensionInfo}
       */
      public static DimensionInfo getDefaultDimensionInfo(double dimensionRadius) {
         return DimensionInfoBuilder.builder()
               .withDimensionRadius(dimensionRadius)
               .withHeightFromBottom(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM)
               .build();
      }

      /**
       * Creates a new {@link DimensionInfo} build from the given one. Only the dimension radius is overwritten
       * 
       * @param dimensionInfo
       *        the {@link DimensionInfo} as template
       * @param dimensionRadius
       *        the new dimension radius (e.g. from a {@link Shape})
       * @return a new {@link DimensionInfo}
       */
      public static DimensionInfo buildFromExisting(DimensionInfo dimensionInfo, double dimensionRadius) {
         return DimensionInfoBuilder.builder()
               .withDimensionRadius(dimensionRadius)
               .withHeightFromBottom(dimensionInfo.getHeightFromBottom())
               .build();
      }
   }
}

package com.myownb3.piranha.core.grid.gridelement.shape.dimension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;

class DimensionInfoImplTest {

   @Test
   void testIsWithinReach() {

      // Given
      DimensionInfo dimensionInfoImpl = DimensionInfoBuilder.getDefaultDimensionInfo(5);
      DimensionInfo otherDimensionInfoImpl = DimensionInfoBuilder.builder()
            .withDimensionRadius(5)
            .withDistanceToGround(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM - 10)
            .withHeightFromBottom(10)
            .build();

      // When
      boolean actualIsWithinReach = dimensionInfoImpl.isWithinHeight(otherDimensionInfoImpl);

      // Then
      assertThat(actualIsWithinReach, is(true));
   }

   @Test
   void testIsNotWithinReach() {

      // Given
      DimensionInfo dimensionInfoImpl = DimensionInfoBuilder.getDefaultDimensionInfo(5);
      DimensionInfo otherDimensionInfoImpl = DimensionInfoBuilder.builder()
            .withDimensionRadius(5)
            .withDistanceToGround(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM + 1)
            .withHeightFromBottom(10)
            .build();

      // When
      boolean actualIsWithinReach = dimensionInfoImpl.isWithinHeight(otherDimensionInfoImpl);

      // Then
      assertThat(actualIsWithinReach, is(false));
   }

   @Test
   void testGetDimensionRadius() {

      // Given
      double dimensionRadius = 5.0;
      DimensionInfoImpl dimensionInfoImpl = DimensionInfoBuilder.builder()
            .withDimensionRadius(dimensionRadius)
            .build();

      // When
      double actualDimensionRadius = dimensionInfoImpl.getDimensionRadius();

      // Then
      assertThat(actualDimensionRadius, is(dimensionRadius));
   }

   @Test
   void testGetDistanceFromGround() {

      // Given
      double expectedGroundDistance = 0;
      DimensionInfo dimensionInfo = DimensionInfoBuilder.getDefaultDimensionInfo(5);

      // When
      double actualGroundDistance = dimensionInfo.getDistanceToGround();

      // Then
      assertThat(actualGroundDistance, is(expectedGroundDistance));
   }

   @Test
   void testGetHeightFromGround() {

      // Given
      double expectedHeightFromGround = 0.0;
      DimensionInfoImpl dimensionInfoImpl = DimensionInfoBuilder.builder()
            .withDimensionRadius(5)
            .build();

      // When
      double actualHeightFromGround = dimensionInfoImpl.getHeightFromBottom();

      // Then
      assertThat(actualHeightFromGround, is(expectedHeightFromGround));
   }
}

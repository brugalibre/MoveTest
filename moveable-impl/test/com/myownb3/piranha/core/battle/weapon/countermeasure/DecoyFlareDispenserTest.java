package com.myownb3.piranha.core.battle.weapon.countermeasure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.countermeasure.DecoyFlareDispenser.DecoyFlareDispenserBuilder;
import com.myownb3.piranha.core.battle.weapon.countermeasure.config.DecoyFlareConfigImpl.DecoyFlareConfigBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class DecoyFlareDispenserTest {

   @Test
   void testDispenseOneDecoyFlares() {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(500)
            .withMaxY(500)
            .build();
      DecoyFlareFactory.INSTANCE.registerGrid(grid);

      int decoyFlareSpreadAngle = 90;
      int aoumtOfFlares = 1;
      int dimensionRadius = 2;
      Position deployFromPosition = Positions.of(5, 5);
      Position expectedDeployFromPosition = Positions.of(5, 5d + (dimensionRadius * DecoyFlareDispenser.DIMENSION_RADIUS_MULTIPLIER));
      DecoyFlareDispenser decoyFlareDispenser = DecoyFlareDispenserBuilder.builder()
            .withMinTimeBetweenDispensing(500)
            .withDecoyFlareConfig(DecoyFlareConfigBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withDecoyFlareSpreadAngle(decoyFlareSpreadAngle)
                  .withAmountDecoyFlares(aoumtOfFlares)
                  .withVelocity(5)
                  .withDimensionInfo(DimensionInfoBuilder.builder()
                        .withDimensionRadius(dimensionRadius)
                        .withHeightFromBottom(10)
                        .build())
                  .withProjectileDamage(0)
                  .build())
            .build();

      // When
      decoyFlareDispenser.dispenseDecoyFlares(deployFromPosition);

      // Then
      assertThat(grid.getAllGridElements(null).size(), is(aoumtOfFlares));
      GridElement gridElement = grid.getAllGridElements(null).get(0);
      assertThat(gridElement.getPosition(), is(expectedDeployFromPosition));
   }

   @Test
   void testTwoDispenseDecoyFlares() {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(500)
            .withMaxY(500)
            .build();
      DecoyFlareFactory.INSTANCE.registerGrid(grid);

      int decoyFlareSpreadAngle = 90;
      int aoumtOfFlares = 3;
      int dimensionRadius = 2;
      Position deployFromPosition = Positions.of(5, 5);
      double increment = (double) decoyFlareSpreadAngle / (aoumtOfFlares + 1);
      Position expectedDeployFromPosition = deployFromPosition.rotate(-decoyFlareSpreadAngle / 2d)
            .rotate(increment)
            .movePositionForward4Distance((double) dimensionRadius * DecoyFlareDispenser.DIMENSION_RADIUS_MULTIPLIER);
      Position expectedDeployFromPosition2 = deployFromPosition.rotate(-decoyFlareSpreadAngle / 2d)
            .rotate(2d * increment)
            .movePositionForward4Distance((double) dimensionRadius * DecoyFlareDispenser.DIMENSION_RADIUS_MULTIPLIER);
      Position expectedDeployFromPosition3 = deployFromPosition.rotate(-decoyFlareSpreadAngle / 2d)
            .rotate(3d * increment)
            .movePositionForward4Distance((double) dimensionRadius * DecoyFlareDispenser.DIMENSION_RADIUS_MULTIPLIER);
      DecoyFlareDispenser decoyFlareDispenser = DecoyFlareDispenserBuilder.builder()
            .withMinTimeBetweenDispensing(500)
            .withDecoyFlareConfig(DecoyFlareConfigBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withDecoyFlareSpreadAngle(decoyFlareSpreadAngle)
                  .withAmountDecoyFlares(aoumtOfFlares)
                  .withVelocity(5)
                  .withDimensionInfo(DimensionInfoBuilder.builder()
                        .withDimensionRadius(dimensionRadius)
                        .withHeightFromBottom(10)
                        .build())
                  .withProjectileDamage(0)
                  .build())
            .build();

      // When
      decoyFlareDispenser.dispenseDecoyFlares(deployFromPosition);

      List<Position> actualGridElemPositions = grid.getAllGridElements(null).stream()
            .map(GridElement::getPosition)
            .sorted(Comparator.comparing(pos -> pos.getDirection().getAngle()))
            .collect(Collectors.toList());

      // Then
      assertThat(actualGridElemPositions.size(), is(aoumtOfFlares));
      Position actualGridElementPos1 = actualGridElemPositions.get(0);
      assertThat(actualGridElementPos1, is(expectedDeployFromPosition));
      Position actualGridElementPos2 = actualGridElemPositions.get(1);
      assertThat(actualGridElementPos2, is(expectedDeployFromPosition2));
      Position actualGridElementPos3 = actualGridElemPositions.get(2);
      assertThat(actualGridElementPos3, is(expectedDeployFromPosition3));
   }

   @Test
   void testDispenseDecoyFlares_DontDispenseTwice() {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(500)
            .withMaxY(500)
            .build();
      DecoyFlareFactory.INSTANCE.registerGrid(grid);

      Position deployFromPosition = Positions.of(5, 5);
      int aoumtOfFlares = 1;
      DecoyFlareDispenser decoyFlareDispenser = DecoyFlareDispenserBuilder.builder()
            .withMinTimeBetweenDispensing(500)
            .withDecoyFlareConfig(DecoyFlareConfigBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withDecoyFlareSpreadAngle(90)
                  .withAmountDecoyFlares(aoumtOfFlares)
                  .withVelocity(5)
                  .withDimensionInfo(DimensionInfoBuilder.builder()
                        .withDimensionRadius(2)
                        .withHeightFromBottom(10)
                        .build())
                  .withProjectileDamage(0)
                  .build())
            .build();

      // When
      decoyFlareDispenser.dispenseDecoyFlares(deployFromPosition);
      decoyFlareDispenser.dispenseDecoyFlares(deployFromPosition);

      // Then
      assertThat(grid.getAllGridElements(null).size(), is(aoumtOfFlares));
   }
}

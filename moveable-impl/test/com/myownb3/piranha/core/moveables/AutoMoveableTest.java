package com.myownb3.piranha.core.moveables;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.AutoMoveable.AutoMoveableBuilder;
import com.myownb3.piranha.core.moveables.types.AutoMoveableTypes;

class AutoMoveableTest {

   @Test
   void testGetAutoMoveableTypes() {

      // Given
      AutoMoveableTypes autoMoveableTypes = AutoMoveableTypes.DECOY_FLARE;

      // When
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(getDefaultDimensionInfo(3))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withHealth(1)
                  .withDamage(9.5)
                  .build())
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withAutoMoveableTypes(autoMoveableTypes)
            .build();

      // Then
      assertThat(autoMoveable.getAutoMoveableTypes(), is(autoMoveableTypes));
   }

   @Test
   void testGetDamage() {

      // Given
      double damage = 9.0;

      // When
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(getDefaultDimensionInfo(3))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withHealth(1)
                  .withDamage(damage)
                  .build())
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withAutoMoveableTypes(AutoMoveableTypes.DECOY_FLARE)
            .build();

      // Then
      assertThat(autoMoveable.getDamage().getDamageValue(), is(damage));
   }

   @Test
   void testGetBelligerentParty() {

      // Given
      BelligerentParty expectedBelligerentParty = BelligerentPartyConst.GALACTIC_EMPIRE;

      // When
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withBelligerentParty(expectedBelligerentParty)
            .withDimensionInfo(getDefaultDimensionInfo(3))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withHealth(1)
                  .withDamage(1)
                  .build())
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // Then
      assertThat(autoMoveable.getBelligerentParty(), is(expectedBelligerentParty));
   }

   @Test
   void testOnCollision() {

      // Given
      DestructionHelper expectedBelligerentParty = spy(DestructionHelperBuilder.builder()
            .withHealth(1)
            .withDamage(1)
            .build());
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(getDefaultDimensionInfo(3))
            .withDestructionHelper(expectedBelligerentParty)
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // When
      autoMoveable.onCollision(Collections.emptyList());

      // Then
      verify(expectedBelligerentParty).onCollision(eq(Collections.emptyList()));
   }

   @Test
   void testAutodetect() {
      // Given
      Position expectedPos = Positions.of(0, 0.1);
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withDimensionInfo(getDefaultDimensionInfo(3))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withHealth(1)
                  .withDamage(1)
                  .build())
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // When
      autoMoveable.autodetect();

      // Then
      assertThat(autoMoveable.getPosition(), is(expectedPos));
   }

}

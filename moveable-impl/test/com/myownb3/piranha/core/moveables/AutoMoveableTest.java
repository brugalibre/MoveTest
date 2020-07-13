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
import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AutoMoveable.AutoMoveableBuilder;

class AutoMoveableTest {

   @Test
   void testGetDamage() {

      // Given
      double damage = 9.0;

      // When
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(getDefaultDimensionInfo(3))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withHealth(HealthImpl.of(1))
                  .withDamage(DamageImpl.of(damage))
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
                  .withHealth(HealthImpl.of(1))
                  .withDamage(DamageImpl.of(1))
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
            .withHealth(HealthImpl.of(1))
            .withDamage(DamageImpl.of(1))
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
                  .withHealth(HealthImpl.of(1))
                  .withDamage(DamageImpl.of(1))
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

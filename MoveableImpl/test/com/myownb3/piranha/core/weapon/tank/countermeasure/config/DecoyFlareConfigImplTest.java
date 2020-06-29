package com.myownb3.piranha.core.weapon.tank.countermeasure.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.weapon.tank.countermeasure.config.DecoyFlareConfigImpl.DecoyFlareConfigBuilder;

class DecoyFlareConfigImplTest {

   @Test
   void testBuildAndVerifyDecoyFlareTTL() {
      // Given
      int expectedDecoyFlareTimeToLife = 10;
      DecoyFlareConfigImpl decoyFlareConfigImpl = DecoyFlareConfigBuilder.builder()
            .withAmountDecoyFlares(5)
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .withDecoyFlareSpreadAngle(90)
            .withDecoyFlareTimeToLife(expectedDecoyFlareTimeToLife)
            .withVelocity(5)
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(2)
                  .withHeightFromBottom(10)
                  .build())
            .withProjectileDamage(0)
            .build();

      // When
      int actualDecoyFlareTimeToLife = decoyFlareConfigImpl.getDecoyFlareTimeToLife();

      int offset = 3;
      int lowerBorder = actualDecoyFlareTimeToLife;
      int upperBorder = actualDecoyFlareTimeToLife + offset;
      int actualDecoyFlareTimeToLifeWithOffset = decoyFlareConfigImpl.getDecoyFlareTimeToLife(offset);

      // When
      assertThat(actualDecoyFlareTimeToLife, is(expectedDecoyFlareTimeToLife));
      assertThat(lowerBorder <= actualDecoyFlareTimeToLifeWithOffset && actualDecoyFlareTimeToLifeWithOffset <= upperBorder, is(true));
   }

   @Test
   void testBuildAndVerifyDecoyFlareConfig() {
      // Given
      int expectedAmountOfFlares = 15;
      double expectedDecoyFlareSpreadAngle = 90.0;
      DecoyFlareConfigImpl decoyFlareConfigImpl = DecoyFlareConfigBuilder.builder()
            .withAmountDecoyFlares(expectedAmountOfFlares)
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .withDecoyFlareSpreadAngle(90)
            .withDecoyFlareTimeToLife(5)
            .withVelocity(5)
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(2)
                  .withHeightFromBottom(10)
                  .build())
            .withProjectileDamage(0)
            .build();

      // When
      BelligerentParty actualBelligerentParty = decoyFlareConfigImpl.getBelligerentParty();
      int actualAmountOfFlares = decoyFlareConfigImpl.getAmountDecoyFlares();
      double actualDecoyFlareSpreadAngle = decoyFlareConfigImpl.getDecoyFlareSpreadAngle();

      // When
      assertThat(actualBelligerentParty, is(BelligerentPartyConst.REBEL_ALLIANCE));
      assertThat(actualAmountOfFlares, is(expectedAmountOfFlares));
      assertThat(actualDecoyFlareSpreadAngle, is(expectedDecoyFlareSpreadAngle));
   }
}

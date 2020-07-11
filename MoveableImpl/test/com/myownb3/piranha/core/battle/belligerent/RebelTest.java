package com.myownb3.piranha.core.battle.belligerent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.StroomTrooper;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.belligerent.rebelalliance.Rebel;

class RebelTest {

   @Test
   void testIsNotEnemy() {
      // Given
      Rebel rebel = new Rebel();

      // When
      boolean actualIsEnemy = rebel.isEnemy(new Rebel());

      // Then
      assertThat(actualIsEnemy, is(false));
   }

   @Test
   void testIsEnemy() {
      // Given
      Rebel rebel = new Rebel();

      // When
      boolean actualIsEnemy = rebel.isEnemy(new StroomTrooper());

      // Then
      assertThat(actualIsEnemy, is(true));
   }

   @Test
   void testGetBelligerentParty() {
      // Given
      Rebel rebel = new Rebel();

      // When
      BelligerentParty actualBelligerentParty = rebel.getBelligerentParty();

      // Then
      assertThat(actualBelligerentParty, is(BelligerentPartyConst.REBEL_ALLIANCE));
   }

}

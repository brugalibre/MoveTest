package com.myownb3.piranha.core.battle.belligerent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;

class StroomTrooperTest {

   @Test
   void testIsNotEnemy() {
      // Given
      StroomTrooper stroomTrooper = new StroomTrooper();

      // When
      boolean actualIsEnemy = stroomTrooper.isEnemy(new StroomTrooper());

      // Then
      assertThat(actualIsEnemy, is(false));
   }

   @Test
   void testIsEnemy() {
      // Given
      StroomTrooper stroomTrooper = new StroomTrooper();

      // When
      boolean actualIsEnemy = stroomTrooper.isEnemy(new Rebel());

      // Then
      assertThat(actualIsEnemy, is(true));
   }

   @Test
   void testGetBelligerentParty() {
      // Given
      StroomTrooper stroomTrooper = new StroomTrooper();

      // When
      BelligerentParty actualBelligerentParty = stroomTrooper.getBelligerentParty();

      // Then
      assertThat(actualBelligerentParty, is(BelligerentPartyConst.GALACTIC_EMPIRE));
   }

}

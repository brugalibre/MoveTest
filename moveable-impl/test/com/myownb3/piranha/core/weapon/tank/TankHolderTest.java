package com.myownb3.piranha.core.weapon.tank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.position.Position;

class TankHolderTest {

   @Test
   void testGetPosition_IsNull() {

      // Given
      TankHolder tankHolder = new TankHolder();

      // When
      Position actualPos = tankHolder.getPosition();

      // Then
      assertThat(actualPos, is(nullValue()));
   }

}

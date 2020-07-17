package com.myownb3.piranha.core.battle.weapon.gun.projectile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.descent.DescentHandler;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class DescentHandlerTest {

   @Test
   void testEvlPositionForNewHeight_FarEnough() {

      // Given
      int targetHeight = 0;
      Position startPos = Positions.of(5, 5, 5);
      double distanceBeforeDescent = 10;
      DescentHandler descentHandler = new DescentHandler(startPos, distanceBeforeDescent, targetHeight);
      Position currentPos = Positions.of(15, 5, 5);
      Position expectedNewPos = Positions.of(15, 5, 0);

      // When
      Position actualNewPos = descentHandler.evlPositionForNewHeight(currentPos);

      // Then
      assertThat(actualNewPos, is(expectedNewPos));
   }

   @Test
   void testEvlPositionForNewHeight_FarEnough_AlreadyOnTargetHeight() {

      // Given
      int targetHeight = 0;
      Position startPos = Positions.of(5, 5, 0);
      double distanceBeforeDescent = 10;
      DescentHandler descentHandler = new DescentHandler(startPos, distanceBeforeDescent, targetHeight);
      Position currentPos = Positions.of(15, 5, 0);
      Position expectedNewPos = Positions.of(15, 5, 0);

      // When
      Position actualNewPos = descentHandler.evlPositionForNewHeight(currentPos);

      // Then
      assertThat(actualNewPos, is(expectedNewPos));
   }

   @Test
   void testEvlPositionForNewHeight_NotFarEnough() {

      // Given
      int targetHeight = 0;
      Position startPos = Positions.of(5, 5, 5);
      double distanceBeforeDescent = 100;
      DescentHandler descentHandler = new DescentHandler(startPos, distanceBeforeDescent, targetHeight);
      Position currentPos = Positions.of(6, 5, 5);
      Position expectedNewPos = currentPos;

      // When
      Position actualNewPos = descentHandler.evlPositionForNewHeight(currentPos);

      // Then
      assertThat(actualNewPos, is(expectedNewPos));
   }

}

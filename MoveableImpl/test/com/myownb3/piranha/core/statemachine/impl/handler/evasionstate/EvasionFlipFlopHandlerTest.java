package com.myownb3.piranha.core.statemachine.impl.handler.evasionstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class EvasionFlipFlopHandlerTest {

   @Test
   void testGetEffectiveAvoidAngle_FlipFlop() {

      // Given
      double secondAvoidingAngle = -5;
      double firstAvoidingAngle = 5;
      EvasionFlipFlopHandler evasionFlipFlopHandler = new EvasionFlipFlopHandler();
      double expectedAvoidingAngle = firstAvoidingAngle;

      // When
      // first flip flop
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);

      // second flip flop
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);

      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);

      double actualAvoidAngle = evasionFlipFlopHandler.getAvoidAngle(firstAvoidingAngle);

      // Then
      assertThat(actualAvoidAngle, is(expectedAvoidingAngle));
   }

   @Test
   void testGetEffectiveAvoidAngle_NoFlipFlop_1ValueDiffersFrom2() {

      // Given
      double secondAvoidingAngle = -5;
      double firstAvoidingAngle = 5;
      EvasionFlipFlopHandler evasionFlipFlopHandler = new EvasionFlipFlopHandler();
      double expectedAvoidingAngle = secondAvoidingAngle;

      // When
      // first flip flop
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);

      // second flip flop
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle + 1);

      double actualAvoidAngle = evasionFlipFlopHandler.getAvoidAngle(secondAvoidingAngle);

      // Then
      assertThat(actualAvoidAngle, is(expectedAvoidingAngle));
   }

   @Test
   void testGetEffectiveAvoidAngle_NoFlipFlop_3ValueDiffersFrom1() {

      // Given
      double firstAvoidingAngle = 5;
      double secondAvoidingAngle = -5;
      double thirdAvoidingAngle = -4;
      EvasionFlipFlopHandler evasionFlipFlopHandler = new EvasionFlipFlopHandler();
      double expectedAvoidingAngle = secondAvoidingAngle;

      // When
      // first flip flop
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);

      // second flip flop
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(thirdAvoidingAngle);

      double actualAvoidAngle = evasionFlipFlopHandler.getAvoidAngle(secondAvoidingAngle);

      // Then
      assertThat(actualAvoidAngle, is(expectedAvoidingAngle));
      assertThat(evasionFlipFlopHandler.avoidAngleHistory.size(), is(2));
      assertThat(evasionFlipFlopHandler.flipFlopCounter, is(0));
   }

   @Test
   void testGetEffectiveAvoidAngle_NoFlipFlop_1ValueSameThan2() {

      // Given
      double secondAvoidingAngle = -5;
      double firstAvoidingAngle = 5;
      EvasionFlipFlopHandler evasionFlipFlopHandler = new EvasionFlipFlopHandler();
      double expectedAvoidingAngle = secondAvoidingAngle;

      // When
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);

      double actualAvoidAngle = evasionFlipFlopHandler.getAvoidAngle(secondAvoidingAngle);

      // Then
      assertThat(actualAvoidAngle, is(expectedAvoidingAngle));
      assertThat(evasionFlipFlopHandler.avoidAngleHistory.size(), is(2));
      assertThat(evasionFlipFlopHandler.flipFlopCounter, is(0));
   }

   @Test
   void testGetEffectiveAvoidAngle_NoFlipFlop() {

      // Given
      double firstAvoidingAngle = 5;
      double secondAvoidingAngle = -5;
      double thirdAvoidingAngle = 5;
      EvasionFlipFlopHandler evasionFlipFlopHandler = new EvasionFlipFlopHandler();
      double expectedAvoidingAngle = secondAvoidingAngle;

      // When
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(thirdAvoidingAngle);
      double actualAvoidAngle = evasionFlipFlopHandler.getAvoidAngle(secondAvoidingAngle);

      // Then
      assertThat(actualAvoidAngle, is(expectedAvoidingAngle));
   }

   @Test
   void testGetEffectiveAvoidAngle_NoFlipFlop2() {

      // Given
      double firstAvoidingAngle = 5;
      double secondAvoidingAngle = -5;
      double thirdAvoidingAngle = 5;
      EvasionFlipFlopHandler evasionFlipFlopHandler = new EvasionFlipFlopHandler();
      double expectedAvoidingAngle = secondAvoidingAngle;

      // When
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(thirdAvoidingAngle);
      double actualAvoidAngle = evasionFlipFlopHandler.getAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(thirdAvoidingAngle);

      // Then
      assertThat(actualAvoidAngle, is(expectedAvoidingAngle));
   }

   @Test
   void testReset() {

      // Given
      double secondAvoidingAngle = -5;
      double firstAvoidingAngle = 5;
      EvasionFlipFlopHandler evasionFlipFlopHandler = new EvasionFlipFlopHandler();
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(secondAvoidingAngle);
      evasionFlipFlopHandler.registerAvoidAngle(firstAvoidingAngle);

      // When
      evasionFlipFlopHandler.init();

      // Then
      assertThat(evasionFlipFlopHandler.avoidAngleHistory.isEmpty(), is(true));
   }

}

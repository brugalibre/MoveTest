package com.myownb3.piranha.core.battle.weapon.guncarriage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.battle.weapon.guncarriage.state.GunCarriageStates;

class AbstractGunCarriageTest {

   @Test
   void testGunCarriageState_FromtIdleTurningToIdle() {

      // Given
      GunCarriageStates currentState = GunCarriageStates.IDLE;
      GunCarriageStates expectedNewGunCarriageState = GunCarriageStates.IDLE;
      double angle = 0.0;

      // When
      GunCarriageStates actualNewGunCarriageState = AbstractGunCarriage.evaluateCurrentGunCarriageState(angle, currentState);

      // Then
      assertThat(actualNewGunCarriageState, is(expectedNewGunCarriageState));
   }

   @Test
   void testGunCarriageState_FromtStartTurningToIdle() {

      // Given
      GunCarriageStates currentState = GunCarriageStates.START_TURNING;
      GunCarriageStates expectedNewGunCarriageState = GunCarriageStates.IDLE;
      double angle = 0.0;

      // When
      GunCarriageStates actualNewGunCarriageState = AbstractGunCarriage.evaluateCurrentGunCarriageState(angle, currentState);

      // Then
      assertThat(actualNewGunCarriageState, is(expectedNewGunCarriageState));
   }

   @Test
   void testGunCarriageState_FromtStartTurningToTurning() {

      // Given
      GunCarriageStates currentState = GunCarriageStates.START_TURNING;
      GunCarriageStates expectedNewGunCarriageState = GunCarriageStates.TURNING;
      double angle = 0.5;

      // When
      GunCarriageStates actualNewGunCarriageState = AbstractGunCarriage.evaluateCurrentGunCarriageState(angle, currentState);

      // Then
      assertThat(actualNewGunCarriageState, is(expectedNewGunCarriageState));
   }

   @Test
   void testGunCarriageState_FromIdleToStartTurning() {

      // Given
      GunCarriageStates currentState = GunCarriageStates.IDLE;
      GunCarriageStates expectedNewGunCarriageState = GunCarriageStates.START_TURNING;
      double angle = 0.5;

      // When
      GunCarriageStates actualNewGunCarriageState = AbstractGunCarriage.evaluateCurrentGunCarriageState(angle, currentState);

      // Then
      assertThat(actualNewGunCarriageState, is(expectedNewGunCarriageState));
   }

   @Test
   void testGunCarriageState_FromTurningToEndTurning() {

      // Given
      GunCarriageStates currentState = GunCarriageStates.TURNING;
      GunCarriageStates expectedNewGunCarriageState = GunCarriageStates.END_TURNING;
      double angle = 0.0;

      // When
      GunCarriageStates actualNewGunCarriageState = AbstractGunCarriage.evaluateCurrentGunCarriageState(angle, currentState);

      // Then
      assertThat(actualNewGunCarriageState, is(expectedNewGunCarriageState));
   }

   @Test
   void testGunCarriageState_FromTurningToTurning_StayTurning() {

      // Given
      GunCarriageStates currentState = GunCarriageStates.TURNING;
      GunCarriageStates expectedNewGunCarriageState = GunCarriageStates.TURNING;
      double angle = 0.5;

      // When
      GunCarriageStates actualNewGunCarriageState = AbstractGunCarriage.evaluateCurrentGunCarriageState(angle, currentState);

      // Then
      assertThat(actualNewGunCarriageState, is(expectedNewGunCarriageState));
   }

   @Test
   void testGunCarriageState_FromEndTurningToIdle() {

      // Given
      GunCarriageStates currentState = GunCarriageStates.END_TURNING;
      GunCarriageStates expectedNewGunCarriageState = GunCarriageStates.IDLE;
      double angle = 0.0;

      // When
      GunCarriageStates actualNewGunCarriageState = AbstractGunCarriage.evaluateCurrentGunCarriageState(angle, currentState);

      // Then
      assertThat(actualNewGunCarriageState, is(expectedNewGunCarriageState));
   }

   @Test
   void testGunCarriageState_FromEndTurningToStartTurningIdle() {

      // Given
      GunCarriageStates currentState = GunCarriageStates.END_TURNING;
      GunCarriageStates expectedNewGunCarriageState = GunCarriageStates.START_TURNING;
      double angle = 0.60;

      // When
      GunCarriageStates actualNewGunCarriageState = AbstractGunCarriage.evaluateCurrentGunCarriageState(angle, currentState);

      // Then
      assertThat(actualNewGunCarriageState, is(expectedNewGunCarriageState));
   }

   @Test
   void testGunCarriageState_InvalidState() {

      // Given
      GunCarriageStates state = GunCarriageStates.NONE;

      // When
      Executable exec = () -> AbstractGunCarriage.evaluateCurrentGunCarriageState(0, state);

      // Then
      assertThrows(IllegalStateException.class, exec);
   }
}

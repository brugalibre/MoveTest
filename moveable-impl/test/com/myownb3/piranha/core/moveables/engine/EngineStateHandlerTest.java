package com.myownb3.piranha.core.moveables.engine;

import static com.myownb3.piranha.core.moveables.engine.EngineStates.ACCELERATING;
import static com.myownb3.piranha.core.moveables.engine.EngineStates.IDLE;
import static com.myownb3.piranha.core.moveables.engine.EngineStates.MOVING_BACKWARDS;
import static com.myownb3.piranha.core.moveables.engine.EngineStates.MOVING_FORWARDS;
import static com.myownb3.piranha.core.moveables.engine.EngineStates.SLOWINGDOWN_NATURALLY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.moveables.engine.accelerate.EngineAccelerator;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.transmission.Gear;

class EngineStateHandlerTest {

   @Test
   void testHandleEngineState_UnknownState() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.NONE;

      // When
      Executable executable = () -> engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThrows(IllegalStateException.class, executable);
   }

   @Test
   void testHandleEngineState_IdleToIdle() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.IDLE;
      EngineStates expectedEngineState = EngineStates.IDLE;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_IdleToAcceleratingBackwards() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.IDLE;
      EngineStates expectedEngineState = EngineStates.ACCELERATING;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_IdleToAccelerating_Forwards() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.IDLE;
      EngineStates expectedEngineState = EngineStates.ACCELERATING;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_MovingForwardToSlowingDown() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.MOVING_FORWARDS;
      EngineStates expectedEngineState = EngineStates.SLOWINGDOWN;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_MovingForwardToSlowingDownNaturally() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.MOVING_FORWARDS;
      EngineStates expectedEngineState = EngineStates.SLOWINGDOWN_NATURALLY;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_MovingForwardToMovingForward() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.MOVING_FORWARDS;
      EngineStates expectedEngineState = EngineStates.MOVING_FORWARDS;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_MovingBackwardsToSlowingDown() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.MOVING_BACKWARDS;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThat(actualEngineState, is(SLOWINGDOWN_NATURALLY));
   }

   @Test
   void testHandleEngineState_MovingBackwardsToMovingBackwards() {

      // Given
      EngineStateHandler engineStateHandler = new EngineStateHandler();
      EngineStates currentEngineState = EngineStates.MOVING_BACKWARDS;
      EngineStates expectedEngineState = EngineStates.MOVING_BACKWARDS;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_AcceleratingToMovingForward_AcceleratingWasLongEnough() {

      // Given
      double acceleratingSpeed = 0.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 2);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.ACCELERATING;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, currentEngineState);
      actualEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, currentEngineState);// 2nd time since we have to gears

      // Then
      assertThat(actualEngineState, is(MOVING_FORWARDS));
   }

   @Test
   void testHandleEngineState_AcceleratingToAccelerating_Forwards_StillAccelerating() {

      // Given
      double acceleratingSpeed = 10.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 2);

      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.ACCELERATING;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, currentEngineState);

      // Then
      assertThat(actualEngineState, is(ACCELERATING));
   }

   @Test
   void testHandleEngineState_AcceleratingToAccelerating_Backwards_StillAccelerating() {

      // Given
      double acceleratingSpeed = 10.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 2);

      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.ACCELERATING;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);

      // Then
      assertThat(actualEngineState, is(ACCELERATING));
   }

   @Test
   void testHandleEngineState_AcceleratingToMovingBackwards_AcceleratingWasLongEnough() {

      // Given
      double acceleratingSpeed = 0.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 2);

      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.ACCELERATING;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);
      actualEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);// 2nd times, since we have to gears

      // Then
      assertThat(actualEngineState, is(MOVING_BACKWARDS));
   }

   @Test
   void testHandleEngineState_AcceleratingToSlowingDownNaturally_NeitherMovingForwardsNorBackwards_DoneSlowingDownNaturally() {

      // Given
      double acceleratingSpeed = 0.0;
      int manuallySlowDownSpeed = 0;
      Gear gear1 = GearBuilder.builder()
            .withAccelerationSpeed(acceleratingSpeed)
            .withMaxVelocity(1)
            .withNumber(1)
            .buil();
      Gear gear2 = GearBuilder.builder()
            .withAccelerationSpeed(acceleratingSpeed)
            .withMaxVelocity(1)
            .withNumber(2)
            .buil();
      EngineTransmissionConfigImpl transmissionConfigImpl = spy(EngineTransmissionConfigBuilder.builder()
            .addGear(gear1)
            .addGear(gear2)
            .addGear(gear2)
            .build());
      doReturn(acceleratingSpeed).when(transmissionConfigImpl).getCurrentAccelerationSpeed(anyInt());
      doReturn(1).when(transmissionConfigImpl).getCurrentMaxVelocity(anyInt());
      EngineAccelerator engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(transmissionConfigImpl)
            .withManuallySlowDownSpeed(manuallySlowDownSpeed)
            .withNaturallySlowDownSpeed(manuallySlowDownSpeed)
            .build();
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.ACCELERATING;

      // When

      // First accelerate, so that we are in the 2nd gear
      EngineStates actualEngineState0 = engineStateHandler.handleEngineState(MovingDirections.FORWARD, currentEngineState);
      EngineStates actualEngineState1 = engineStateHandler.handleEngineState(MovingDirections.FORWARD, currentEngineState);
      EngineStates actualEngineState2 = engineStateHandler.handleEngineState(MovingDirections.FORWARD, currentEngineState);
      EngineStates actualEngineState3 = engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThat(actualEngineState0, is(ACCELERATING));
      assertThat(actualEngineState1, is(ACCELERATING));
      assertThat(actualEngineState2, is(MOVING_FORWARDS));
      assertThat(actualEngineState3, is(SLOWINGDOWN_NATURALLY));
   }

   @Test
   void testHandleEngineState_AcceleratingToIdle_NeitherMovingForwardsNorBackwards_DoneSlowingDownNaturally_TwoGears() {

      // Given
      double acceleratingSpeed = 100.0;
      int manuallySlowDownSpeed = 0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, manuallySlowDownSpeed, manuallySlowDownSpeed, 2);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.ACCELERATING;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThat(actualEngineState, is(IDLE));
   }

   @Test
   void testHandleEngineState_AcceleratingToSlowingDownNaturally_NeitherMovingForwardsNorBackwards_NotYetDoneSlowingDownNaturally() {

      // Given
      double acceleratingSpeed = 100.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 100, 100, 2);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.ACCELERATING;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThat(actualEngineState, is(EngineStates.SLOWINGDOWN_NATURALLY));
   }

   @Test
   void testHandleEngineState_SlowingDownToIdleSlowingDownWasLongEnough() throws InterruptedException {

      // Given
      double acceleratingSpeed = 100.0;
      int naturallySlowDownSpeed = 0;
      EngineAccelerator engineAccelerator = spy(buildEngineAccelerator(acceleratingSpeed, naturallySlowDownSpeed, naturallySlowDownSpeed, 2));
      when(engineAccelerator.getCurrentVelocity()).thenReturn(5);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);

      // When
      engineStateHandler.handleEngineState(MovingDirections.FORWARD, EngineStates.IDLE); // initial move forward, so we have a direction
      engineStateHandler.handleEngineState(MovingDirections.BACKWARD, EngineStates.SLOWINGDOWN);
      Thread.sleep(50);
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, EngineStates.SLOWINGDOWN);

      // Then
      assertThat(actualEngineState, is(IDLE));
   }

   @Test
   void testHandleEngineState_SlowingDownToAccelerating_StillSlowingDown() throws InterruptedException {

      // Given
      double acceleratingSpeed = 1000.0;
      EngineAccelerator engineAccelerator = spy(buildEngineAccelerator(acceleratingSpeed, acceleratingSpeed, 500, 2));
      when(engineAccelerator.getCurrentVelocity()).thenReturn(5);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.SLOWINGDOWN;
      EngineStates expectedEngineState1 = EngineStates.SLOWINGDOWN;
      EngineStates expectedEngineState2 = EngineStates.SLOWINGDOWN;

      // When
      engineStateHandler.handleEngineState(MovingDirections.FORWARD, EngineStates.IDLE); // initial move forward, so we have a direction
      EngineStates actualEngineState1 = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);
      Thread.sleep((long) (acceleratingSpeed / 2d));
      EngineStates actualEngineState2 = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);

      // Then
      assertThat(actualEngineState1, is(expectedEngineState1));
      assertThat(actualEngineState2, is(expectedEngineState2));
   }

   @Test
   void testHandleEngineState_SlowingDownToIdle_NotSlowingDownAnyMore() throws InterruptedException {

      // Given
      double acceleratingSpeed = 1000.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 2);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates expectedEngineState = EngineStates.IDLE;

      // When
      engineStateHandler.handleEngineState(MovingDirections.BACKWARD, EngineStates.IDLE);
      engineStateHandler.handleEngineState(MovingDirections.FORWARD, EngineStates.SLOWINGDOWN);
      Thread.sleep(500);
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, EngineStates.SLOWINGDOWN);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_SlowingDownNaturallyToAccelerating_SlowingDownWasLongEnough() throws InterruptedException {

      // Given
      double acceleratingSpeed = 100.0;
      int naturallySlowDownSpeed = 0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, naturallySlowDownSpeed, naturallySlowDownSpeed, 2);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);

      // When
      EngineStates handleEngineAccelerating = engineStateHandler.handleEngineState(MovingDirections.FORWARD, EngineStates.IDLE); // initial move forward, so we have a direction
      EngineStates handleEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, handleEngineAccelerating);
      Thread.sleep(50);
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, handleEngineState);

      // Then
      assertThat(actualEngineState, is(ACCELERATING));
   }

   @Test
   void testHandleEngineState_SlowingDownNaturallyToSlowingDownStillSlowingDown() throws InterruptedException {

      // Given
      double acceleratingSpeed = 1000.0;
      EngineAccelerator engineAccelerator = spy(buildEngineAccelerator(acceleratingSpeed, acceleratingSpeed, 500, 2));
      when(engineAccelerator.getCurrentVelocity()).thenReturn(5);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.SLOWINGDOWN_NATURALLY;
      EngineStates expectedEngineState1 = EngineStates.SLOWINGDOWN;
      EngineStates expectedEngineState2 = EngineStates.SLOWINGDOWN;

      // When
      engineStateHandler.handleEngineState(MovingDirections.FORWARD, EngineStates.IDLE);
      EngineStates actualEngineState1 = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);
      Thread.sleep((long) (acceleratingSpeed / 2d));
      EngineStates actualEngineState2 = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, currentEngineState);

      // Then
      assertThat(actualEngineState1, is(expectedEngineState1));
      assertThat(actualEngineState2, is(expectedEngineState2));
   }

   @Test
   void testHandleEngineState_SlowingDownNaturallyToIdle_DoneSlowingDown() throws InterruptedException {

      // Given
      double acceleratingSpeed = 500;
      EngineAccelerator engineAccelerator = spy(buildEngineAccelerator(acceleratingSpeed, acceleratingSpeed, 500, 2));
      when(engineAccelerator.getCurrentVelocity()).thenReturn(5);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);

      // When
      engineStateHandler.handleEngineState(MovingDirections.FORWARD, EngineStates.IDLE);
      EngineStates handleEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, EngineStates.SLOWINGDOWN_NATURALLY);
      Thread.sleep(500);
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, handleEngineState);

      // Then
      assertThat(actualEngineState, is(EngineStates.IDLE));
   }

   @Test
   void testHandleEngineState_MovingBackward_SlowingDownNaturallyToIdle_NotSlowingDownAnyMore() throws InterruptedException {

      // Given
      double acceleratingSpeed = 1000.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 1);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);

      // When
      EngineStates engineStateAccelerating = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, EngineStates.IDLE);// start accelerating
      EngineStates engineStateMovingBackwards = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, engineStateAccelerating);// start moving backwards
      EngineStates engineStateSlowingDownNaturally = engineStateHandler.handleEngineState(MovingDirections.FORWARD, engineStateMovingBackwards);// move forward
      EngineStates engineStateSlowingDown = engineStateHandler.handleEngineState(MovingDirections.NONE, engineStateSlowingDownNaturally);
      Thread.sleep(500);
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.NONE, engineStateSlowingDown);

      // Then
      assertThat(engineStateAccelerating, is(EngineStates.ACCELERATING));
      assertThat(engineStateSlowingDown, is(EngineStates.SLOWINGDOWN_NATURALLY));
      assertThat(actualEngineState, is(EngineStates.IDLE));
   }

   @Test
   void testHandleEngineState_MovingBackward_SlowingDownManuallyToAccelerating_NotSlowingDownAnyMore() throws InterruptedException {

      // Given
      double acceleratingSpeed = 1000.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 1);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);

      // When
      EngineStates engineStateAccelerating = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, EngineStates.IDLE);// start accelerating
      EngineStates engineStateMovingBackwards = engineStateHandler.handleEngineState(MovingDirections.BACKWARD, engineStateAccelerating);// start moving backwards
      EngineStates engineStateSlowingDownNaturally = engineStateHandler.handleEngineState(MovingDirections.FORWARD, engineStateMovingBackwards);// move forward
      EngineStates engineStateSlowingDown = engineStateHandler.handleEngineState(MovingDirections.FORWARD, engineStateSlowingDownNaturally);
      Thread.sleep(500);
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.FORWARD, engineStateSlowingDown);

      // Then
      assertThat(engineStateAccelerating, is(EngineStates.ACCELERATING));
      assertThat(engineStateSlowingDown, is(EngineStates.SLOWINGDOWN));
      assertThat(actualEngineState, is(EngineStates.ACCELERATING));
   }

   @Test
   void testHandleEngineState_SlowingDownToSlowingDownNaturally_StillSlowingDown() {

      // Given
      double acceleratingSpeed = 1000.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 2);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.SLOWINGDOWN;
      EngineStates expectedEngineState = EngineStates.SLOWINGDOWN_NATURALLY;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   @Test
   void testHandleEngineState_SlowingDownToIdle_DoneSlowingDown() {

      // Given
      double acceleratingSpeed = 1000.0;
      EngineAccelerator engineAccelerator = buildEngineAccelerator(acceleratingSpeed, 0, 0, 2);
      EngineStateHandler engineStateHandler = new EngineStateHandler(engineAccelerator);
      EngineStates currentEngineState = EngineStates.SLOWINGDOWN;
      EngineStates expectedEngineState = EngineStates.IDLE;

      // When
      EngineStates actualEngineState = engineStateHandler.handleEngineState(MovingDirections.NONE, currentEngineState);

      // Then
      assertThat(actualEngineState, is(expectedEngineState));
   }

   private static EngineAcceleratorImpl buildEngineAccelerator(double acceleratingSpeed, double manuallySlowDownSpeed,
         double naturallySlowDownSpeed, int amountOfGears) {
      Gear gear1 = GearBuilder.builder()
            .withAccelerationSpeed(acceleratingSpeed)
            .withMaxVelocity(1)
            .withNumber(1)
            .buil();
      EngineTransmissionConfigImpl transmissionConfigImpl;
      if (amountOfGears == 2) {
         transmissionConfigImpl = spy(EngineTransmissionConfigBuilder.builder()
               .addGear(gear1)
               .addGear(GearBuilder.builder()
                     .withAccelerationSpeed(acceleratingSpeed)
                     .withMaxVelocity(1)
                     .withNumber(2)
                     .buil())
               .build());

         doReturn(gear1.getAccelerationSpeed()).when(transmissionConfigImpl).getCurrentAccelerationSpeed(anyInt());
         doReturn(gear1.getMaxVelocity()).when(transmissionConfigImpl).getCurrentMaxVelocity(anyInt());
      } else {
         transmissionConfigImpl = spy(EngineTransmissionConfigBuilder.builder()
               .addGear(gear1)
               .build());
      }
      return EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(transmissionConfigImpl)
            .withManuallySlowDownSpeed(manuallySlowDownSpeed)
            .withNaturallySlowDownSpeed(naturallySlowDownSpeed)
            .build();
   }

   private static EngineAcceleratorImpl buildEngineAccelerator(double acceleratingSpeed, int amountOfGears) {
      return buildEngineAccelerator(acceleratingSpeed, 500, 500, amountOfGears);
   }
}

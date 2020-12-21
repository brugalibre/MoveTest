package com.myownb3.piranha.core.battle.weapon.tank.engine.human;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;
import com.myownb3.piranha.core.moveables.engine.human.HumanMoveableEngine;
import com.myownb3.piranha.core.moveables.engine.human.HumanMoveableEngine.HumanMoveableEngineBuilder;

class HumanMoveableEngineIntegTest {

   @Test
   void testOnForward_AndMove() throws InterruptedException {
      // Given
      int accelerationSpeed = 1300;
      double manuallySlowDownSpeed = 200;
      double naturallySlowDownSpeed = 900;
      int velocity = 180;
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = new EngineStateHandler(EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withAccelerationSpeed(accelerationSpeed)
                        .withMaxVelocity(velocity / 3)
                        .withNumber(1)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withAccelerationSpeed(accelerationSpeed)
                        .withMaxVelocity(2 * velocity / 3)
                        .withNumber(1)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withAccelerationSpeed(accelerationSpeed)
                        .withMaxVelocity(velocity)
                        .withNumber(3)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(manuallySlowDownSpeed)
            .withNaturallySlowDownSpeed(naturallySlowDownSpeed)
            .build());
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(velocity)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanMoveableEngine.onForwardPressed(true);
      humanMoveableEngine.moveForward(); // switch state accelerating 
      humanMoveableEngine.moveForward(); // accelerate from 0 -> 1
      humanMoveableEngine.moveForward(); // start accelerating from 1 -> 2
      Thread.sleep(1000);
      humanMoveableEngine.moveForward(); // done accelerating from 1 -> 2
      humanMoveableEngine.moveForward(); // start accelerating from 2 -> 3
      Thread.sleep(1000);
      humanMoveableEngine.moveForward(); // done accelerating from 2 -> 3
      humanMoveableEngine.moveForward(); // start 'moving forwards'
      Thread.sleep(1000);
      humanMoveableEngine.moveForward(); // state 'moving forwards'
      Thread.sleep(1000);
      humanMoveableEngine.moveForward(); // state 'moving forwards'

      // Then
      verify(humanMoveableEngine.getMoveable(), times(8)).moveForward();
      assertThat(engineStateHandler.getCurrentVelocity(), is(velocity));
   }

   @Test
   void testOnBackward() throws InterruptedException {
      // Given
      int accelerationSpeed = 1300;
      double manuallySlowDownSpeed = 200;
      double naturallySlowDownSpeed = 900;
      int velocity = 180;
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = new EngineStateHandler(EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withAccelerationSpeed(accelerationSpeed)
                        .withMaxVelocity(velocity / 3)
                        .withNumber(1)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withAccelerationSpeed(accelerationSpeed)
                        .withMaxVelocity(2 * velocity / 3)
                        .withNumber(1)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withAccelerationSpeed(accelerationSpeed)
                        .withMaxVelocity(velocity)
                        .withNumber(3)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(manuallySlowDownSpeed)
            .withNaturallySlowDownSpeed(naturallySlowDownSpeed)
            .build());
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(velocity)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanMoveableEngine.onBackwardPressed(true);
      humanMoveableEngine.moveForward(); // switch state accelerating 
      humanMoveableEngine.moveForward(); // accelerate from 0 -> 1
      humanMoveableEngine.moveForward(); // start accelerating from 1 -> 2
      Thread.sleep(1000);
      humanMoveableEngine.moveForward(); // done accelerating from 1 -> 2
      humanMoveableEngine.moveForward(); // start accelerating from 2 -> 3
      Thread.sleep(1000);
      humanMoveableEngine.moveForward(); // done accelerating from 2 -> 3
      humanMoveableEngine.moveForward(); // start 'moving backwards'
      Thread.sleep(1000);
      humanMoveableEngine.moveForward(); // state 'moving backwards'
      Thread.sleep(1000);
      humanMoveableEngine.moveForward(); // state 'moving backwards'

      // Then
      verify(humanMoveableEngine.getMoveable(), times(8)).moveBackward();
      assertThat(engineStateHandler.getCurrentVelocity(), is(velocity));
   }
}

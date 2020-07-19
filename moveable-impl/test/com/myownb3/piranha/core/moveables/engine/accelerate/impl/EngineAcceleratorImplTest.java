package com.myownb3.piranha.core.moveables.engine.accelerate.impl;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.moveables.engine.accelerate.EngineAccelerator;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;

class EngineAcceleratorImplTest {

   @Test
   void testOneGrading_DoneAccelerating() throws InterruptedException {

      // Given
      int velocity = 100;
      int expectedVelocity = 100;
      int acceleratingSpeed = 1;
      EngineAccelerator engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(velocity)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(1)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(10)
            .withNaturallySlowDownSpeed(1)
            .build();

      // When
      engineAccelerator.accelerate();
      Thread.sleep(acceleratingSpeed);
      int actualVelocity = engineAccelerator.getCurrentVelocity();

      // Then
      assertThat(actualVelocity, is(expectedVelocity));
   }

   @Test
   void testOneGrading_isDoneAccelerating4DefaultState() {

      // Given
      int velocity = 100;
      int acceleratingSpeed = 1;
      EngineAccelerator engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(velocity)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(1)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(10)
            .withNaturallySlowDownSpeed(1)
            .build();

      // When
      boolean actualIsDoneAccelerating = engineAccelerator.isDoneAccelerating();

      // Then
      assertThat(actualIsDoneAccelerating, is(true));
   }

   @Test
   void testOneGrading_GetCurrentAcceleratingSpeed4DefaultState() {

      // Given
      int velocity = 100;
      int acceleratingSpeed = 1;
      EngineAccelerator engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(velocity)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(1)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(10)
            .withNaturallySlowDownSpeed(1)
            .build();

      // When
      double actualCurrentAcceleratingSpeed = engineAccelerator.getCurrentAcceleratingSpeed();

      // Then
      assertThat(actualCurrentAcceleratingSpeed, is(0.0));
   }

   @Test
   void testOneGrading_NotAccelerated() {

      // Given
      int velocity = 100;
      int expectedVelocity = 0;
      int acceleratingSpeed = 1;
      EngineAccelerator engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(velocity)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(1)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(10)
            .withNaturallySlowDownSpeed(1)
            .build();

      // When
      int actualVelocity = engineAccelerator.getCurrentVelocity();

      // Then
      assertThat(actualVelocity, is(expectedVelocity));
   }

   @Test
   void testAccelerateThreeGradings_DontFullyAccelerate() {

      // Given
      int acceleratingSpeed = 0;
      int velocityBeforFirstAcceleration = 0;
      int expectedVelocityAfterSecondAcceleration = 33;
      int expectedVelocityAfterThirdAcceleration = 33;
      EngineAccelerator engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(velocityBeforFirstAcceleration)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(1)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(expectedVelocityAfterSecondAcceleration)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(2)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(expectedVelocityAfterThirdAcceleration)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(3)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(10)
            .withNaturallySlowDownSpeed(1)
            .build();

      // When
      int actualVelocityAfterFirstAcceleration = engineAccelerator.getCurrentVelocity();
      engineAccelerator.accelerate();
      engineAccelerator.accelerate();
      int actualVelocityAfterSecondAcceleration = engineAccelerator.getCurrentVelocity();
      engineAccelerator.accelerate();
      engineAccelerator.accelerate();
      int actualVelocityAfterThirdAcceleration = engineAccelerator.getCurrentVelocity();

      // Then
      assertThat(actualVelocityAfterFirstAcceleration, is(velocityBeforFirstAcceleration));
      assertThat(actualVelocityAfterSecondAcceleration, is(expectedVelocityAfterSecondAcceleration));
      assertThat(actualVelocityAfterThirdAcceleration, is(expectedVelocityAfterThirdAcceleration));
   }

   @Test
   void testAccelerateThreeGradings_AlwaysFullyAccelerate() throws InterruptedException {

      // Given
      int velocity = 100;
      int acceleratingSpeed = 10;
      int velocityAfterFirstAcceleration = 0;
      int expectedVelocityAfterSecondAcceleration = 67;
      int expectedVelocityAfterThirdAcceleration = velocity;
      EngineAccelerator engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(velocityAfterFirstAcceleration)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(1)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(expectedVelocityAfterSecondAcceleration)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(2)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(expectedVelocityAfterThirdAcceleration)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(3)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(10)
            .withNaturallySlowDownSpeed(1)
            .build();

      // When
      int actualVelocityBeforeFirstAcceleration = engineAccelerator.getCurrentVelocity();
      engineAccelerator.accelerate();// First accelerate -> change from gear 0 to gear 1
      engineAccelerator.accelerate();
      sleep(acceleratingSpeed);
      engineAccelerator.accelerate();
      int actualVelocityAfterSecondAcceleration = engineAccelerator.getCurrentVelocity();
      engineAccelerator.accelerate();
      sleep(acceleratingSpeed);
      engineAccelerator.accelerate();
      int actualVelocityAfterThirdAcceleration = engineAccelerator.getCurrentVelocity();

      // Then
      assertThat(actualVelocityBeforeFirstAcceleration, is(velocityAfterFirstAcceleration));
      assertThat(actualVelocityAfterSecondAcceleration, is(expectedVelocityAfterSecondAcceleration));
      assertThat(actualVelocityAfterThirdAcceleration, is(expectedVelocityAfterThirdAcceleration));
   }

   @Test
   void testSlowingDownThreeGradings() throws InterruptedException {

      // Given
      int velocity = 100;
      int acceleratingSpeed = 10;
      int velocityBeforeFirstSlowDown = velocity;
      int expectedVelocityBeforeSecondSlowDown = 67;
      int expectedVelocityBeforeThirdSlowDown = 33;
      int manuallySlowDownSpeed = 10;
      EngineAccelerator engineAccelerator = EngineAcceleratorBuilder.builder()
            .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(expectedVelocityBeforeThirdSlowDown)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(1)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(expectedVelocityBeforeSecondSlowDown)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(2)
                        .buil())
                  .addGear(GearBuilder.builder()
                        .withMaxVelocity(velocityBeforeFirstSlowDown)
                        .withAccelerationSpeed(acceleratingSpeed)
                        .withNumber(3)
                        .buil())
                  .build())
            .withManuallySlowDownSpeed(manuallySlowDownSpeed)
            .withNaturallySlowDownSpeed(1)
            .build();

      // When
      // First speed up, so we can slow down 
      engineAccelerator.accelerate(); // start 0 -> 1
      sleep(acceleratingSpeed);
      engineAccelerator.accelerate(); // end 0 -> 1
      engineAccelerator.accelerate(); // start 1 -> 2
      sleep(acceleratingSpeed);
      engineAccelerator.accelerate(); // end 1 -> 2
      engineAccelerator.accelerate(); // start 2 -> 3
      sleep(acceleratingSpeed);
      engineAccelerator.accelerate(); // end 2 -> 3
      sleep(acceleratingSpeed);

      // Then start slowing down
      int actualVelocityBeforeFirstSlowDown = engineAccelerator.getCurrentVelocity();
      engineAccelerator.slowdown(); // start slow down from 3 -> 2
      sleep(manuallySlowDownSpeed);
      engineAccelerator.slowdown(); // since the time is up, change gear from 3 -> 2
      int actualVelocityBeforeSecondSlowDown = engineAccelerator.getCurrentVelocity();
      engineAccelerator.slowdown();
      sleep(manuallySlowDownSpeed);
      engineAccelerator.slowdown(); // since the time is up, change gear from 2 -> 1
      int actualVelocityBeforeThirdSlowDown = engineAccelerator.getCurrentVelocity();

      // Then
      assertThat(actualVelocityBeforeFirstSlowDown, is(velocityBeforeFirstSlowDown));
      assertThat(actualVelocityBeforeSecondSlowDown, is(expectedVelocityBeforeSecondSlowDown));
      assertThat(actualVelocityBeforeThirdSlowDown, is(expectedVelocityBeforeThirdSlowDown));
   }
}

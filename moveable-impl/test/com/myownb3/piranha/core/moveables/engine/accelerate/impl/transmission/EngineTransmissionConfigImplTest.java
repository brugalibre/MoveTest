package com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.transmission.EngineTransmissionConfig;
import com.myownb3.piranha.core.moveables.engine.accelerate.transmission.Gear;

class EngineTransmissionConfigImplTest {

   @Test
   void testBuildAndGetGear() {

      // Given
      int maxVelocityOf2ndGear = 4;
      double accelerationSpeed2ndGear = 5.0;
      Gear gear2 = GearBuilder.builder()
            .withAccelerationSpeed(accelerationSpeed2ndGear)
            .withMaxVelocity(maxVelocityOf2ndGear)
            .withNumber(2)
            .buil();
      EngineTransmissionConfig engineTransmissionConfig = EngineTransmissionConfigBuilder.builder()
            .addGear(GearBuilder.builder()
                  .withAccelerationSpeed(1)
                  .withMaxVelocity(3)
                  .withNumber(1)
                  .buil())
            .addGear(gear2)
            .build();

      // When
      int actualAmountOfGears = engineTransmissionConfig.getAmountOfGears();

      // Then
      assertThat(gear2.getMaxVelocity(), is(maxVelocityOf2ndGear));
      assertThat(gear2.getAccelerationSpeed(), is(accelerationSpeed2ndGear));
      assertThat(gear2.getNumber(), is(2));
      assertThat(actualAmountOfGears, is(2));
   }

}

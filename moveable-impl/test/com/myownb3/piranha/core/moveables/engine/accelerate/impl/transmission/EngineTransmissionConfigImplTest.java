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
      EngineTransmissionConfig engineTransmissionConfig = EngineTransmissionConfigBuilder.builder()
            .addGear(GearBuilder.builder()
                  .withAccelerationSpeed(1)
                  .withMaxVelocity(3)
                  .withNumber(1)
                  .buil())
            .addGear(GearBuilder.builder()
                  .withAccelerationSpeed(accelerationSpeed2ndGear)
                  .withMaxVelocity(maxVelocityOf2ndGear)
                  .withNumber(2)
                  .buil())
            .build();

      // When
      Gear gear = engineTransmissionConfig.getGear(2);
      int actualAmountOfGears = engineTransmissionConfig.getAmountOfGears();

      // Then
      assertThat(gear.getMaxVelocity(), is(maxVelocityOf2ndGear));
      assertThat(gear.getAccelerationSpeed(), is(accelerationSpeed2ndGear));
      assertThat(gear.getNumber(), is(2));
      assertThat(actualAmountOfGears, is(2));
   }

}

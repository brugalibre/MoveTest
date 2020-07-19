package com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.core.moveables.engine.accelerate.transmission.EngineTransmissionConfig;
import com.myownb3.piranha.core.moveables.engine.accelerate.transmission.Gear;

public class EngineTransmissionConfigImpl implements EngineTransmissionConfig {

   private List<Gear> gears;

   private EngineTransmissionConfigImpl(List<Gear> gears) {
      this.gears = gears;
   }

   @Override
   public Gear getGear(int number) {
      return gears.get(--number);
   }

   @Override
   public int getAmountOfGears() {
      return gears.size();
   }

   public static class EngineTransmissionConfigBuilder {

      private List<Gear> gears;

      private EngineTransmissionConfigBuilder() {
         this.gears = new ArrayList<>();
      }

      public EngineTransmissionConfigImpl build() {
         requireNonNull(gears);
         return new EngineTransmissionConfigImpl(gears);
      }

      public static EngineTransmissionConfigBuilder builder() {
         return new EngineTransmissionConfigBuilder();
      }

      public EngineTransmissionConfigBuilder addGear(Gear gear) {
         gears.add(gear);
         return this;
      }
   }
}

package com.myownb3.piranha.core.moveables.engine.accelerate.transmission;

/**
 * Defines the configuration for the transmission of an engine
 * 
 * @author Dominic
 *
 */
public interface EngineTransmissionConfig {

   /**
    * Returns the {@link Gear} for the given number
    * 
    * @param number
    *        the number of the {@link Gear}
    * @return the {@link Gear} for the given number
    * @throws IndexOutOfBoundsException
    *         if there is no {@link Gear} for the specified number
    */
   Gear getGear(int number);

   /**
    * @return the amount of {@link Gear}s of this {@link EngineTransmissionConfig}
    */
   int getAmountOfGears();
}

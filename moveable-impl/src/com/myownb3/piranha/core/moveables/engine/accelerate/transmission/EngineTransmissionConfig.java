package com.myownb3.piranha.core.moveables.engine.accelerate.transmission;

/**
 * Defines the configuration for the transmission of an engine
 * 
 * @author Dominic
 *
 */
public interface EngineTransmissionConfig {

   /**
    * @return the max velocity which is possible at the current {@link Gear}
    * @param gearNumber
    *        the number of the {@link Gear}
    * @throws IndexOutOfBoundsException
    *         if there is no {@link Gear} for the specified number
    */
   int getCurrentMaxVelocity(int gearNumber);

   /**
    * @return the acceleration speed of the engine at the current {@link Gear}
    * @param gearNumber
    *        the number of the {@link Gear}
    * @throws IndexOutOfBoundsException
    *         if there is no {@link Gear} for the specified number
    */
   double getCurrentAccelerationSpeed(int gearNumber);

   /**
    * @return the amount of {@link Gear}s of this {@link EngineTransmissionConfig}
    */
   int getAmountOfGears();
}

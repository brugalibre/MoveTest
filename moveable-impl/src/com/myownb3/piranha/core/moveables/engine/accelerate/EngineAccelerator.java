package com.myownb3.piranha.core.moveables.engine.accelerate;

/**
 * The {@link EngineAccelerator} is responsible for accelerate and slow down an engine
 * 
 * @author Dominic
 *
 */
public interface EngineAccelerator {

   /**
    * This will lead the {@link EngineAccelerator} to start the accelerating
    */
   void accelerate();

   /**
    * This will lead the {@link EngineAccelerator} to start slowing down
    */
   void slowdown();

   /**
    * This will lead the {@link EngineAccelerator} to start slowing down but slower, since it's cause is naturally
    */
   void slowdownNaturally();

   /**
    * @return <code>true</code> if this {@link EngineAccelerator} is done with accelerating or slowing down
    */
   boolean isDoneAccelerating();

   /**
    * Returns the accelerations speed according to the current grading and gear transmission ratio
    * 
    * @return the accelerations speed according to the current grading
    */
   double getCurrentAcceleratingSpeed();

   /**
    * Returns the velocity according to the current grading and gear transmission ratio
    * 
    * @return the velocity according to the current grading
    */
   int getCurrentVelocity();

}

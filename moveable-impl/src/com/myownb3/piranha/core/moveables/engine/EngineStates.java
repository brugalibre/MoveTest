package com.myownb3.piranha.core.moveables.engine;

/**
 * Defines the different states of an engine
 * 
 * @author Dominic
 *
 */
public enum EngineStates {

   /** The engine is running but still dile */
   IDLE,

   /** The engine is accelerating */
   ACCELERATING,

   /** The engine is slowing down, triggered by hitting the brakes */
   SLOWINGDOWN,

   /** The engine is slowing down naturally. This means by stop accelerating */
   SLOWINGDOWN_NATURALLY,

   /** The engine is moving forwards */
   MOVING_FORWARDS,

   /** The engine is moving backwards */
   MOVING_BACKWARDS,

   /** Only for testing purpose */
   NONE,
}

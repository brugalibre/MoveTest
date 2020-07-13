package com.myownb3.piranha.core.destruction;

/**
 * A callback when ever the {@link DestructionHelper} was destroyed
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface OnDestroyedCallbackHandler {

   /**
    * Call in order to propagate the destruction
    */
   void onDestroy();
}
